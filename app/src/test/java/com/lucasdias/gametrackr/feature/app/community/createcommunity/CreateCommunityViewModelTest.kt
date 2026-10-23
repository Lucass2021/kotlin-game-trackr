package com.lucasdias.gametrackr.feature.app.community.createcommunity

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.data.community.FakeCommunityRepository
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CreateCommunityViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeCommunityRepository()

    private fun viewModel(
        name: String = NAME,
        description: String = DESCRIPTION,
    ) = CreateCommunityViewModel(repository).apply {
        onNameChange(name)
        onDescriptionChange(description)
    }

    @Test
    fun `name spaces are removed from the handle`() {
        val state = viewModel(name = "Retro Collectors").uiState.value

        assertEquals("RetroCollectors", state.handle)
        assertTrue(state.isRenamed)
    }

    @Test
    fun `errors show only after the first submit`() =
        runTest {
            val viewModel = viewModel(name = "", description = "")
            assertNull(viewModel.uiState.value.nameError)

            viewModel.onSubmit()

            assertEquals(UiText.Resource(R.string.validation_community_name_required), viewModel.uiState.value.nameError)
            assertEquals(0, repository.createCommunityCount)
        }

    @Test
    fun `submit creates the community with the handle and the trimmed description`() =
        runTest {
            val viewModel = viewModel(name = "Retro Collectors", description = "  $DESCRIPTION  ")

            viewModel.onSubmit()
            advanceUntilIdle()

            val created = viewModel.uiState.value.createdCommunity
            assertEquals("RetroCollectors", created?.name)
            assertEquals(DESCRIPTION, created?.description)
            assertTrue(created?.isJoined == true)
            assertFalse(viewModel.uiState.value.isSubmitting)
        }

    @Test
    fun `server error says the name may be taken and keeps the form`() =
        runTest {
            repository.failure = ApiError.Server(SERVER_ERROR)
            val viewModel = viewModel()

            viewModel.onSubmit()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(UiText.Resource(R.string.create_community_name_taken), state.toastMessage)
            assertEquals(NAME, state.name)
            assertNull(state.createdCommunity)
        }

    @Test
    fun `other errors show the api message`() =
        runTest {
            repository.failure = ApiError.Network
            val viewModel = viewModel()

            viewModel.onSubmit()
            advanceUntilIdle()

            assertEquals(UiText.Resource(R.string.error_network), viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `submit after a successful create sends no request`() =
        runTest {
            val viewModel = viewModel()
            viewModel.onSubmit()
            advanceUntilIdle()

            viewModel.onSubmit()
            advanceUntilIdle()

            assertEquals(1, repository.createCommunityCount)
        }

    @Test
    fun `description limit ignores surrounding whitespace`() {
        val body = "a".repeat(CreateCommunityUiState.DESCRIPTION_LIMIT)

        assertTrue(viewModel(description = "  $body  ").uiState.value.canSubmit)
    }

    private companion object {
        const val NAME = "RetroClub"
        const val DESCRIPTION = "A club for retro games."
        const val SERVER_ERROR = 500
    }
}
