package com.lucasdias.gametrackr.feature.app.profile

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.profile.FakeProfileOverviewRepository
import com.lucasdias.gametrackr.data.profile.UserProfileMockData
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class UserProfileViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val user = UserProfileMockData.user
    private val repository = FakeProfileOverviewRepository()

    private fun viewModel() = UserProfileViewModel(repository, user)

    @Test
    fun `load shows the header, stats and sections from the repository`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(UserProfileMockData.BIO, state.profile?.bio)
            assertEquals(user.username, state.profile?.username)
            assertEquals(UserProfileMockData.stats, state.overview?.stats)
            assertFalse(state.isFriend)
        }

    @Test
    fun `load failure shows the error state instead of loading forever`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = IllegalStateException()
            val viewModel = viewModel()

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.hasError)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `retry after a failure shows the profile`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = IllegalStateException()
            val viewModel = viewModel()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.failure = null
            viewModel.load()
            advanceUntilIdle()

            assertEquals(
                UserProfileMockData.stats,
                viewModel.uiState.value.overview
                    ?.stats,
            )
            assertFalse(viewModel.uiState.value.hasError)
        }

    @Test
    fun `returning to the screen keeps the profile without fetching again`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(1, repository.fetchCount)
        }

    @Test
    fun `adding a friend saves it and survives reopening the profile`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.onFriendAction(isGuest = false)
            advanceUntilIdle()
            val reopened = viewModel()
            reopened.loadIfNeeded()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isFriend)
            assertTrue(repository.isFriend(user.username))
            assertTrue(reopened.uiState.value.isFriend)
        }

    @Test
    fun `a friend failure restores the previous state and shows an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.failure = IllegalStateException()
            viewModel.onFriendAction(isGuest = false)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isFriend)
            assertEquals(R.string.user_profile_friend_error, viewModel.uiState.value.toastMessage)

            viewModel.onToastShown()

            assertNull(viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `a guest gets a toast and nothing reaches the repository`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.onFriendAction(isGuest = true)
            advanceUntilIdle()

            assertEquals(R.string.user_profile_guest_add_friend, viewModel.uiState.value.toastMessage)
            assertFalse(viewModel.uiState.value.isFriend)
            assertEquals(0, repository.setFriendCount)

            viewModel.onMessage(isGuest = true)

            assertEquals(R.string.user_profile_guest_message, viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `a quick second tap supersedes the first without an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.onFriendAction(isGuest = false)
            runCurrent()
            viewModel.onFriendAction(isGuest = false)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isFriend)
            assertFalse(repository.isFriend(user.username))
            assertNull(viewModel.uiState.value.toastMessage)
        }
}
