package com.lucasdias.gametrackr.feature.app.search

import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.FakeLatency
import com.lucasdias.gametrackr.data.game.FakeGameRepository
import com.lucasdias.gametrackr.data.game.HomeMockData
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val manyGames = List(SearchUiState.PAGE_SIZE + 5) { Game(id = 100 + it, name = "Game $it", slug = "game-$it") }
    private val repository = FakeGameRepository(searchResults = manyGames)
    private val viewModel by lazy { SearchViewModel(SearchScope.ALL, repository) }

    @Test
    fun `load shows the first page and the platforms`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(SearchUiState.PAGE_SIZE, state.games.size)
            assertEquals(manyGames.size, state.total)
            assertEquals(HomeMockData.platforms, state.platforms)
        }

    @Test
    fun `coming back to search does not reload it`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(1, repository.searchCount)
        }

    @Test
    fun `the empty state waits for the first page`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = SearchViewModel(SearchScope.ALL, FakeGameRepository(searchResults = emptyList()))

            viewModel.loadIfNeeded()
            runCurrent()

            assertFalse(viewModel.uiState.value.isEmpty)
            assertTrue(viewModel.uiState.value.isWaiting)

            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isEmpty)
        }

    @Test
    fun `a load failure shows the error state instead of no results and retry loads`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.searchFailure = IllegalStateException()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.hasError)
            assertFalse(viewModel.uiState.value.isEmpty)

            repository.searchFailure = null
            viewModel.load()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.hasError)
            assertEquals(SearchUiState.PAGE_SIZE, viewModel.uiState.value.games.size)
        }

    @Test
    fun `a load more failure shows the retry row and pauses infinite scroll`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.searchFailure = IllegalStateException()
            viewModel.loadMore()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.loadMoreFailed)
            assertFalse(viewModel.uiState.value.canLoadMore)

            repository.searchFailure = null
            viewModel.loadMore()
            advanceUntilIdle()

            assertEquals(manyGames.size, viewModel.uiState.value.games.size)
        }

    @Test
    fun `typing a search asks the repository once after the pause`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.onQueryChange("z")
            viewModel.onQueryChange("ze")
            viewModel.onQueryChange("zelda")
            advanceUntilIdle()

            assertEquals(2, repository.searchCount)
            assertEquals("zelda", repository.lastSearch)
        }

    @Test
    fun `a keystroke that keeps the same filter does not search again`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            runCurrent()

            viewModel.onQueryChange(" ")
            advanceUntilIdle()

            assertEquals(1, repository.searchCount)
            assertEquals(SearchUiState.PAGE_SIZE, viewModel.uiState.value.games.size)
            assertFalse(viewModel.uiState.value.isSearchPending)
        }

    @Test
    fun `a load more while a search is pending is ignored`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.onQueryChange("zelda")
            viewModel.loadMore()
            runCurrent()

            assertEquals(1, repository.searchCount)
        }

    @Test
    fun `a platform asks the repository with its slug and clearing resets the filters`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()
            val platform = HomeMockData.platforms.first()

            viewModel.onPlatformChange(platform.slug)
            advanceUntilIdle()

            assertEquals(platform.slug, repository.lastPlatformSlug)

            viewModel.onQueryChange("zelda")
            advanceUntilIdle()
            viewModel.onClearFilters()
            advanceUntilIdle()

            assertEquals("", viewModel.uiState.value.query)
            assertNull(viewModel.uiState.value.platformSlug)
            assertEquals(manyGames.size, viewModel.uiState.value.total)
            assertEquals(SearchUiState.PAGE_SIZE, viewModel.uiState.value.games.size)
        }

    @Test
    fun `a filter loaded before comes from the cache`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()
            val platform = HomeMockData.platforms.first()
            viewModel.onPlatformChange(platform.slug)
            advanceUntilIdle()

            viewModel.onPlatformChange(null)
            advanceUntilIdle()

            assertEquals(2, repository.searchCount)
            assertEquals(SearchUiState.PAGE_SIZE, viewModel.uiState.value.games.size)
        }

    @Test
    fun `an older page does not replace a newer filter`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()
            val platform = HomeMockData.platforms.first()
            viewModel.onQueryChange("zelda")
            advanceTimeBy(SEARCH_DEBOUNCE_MS + FakeLatency.RESPONSE_DELAY_MS / 2)

            viewModel.onPlatformChange(platform.slug)
            advanceTimeBy(FakeLatency.RESPONSE_DELAY_MS * 3 / 4)

            assertTrue(
                viewModel.uiState.value.games
                    .isEmpty(),
            )
            assertTrue(viewModel.uiState.value.isLoading)
            assertEquals(platform.slug, repository.lastPlatformSlug)
        }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 300L
    }
}
