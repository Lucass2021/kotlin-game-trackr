package com.lucasdias.gametrackr.feature.app.achievements

import com.lucasdias.gametrackr.core.model.AchievementFilter
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.achievements.AchievementsMockData
import com.lucasdias.gametrackr.data.achievements.FakeAchievementsRepository
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AchievementsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val games = AchievementsMockData.games
    private val firstPage = games.take(AchievementsUiState.PAGE_SIZE)
    private val inProgress = games.filter { !it.isComplete && it.unlockedCount > 0 }

    @Test
    fun `load shows the first page and the summary`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = AchievementsViewModel(FakeAchievementsRepository())

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(firstPage, viewModel.uiState.value.games)
            assertTrue(viewModel.uiState.value.showsSummary)
            assertTrue(viewModel.uiState.value.canLoadMore)
        }

    @Test
    fun `load failure shows the error state instead of loading forever`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = AchievementsViewModel(FakeAchievementsRepository(failure = IllegalStateException()))

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.hasError)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `retry after a failure shows the games`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeAchievementsRepository(failure = IllegalStateException())
            val viewModel = AchievementsViewModel(repository)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.failure = null
            viewModel.load()
            advanceUntilIdle()

            assertEquals(firstPage, viewModel.uiState.value.games)
            assertFalse(viewModel.uiState.value.hasError)
        }

    @Test
    fun `returning to the screen keeps the games without fetching again`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeAchievementsRepository()
            val viewModel = AchievementsViewModel(repository)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(1, repository.fetchCount)
            assertEquals(firstPage, viewModel.uiState.value.games)
        }

    @Test
    fun `filtering finds matching games beyond the first page`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = AchievementsViewModel(FakeAchievementsRepository())
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.onFilterChange(AchievementFilter.IN_PROGRESS)
            advanceUntilIdle()

            assertEquals(inProgress.take(AchievementsUiState.PAGE_SIZE), viewModel.uiState.value.games)
            assertFalse(viewModel.uiState.value.showsSummary)
        }

    @Test
    fun `a filter change replaces the load in flight`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = AchievementsViewModel(FakeAchievementsRepository())

            viewModel.loadIfNeeded()
            runCurrent()
            viewModel.onFilterChange(AchievementFilter.IN_PROGRESS)
            advanceUntilIdle()

            assertEquals(inProgress.take(AchievementsUiState.PAGE_SIZE), viewModel.uiState.value.games)
        }

    @Test
    fun `loading more appends the next page until the last one`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = AchievementsViewModel(FakeAchievementsRepository())
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.loadMore()
            advanceUntilIdle()

            assertEquals(games, viewModel.uiState.value.games)
            assertFalse(viewModel.uiState.value.canLoadMore)
        }

    @Test
    fun `a load more failure keeps the games and stops until retried`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeAchievementsRepository()
            val viewModel = AchievementsViewModel(repository)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.failure = IllegalStateException()
            viewModel.loadMore()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.loadMoreFailed)
            assertFalse(viewModel.uiState.value.canLoadMore)
            assertEquals(firstPage, viewModel.uiState.value.games)

            repository.failure = null
            viewModel.loadMore()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.loadMoreFailed)
            assertEquals(games, viewModel.uiState.value.games)
        }
}
