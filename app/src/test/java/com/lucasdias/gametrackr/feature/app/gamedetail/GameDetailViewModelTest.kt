package com.lucasdias.gametrackr.feature.app.gamedetail

import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.game.FakeGameRepository
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

    private fun viewModel() = GameDetailViewModel(repository, SLUG)

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

    private companion object {
        const val SLUG = "neon-ascent-revival"
    }
}
