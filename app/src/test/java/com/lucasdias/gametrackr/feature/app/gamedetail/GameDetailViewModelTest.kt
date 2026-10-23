package com.lucasdias.gametrackr.feature.app.gamedetail

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.model.NewLibraryEntry
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.game.FakeGameRepository
import com.lucasdias.gametrackr.data.library.FakeLibraryRepository
import com.lucasdias.gametrackr.data.library.LibraryUpdates
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class GameDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeGameRepository()

    private val libraryRepository = FakeLibraryRepository(entries = emptyList())
    private val libraryUpdates = LibraryUpdates()

    private fun viewModel() = GameDetailViewModel(repository, libraryRepository, libraryUpdates, SLUG)

    @Test
    fun `load shows the game from the repository`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()

            viewModel.loadIfNeeded()
            runCurrent()
            assertTrue(viewModel.uiState.value.isLoading)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(FakeGameRepository.gameDetail, state.game)
            assertFalse(state.isLoading)
            assertFalse(state.hasError)
        }

    @Test
    fun `load failure shows the error state instead of loading forever`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.fetchGameFailure = IllegalStateException()
            val viewModel = viewModel()

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue(state.hasError)
            assertFalse(state.isLoading)
            assertNull(state.game)
        }

    @Test
    fun `retry after a failure shows the game`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.fetchGameFailure = IllegalStateException()
            val viewModel = viewModel()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.fetchGameFailure = null
            viewModel.load()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(FakeGameRepository.gameDetail, state.game)
            assertFalse(state.hasError)
        }

    @Test
    fun `returning to the screen keeps the game without fetching again`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(1, repository.fetchGameCount)
        }

    @Test
    fun `a second appearance while loading does not start another request`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()

            viewModel.loadIfNeeded()
            runCurrent()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(1, repository.fetchGameCount)
        }

    @Test
    fun `adding the game to the library shows the confirmation once`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()

            viewModel.onAddedToLibrary()
            assertEquals(R.string.add_library_saved, viewModel.uiState.value.toastMessage)
            viewModel.onToastShown()

            assertNull(viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `a game already in the library shows it`() =
        runTest(mainDispatcherRule.dispatcher) {
            libraryRepository.addToLibrary(newEntry())
            val viewModel = viewModel()

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isInLibrary)
        }

    @Test
    fun `saving this game from the sheet marks it as in the library`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            viewModel.loadIfNeeded()
            advanceUntilIdle()
            assertFalse(viewModel.uiState.value.isInLibrary)

            libraryUpdates.publish(OTHER_SLUG)
            advanceUntilIdle()
            assertFalse(viewModel.uiState.value.isInLibrary)

            libraryUpdates.publish(SLUG)
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.isInLibrary)
        }

    private fun newEntry(): NewLibraryEntry {
        val game = FakeGameRepository.gameDetail
        return NewLibraryEntry(
            title = game.title,
            slug = SLUG,
            status = LibraryStatus.PLAYING,
            rating = 0,
            hours = null,
            platform = null,
            startedAt = null,
            finishedAt = null,
            review = null,
            coverStart = game.coverStart,
            coverEnd = game.coverEnd,
        )
    }

    private companion object {
        const val SLUG = "neon-ascent-revival"
        const val OTHER_SLUG = "another-game"
    }
}
