package com.lucasdias.gametrackr.feature.app.settings.changepassword

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.data.auth.FakePasswordRepository
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ChangePasswordViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakePasswordRepository()

    private fun viewModel(
        current: String = CURRENT,
        new: String = NEW,
        confirm: String = NEW,
    ) = ChangePasswordViewModel(repository).apply {
        onCurrentPasswordChange(current)
        onNewPasswordChange(new)
        onConfirmPasswordChange(confirm)
    }

    @Test
    fun `errors show only after the first save`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel(current = "", new = "", confirm = "")
            assertNull(viewModel.uiState.value.currentPasswordError)

            viewModel.onSave()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(R.string.change_password_error_current_required, state.currentPasswordError)
            assertEquals(R.string.change_password_error_new_required, state.newPasswordError)
            assertEquals(R.string.change_password_error_confirm_required, state.confirmPasswordError)
            assertEquals(0, repository.changeCount)
        }

    @Test
    fun `short and mismatched passwords are rejected`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel(new = "abc", confirm = "abd")

            viewModel.onSave()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(R.string.validation_password_too_short, state.newPasswordError)
            assertEquals(R.string.validation_passwords_dont_match, state.confirmPasswordError)
            assertEquals(0, repository.changeCount)
        }

    @Test
    fun `save sends the new password and marks it as changed`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()

            viewModel.onSave()
            advanceUntilIdle()

            assertEquals(1, repository.changeCount)
            assertEquals(CURRENT, repository.lastCurrentPassword)
            assertEquals(NEW, repository.lastNewPassword)
            assertTrue(viewModel.uiState.value.isChanged)
            assertFalse(viewModel.uiState.value.isSaving)
        }

    @Test
    fun `a save failure shows a toast and keeps the form`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = ApiError.Network
            val viewModel = viewModel()

            viewModel.onSave()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(UiText.Resource(R.string.error_network), state.toastMessage)
            assertFalse(state.isChanged)
            assertEquals(NEW, state.newPassword)
        }

    @Test
    fun `a second tap while saving and a save after success send no request`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()

            viewModel.onSave()
            viewModel.onSave()
            advanceUntilIdle()
            viewModel.onSave()
            advanceUntilIdle()

            assertEquals(1, repository.changeCount)
        }

    private companion object {
        const val CURRENT = "old-secret"
        const val NEW = "new-secret"
    }
}
