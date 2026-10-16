package com.lucasdias.gametrackr.feature.app.library

import com.lucasdias.gametrackr.core.model.LibraryStats
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.library.FakeLibraryRepository
import com.lucasdias.gametrackr.data.library.LibraryMockData
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LibraryViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val entries = LibraryMockData.entries

    @Test
    fun `loading for a user shows the first page and the stats`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = LibraryViewModel(FakeLibraryRepository())

            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(entries.take(LibraryUiState.PAGE_SIZE), state.entries)
            assertEquals(LibraryMockData.stats, state.stats)
            assertTrue(state.canLoadMore)
        }

    @Test
    fun `load failure shows the error state instead of loading forever`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = LibraryViewModel(FakeLibraryRepository(failure = IllegalStateException()))

            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.hasError)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `retry after a failure shows the entries`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeLibraryRepository(failure = IllegalStateException())
            val viewModel = LibraryViewModel(repository)
            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            repository.failure = null
            viewModel.load()
            advanceUntilIdle()

            assertEquals(entries.take(LibraryUiState.PAGE_SIZE), viewModel.uiState.value.entries)
            assertFalse(viewModel.uiState.value.hasError)
        }

    @Test
    fun `coming back to the tab keeps the entries without fetching again`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeLibraryRepository()
            val viewModel = LibraryViewModel(repository)
            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            assertEquals(1, repository.fetchCount)
            assertEquals(entries.take(LibraryUiState.PAGE_SIZE), viewModel.uiState.value.entries)
        }

    @Test
    fun `switching to a guest drops the entries and a late result does not bring them back`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = LibraryViewModel(FakeLibraryRepository())

            viewModel.loadFor(USER_ID)
            runCurrent()
            viewModel.loadFor(null)
            advanceUntilIdle()

            assertEquals(LibraryUiState(), viewModel.uiState.value)
        }

    @Test
    fun `filtering finds matching games beyond the first page`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = LibraryViewModel(FakeLibraryRepository())
            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            viewModel.onFilterChange(LibraryStatus.PLATINUM)
            advanceUntilIdle()

            assertEquals(entries.filter { it.status == LibraryStatus.PLATINUM }, viewModel.uiState.value.entries)
            assertEquals(LibraryStatus.PLATINUM, viewModel.uiState.value.filter)
        }

    @Test
    fun `a filter change replaces the load in flight`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = LibraryViewModel(FakeLibraryRepository())

            viewModel.loadFor(USER_ID)
            runCurrent()
            viewModel.onFilterChange(LibraryStatus.PLAYING)
            advanceUntilIdle()

            assertEquals(entries.filter { it.status == LibraryStatus.PLAYING }, viewModel.uiState.value.entries)
        }

    @Test
    fun `loading more appends the next page until the last one`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = LibraryViewModel(FakeLibraryRepository())
            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            viewModel.loadMore()
            advanceUntilIdle()

            assertEquals(entries, viewModel.uiState.value.entries)
            assertFalse(viewModel.uiState.value.canLoadMore)
        }

    @Test
    fun `a load more failure keeps the entries and stops until retried`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeLibraryRepository()
            val viewModel = LibraryViewModel(repository)
            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            repository.failure = IllegalStateException()
            viewModel.loadMore()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.loadMoreFailed)
            assertFalse(viewModel.uiState.value.canLoadMore)
            assertEquals(entries.take(LibraryUiState.PAGE_SIZE), viewModel.uiState.value.entries)

            repository.failure = null
            viewModel.loadMore()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.loadMoreFailed)
            assertEquals(entries, viewModel.uiState.value.entries)
        }

    @Test
    fun `an empty library opened with a filter shows the empty library state`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeLibraryRepository(entries = emptyList(), stats = LibraryStats(totalGames = 0, platinum = 0))
            val viewModel = LibraryViewModel(repository)
            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            viewModel.onFilterChange(LibraryStatus.PLATINUM)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isLibraryEmpty)
        }

    private companion object {
        const val USER_ID = 7
    }
}
