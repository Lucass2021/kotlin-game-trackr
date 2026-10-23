package com.lucasdias.gametrackr.feature.app.profile.editprofile

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.auth.AuthStatus
import com.lucasdias.gametrackr.core.auth.SessionManager
import com.lucasdias.gametrackr.core.model.ProfileVisibility
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.data.profile.FakeProfileRepository
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EditProfileViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeProfileRepository()
    private val sessionManager = SessionManager()

    private fun viewModel() = EditProfileViewModel(ProfileMockData.profile, repository, sessionManager)

    @Test
    fun `starts from the profile and loads the colors`() =
        runTest {
            val viewModel = viewModel()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals("lucasdias", state.username)
            assertEquals(ProfileMockData.colors, state.colors)
            assertFalse(state.hasChanges)
        }

    @Test
    fun `colors failure shows retry and retry loads them`() =
        runTest {
            repository.failure = ApiError.Network
            val viewModel = viewModel()
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.colorsFailed)

            repository.failure = null
            viewModel.loadColors()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.colorsFailed)
            assertEquals(ProfileMockData.colors, viewModel.uiState.value.colors)
            assertEquals(2, repository.fetchColorsCount)
        }

    @Test
    fun `errors show only after the first save attempt`() =
        runTest {
            val viewModel = viewModel()
            advanceUntilIdle()
            viewModel.onNameChange("Al")
            assertNull(viewModel.uiState.value.nameError)

            viewModel.onSave()

            assertEquals(UiText.Resource(R.string.validation_name_too_short), viewModel.uiState.value.nameError)
            assertEquals(0, repository.updateCount)
        }

    @Test
    fun `save sends trimmed values, updates the session and emits the saved profile`() =
        runTest {
            val viewModel = viewModel()
            advanceUntilIdle()
            viewModel.onNameChange("  Lucas  ")
            viewModel.onUsernameChange(" LucasD ")
            viewModel.onVisibilityChange(ProfileVisibility.PRIVATE)

            viewModel.onSave()
            advanceUntilIdle()

            val saved = viewModel.uiState.value.savedProfile
            assertEquals("Lucas", saved?.name)
            assertEquals("@lucasd", saved?.username)
            assertEquals(ProfileVisibility.PRIVATE, saved?.visibility)
            assertEquals("lucasd", repository.user.username)
            assertEquals(AuthStatus.Authenticated(repository.user), sessionManager.status.value)
        }

    @Test
    fun `save failure keeps the edits and shows the server message`() =
        runTest {
            val viewModel = viewModel()
            advanceUntilIdle()
            viewModel.onNameChange("Lucas")
            repository.failure = ApiError.Validation(mapOf("username" to listOf(USERNAME_TAKEN)), USERNAME_TAKEN)

            viewModel.onSave()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(UiText.Raw(USERNAME_TAKEN), state.toastMessage)
            assertEquals("Lucas", state.name)
            assertFalse(state.isSaving)
            assertNull(state.savedProfile)
        }

    @Test
    fun `double tap on save sends one request`() =
        runTest {
            val viewModel = viewModel()
            advanceUntilIdle()

            viewModel.onSave()
            viewModel.onSave()
            advanceUntilIdle()

            assertEquals(1, repository.updateCount)
        }

    @Test
    fun `save after a successful save sends no request`() =
        runTest {
            val viewModel = viewModel()
            advanceUntilIdle()
            viewModel.onSave()
            advanceUntilIdle()

            viewModel.onSave()
            advanceUntilIdle()

            assertEquals(1, repository.updateCount)
        }

    @Test
    fun `has changes compares trimmed values with the profile`() =
        runTest {
            val viewModel = viewModel()
            viewModel.onNameChange("${ProfileMockData.profile.name}  ")
            assertFalse(viewModel.uiState.value.hasChanges)

            viewModel.onAvatarColorChange(ProfileMockData.colors.last().hex)
            assertTrue(viewModel.uiState.value.hasChanges)
        }

    private companion object {
        const val USERNAME_TAKEN = "The username has already been taken."
    }
}
