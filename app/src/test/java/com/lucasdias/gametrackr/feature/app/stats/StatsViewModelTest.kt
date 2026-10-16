package com.lucasdias.gametrackr.feature.app.stats

import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.stats.FakeStatsRepository
import com.lucasdias.gametrackr.data.stats.StatsMockData
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class StatsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `load shows the stats from the repository`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = StatsViewModel(FakeStatsRepository())

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(StatsMockData.stats, viewModel.uiState.value.stats)
            assertFalse(viewModel.uiState.value.isLoading)
            assertFalse(viewModel.uiState.value.hasError)
        }

    @Test
    fun `load failure shows the error state instead of loading forever`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = StatsViewModel(FakeStatsRepository(failure = IllegalStateException()))

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.hasError)
            assertFalse(viewModel.uiState.value.isLoading)
            assertNull(viewModel.uiState.value.stats)
        }

    @Test
    fun `retry after a failure shows the stats`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeStatsRepository(failure = IllegalStateException())
            val viewModel = StatsViewModel(repository)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.failure = null
            viewModel.load()
            advanceUntilIdle()

            assertEquals(StatsMockData.stats, viewModel.uiState.value.stats)
            assertFalse(viewModel.uiState.value.hasError)
        }

    @Test
    fun `returning to the screen keeps the stats without fetching again`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeStatsRepository()
            val viewModel = StatsViewModel(repository)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(1, repository.fetchCount)
            assertEquals(StatsMockData.stats, viewModel.uiState.value.stats)
        }

    @Test
    fun `a second load replaces the one in flight without showing an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeStatsRepository(failure = IllegalStateException())
            val viewModel = StatsViewModel(repository)

            viewModel.load()
            runCurrent()
            repository.failure = null
            viewModel.load()
            advanceUntilIdle()

            assertEquals(StatsMockData.stats, viewModel.uiState.value.stats)
            assertFalse(viewModel.uiState.value.hasError)
        }
}
