package com.lucasdias.gametrackr.feature.app.achievements

import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.achievements.AchievementsMockData
import com.lucasdias.gametrackr.data.achievements.FakeAchievementsRepository
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class GameAchievementsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val game = AchievementsMockData.games[4]
    private val unknownGameId = -1L

    @Test
    fun `load shows the game with unlocked and locked achievements apart`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = GameAchievementsViewModel(FakeAchievementsRepository(), game.id)

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(game, state.game)
            assertEquals(game.achievements.filter { it.isUnlocked }, state.unlockedAchievements)
            assertEquals(game.achievements.filterNot { it.isUnlocked }, state.lockedAchievements)
            assertTrue(state.unlockedAchievements.isNotEmpty())
            assertTrue(state.lockedAchievements.isNotEmpty())
        }

    @Test
    fun `load failure shows the error state instead of loading forever`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel =
                GameAchievementsViewModel(FakeAchievementsRepository(failure = IllegalStateException()), game.id)

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.hasError)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `an unknown game shows the error state`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = GameAchievementsViewModel(FakeAchievementsRepository(), unknownGameId)

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.hasError)
            assertNull(viewModel.uiState.value.game)
        }

    @Test
    fun `retry after a failure shows the game`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeAchievementsRepository(failure = IllegalStateException())
            val viewModel = GameAchievementsViewModel(repository, game.id)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.failure = null
            viewModel.load()
            advanceUntilIdle()

            assertEquals(game, viewModel.uiState.value.game)
            assertFalse(viewModel.uiState.value.hasError)
        }

    @Test
    fun `returning to the screen keeps the game without fetching again`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeAchievementsRepository()
            val viewModel = GameAchievementsViewModel(repository, game.id)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(1, repository.gameFetchCount)
            assertEquals(game, viewModel.uiState.value.game)
        }

    @Test
    fun `a retry replaces the load in flight`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeAchievementsRepository()
            val viewModel = GameAchievementsViewModel(repository, game.id)

            viewModel.loadIfNeeded()
            runCurrent()
            viewModel.load()
            advanceUntilIdle()

            assertEquals(2, repository.gameFetchCount)
            assertEquals(game, viewModel.uiState.value.game)
            assertFalse(viewModel.uiState.value.isLoading)
        }
}
