package com.lucasdias.gametrackr.feature.app.notifications

import com.lucasdias.gametrackr.core.model.NotificationFilter
import com.lucasdias.gametrackr.core.model.NotificationSection
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.notifications.FakeNotificationsRepository
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class NotificationsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val firstPageIds = listOf(1, 2, 3, 4)
    private val mentionIds = listOf(5)

    @Test
    fun `load shows the first page grouped by section`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = NotificationsViewModel(FakeNotificationsRepository())

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(firstPageIds, state.items.map { it.id })
            assertEquals(listOf(NotificationSection.NEW, NotificationSection.TODAY), state.sections.map { it.section })
            assertEquals(
                listOf(1, 2, 3),
                state.sections
                    .first()
                    .items
                    .map { it.id },
            )
            assertTrue(state.canLoadMore)
        }

    @Test
    fun `load failure shows the error state instead of loading forever`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = NotificationsViewModel(FakeNotificationsRepository(failure = IllegalStateException()))

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.hasError)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `retry after a failure shows the notifications`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeNotificationsRepository(failure = IllegalStateException())
            val viewModel = NotificationsViewModel(repository)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.failure = null
            viewModel.load()
            advanceUntilIdle()

            assertEquals(
                firstPageIds,
                viewModel.uiState.value.items
                    .map { it.id },
            )
            assertFalse(viewModel.uiState.value.hasError)
        }

    @Test
    fun `returning to the screen keeps the notifications without fetching again`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeNotificationsRepository()
            val viewModel = NotificationsViewModel(repository)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(1, repository.fetchCount)
            assertEquals(
                firstPageIds,
                viewModel.uiState.value.items
                    .map { it.id },
            )
        }

    @Test
    fun `the mentions filter finds mentions beyond the first page`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = NotificationsViewModel(FakeNotificationsRepository())
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.onFilterChange(NotificationFilter.MENTIONS)
            advanceUntilIdle()

            assertEquals(
                mentionIds,
                viewModel.uiState.value.items
                    .map { it.id },
            )
        }

    @Test
    fun `a filter change replaces the load in flight`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = NotificationsViewModel(FakeNotificationsRepository())

            viewModel.loadIfNeeded()
            runCurrent()
            viewModel.onFilterChange(NotificationFilter.MENTIONS)
            advanceUntilIdle()

            assertEquals(
                mentionIds,
                viewModel.uiState.value.items
                    .map { it.id },
            )
        }

    @Test
    fun `a load more failure keeps the notifications and stops until retried`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeNotificationsRepository()
            val viewModel = NotificationsViewModel(repository)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.failure = IllegalStateException()
            viewModel.loadMore()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.loadMoreFailed)
            assertFalse(viewModel.uiState.value.canLoadMore)
            assertEquals(
                firstPageIds,
                viewModel.uiState.value.items
                    .map { it.id },
            )

            repository.failure = null
            viewModel.loadMore()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.loadMoreFailed)
            assertEquals(
                (1..8).toList(),
                viewModel.uiState.value.items
                    .map { it.id },
            )
        }
}
