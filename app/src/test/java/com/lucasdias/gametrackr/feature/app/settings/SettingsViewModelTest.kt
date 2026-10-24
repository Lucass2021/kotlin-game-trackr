package com.lucasdias.gametrackr.feature.app.settings

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.NotificationSetting
import com.lucasdias.gametrackr.core.model.NotificationSettings
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.data.auth.FakeAccountRepository
import com.lucasdias.gametrackr.data.notifications.FakeNotificationSettingsRepository
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val notificationSettings = FakeNotificationSettingsRepository(NotificationSettings(friendRequests = false))
    private val accounts = FakeAccountRepository()

    private fun viewModel() = SettingsViewModel(notificationSettings, accounts)

    @Test
    fun `load shows the stored notification settings`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(NotificationSettings(friendRequests = false), viewModel.uiState.value.notificationSettings)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `a toggle is saved and survives reopening the screen`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.onNotificationToggle(NotificationSetting.PUSH, false)
            assertFalse(
                viewModel.uiState.value.notificationSettings!!
                    .push,
            )
            advanceUntilIdle()

            val reopened = viewModel()
            reopened.loadIfNeeded()
            advanceUntilIdle()
            assertFalse(
                reopened.uiState.value.notificationSettings!!
                    .push,
            )
            assertEquals(1, notificationSettings.updateCount)
        }

    @Test
    fun `a failed toggle reverts only that setting and shows a toast`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            notificationSettings.failure = ApiError.Network
            viewModel.onNotificationToggle(NotificationSetting.COMMUNITY_REPLIES, false)
            advanceUntilIdle()

            assertEquals(NotificationSettings(friendRequests = false), viewModel.uiState.value.notificationSettings)
            assertEquals(UiText.Resource(R.string.settings_notification_update_error), viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `a quick second toggle supersedes the first without an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            notificationSettings.failure = ApiError.Network
            viewModel.onNotificationToggle(NotificationSetting.PUSH, false)
            runCurrent()
            notificationSettings.failure = null
            viewModel.onNotificationToggle(NotificationSetting.PUSH, true)
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.notificationSettings!!
                    .push,
            )
            assertNull(viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `a load failure shows an error and retry loads the settings`() =
        runTest(mainDispatcherRule.dispatcher) {
            notificationSettings.failure = ApiError.Network
            val viewModel = viewModel()
            viewModel.loadIfNeeded()
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.hasError)

            notificationSettings.failure = null
            viewModel.load()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.hasError)
            assertEquals(NotificationSettings(friendRequests = false), viewModel.uiState.value.notificationSettings)
        }

    @Test
    fun `delete account sends one request and then signs out`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()

            viewModel.onDeleteAccount()
            viewModel.onDeleteAccount()
            advanceUntilIdle()
            viewModel.onDeleteAccount()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isDeleted)
            assertEquals(1, accounts.deleteCount)
        }

    @Test
    fun `a delete failure shows a toast and keeps the user signed in`() =
        runTest(mainDispatcherRule.dispatcher) {
            accounts.failure = ApiError.Network
            val viewModel = viewModel()

            viewModel.onDeleteAccount()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isDeleted)
            assertFalse(viewModel.uiState.value.isDeleting)
            assertEquals(UiText.Resource(R.string.error_network), viewModel.uiState.value.toastMessage)
        }
}
