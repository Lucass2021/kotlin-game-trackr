package com.lucasdias.gametrackr.feature.app.home

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.game.FakeGameRepository
import com.lucasdias.gametrackr.data.game.HomeMockData
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeGameRepository()
    private val viewModel by lazy { HomeViewModel(repository) }

    @Test
    fun `load shows both sections`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(listOf(HomeMockData.sampleGame), state.newReleases.games)
            assertEquals(listOf(HomeMockData.sampleGame), state.mostAnticipated.games)
        }

    @Test
    fun `coming back to home does not reload it`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(1, repository.fetchNewReleasesCount)
            assertEquals(1, repository.fetchMostAnticipatedCount)
        }

    @Test
    fun `a failed section shows its retry and the other section stays loaded`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.mostAnticipatedFailure = IllegalStateException()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue(state.mostAnticipated.hasError)
            assertTrue(state.mostAnticipated.games.isEmpty())
            assertFalse(state.newReleases.hasError)
            assertTrue(state.newReleases.games.isNotEmpty())
        }

    @Test
    fun `retrying a section reloads only that section`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.mostAnticipatedFailure = IllegalStateException()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.mostAnticipatedFailure = null
            viewModel.onRetry(HomeSection.MOST_ANTICIPATED)
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.mostAnticipated.games
                    .isNotEmpty(),
            )
            assertEquals(1, repository.fetchNewReleasesCount)
            assertEquals(2, repository.fetchMostAnticipatedCount)
        }

    @Test
    fun `refresh keeps the games on screen until the new ones arrive`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.refresh()
            runCurrent()

            val state = viewModel.uiState.value
            assertTrue(state.isRefreshing)
            assertTrue(state.newReleases.games.isNotEmpty())

            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isRefreshing)
            assertEquals(2, repository.fetchNewReleasesCount)
        }

    @Test
    fun `a refresh failure keeps the games and shows a toast`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.newReleasesFailure = IllegalStateException()
            viewModel.refresh()
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.newReleases.games
                    .isNotEmpty(),
            )
            assertEquals(R.string.home_refresh_error, viewModel.uiState.value.toastMessage)

            viewModel.onToastShown()

            assertNull(viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `an empty section waits for the first answer`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = HomeViewModel(FakeGameRepository(homeGames = emptyList()))

            viewModel.loadIfNeeded()
            runCurrent()

            assertTrue(viewModel.uiState.value.newReleases.isWaiting)

            advanceUntilIdle()

            val feed = viewModel.uiState.value.newReleases
            assertFalse(feed.isWaiting)
            assertFalse(feed.hasError)
            assertTrue(feed.games.isEmpty())
        }

    @Test
    fun `a refresh during the first load reuses it`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            runCurrent()
            viewModel.refresh()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(1, repository.fetchNewReleasesCount)
            assertEquals(1, repository.fetchMostAnticipatedCount)
            assertFalse(state.isRefreshing)
            assertTrue(state.newReleases.games.isNotEmpty())
        }

    @Test
    fun `a refresh failure with no games on screen shows no toast`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.newReleasesFailure = IllegalStateException()
            repository.mostAnticipatedFailure = IllegalStateException()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.refresh()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertNull(state.toastMessage)
            assertTrue(state.newReleases.hasError)
            assertTrue(state.mostAnticipated.hasError)
        }
}
