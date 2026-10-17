package com.lucasdias.gametrackr.feature.app.notifications

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.NotificationFilter
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.notifications.FakeNotificationsRepository
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class NotificationsViewModelActionsTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val friendRequestId = 1

    private val repository = FakeNotificationsRepository()
    private val viewModel = NotificationsViewModel(repository)

    private fun TestScope.loadFirstPage(filter: NotificationFilter = NotificationFilter.ALL) {
        viewModel.loadIfNeeded()
        advanceUntilIdle()
        viewModel.onFilterChange(filter)
        advanceUntilIdle()
    }

    @Test
    fun `mark all read clears the unread state`() =
        runTest(mainDispatcherRule.dispatcher) {
            loadFirstPage()

            viewModel.onMarkAllRead()
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.items
                    .isNotEmpty(),
            )
            assertTrue(
                viewModel.uiState.value.items
                    .none { it.unread },
            )
            assertTrue(repository.storedItems.none { it.unread })
        }

    @Test
    fun `mark all read with the unread filter leaves nothing to show`() =
        runTest(mainDispatcherRule.dispatcher) {
            loadFirstPage(NotificationFilter.UNREAD)

            viewModel.onMarkAllRead()
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.items
                    .isEmpty(),
            )
            assertFalse(viewModel.uiState.value.canLoadMore)
        }

    @Test
    fun `a mark all read failure keeps the notifications and shows an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            loadFirstPage()

            repository.failure = IllegalStateException()
            viewModel.onMarkAllRead()
            advanceUntilIdle()

            assertEquals(R.string.notifications_mark_all_read_error, viewModel.uiState.value.errorMessage)
            assertTrue(
                viewModel.uiState.value.items
                    .any { it.unread },
            )

            viewModel.onErrorShown()

            assertNull(viewModel.uiState.value.errorMessage)
        }

    @Test
    fun `clear all removes every notification, including the ones not loaded yet`() =
        runTest(mainDispatcherRule.dispatcher) {
            loadFirstPage()

            viewModel.onClearAll()
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.items
                    .isEmpty(),
            )
            assertFalse(viewModel.uiState.value.canLoadMore)
            assertTrue(repository.storedItems.isEmpty())
        }

    @Test
    fun `a clear all failure keeps the notifications and shows an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            loadFirstPage()

            repository.failure = IllegalStateException()
            viewModel.onClearAll()
            advanceUntilIdle()

            assertEquals(R.string.notifications_clear_error, viewModel.uiState.value.errorMessage)
            assertEquals(4, viewModel.uiState.value.items.size)
        }

    @Test
    fun `accepting a friend request removes it`() =
        runTest(mainDispatcherRule.dispatcher) {
            loadFirstPage()

            viewModel.onAcceptFriendRequest(friendRequestId)
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.items
                    .none { it.id == friendRequestId },
            )
            assertTrue(repository.storedItems.none { it.id == friendRequestId })
        }

    @Test
    fun `a decline failure keeps the friend request and shows an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            loadFirstPage()

            repository.failure = IllegalStateException()
            viewModel.onDeclineFriendRequest(friendRequestId)
            advanceUntilIdle()

            assertEquals(R.string.notifications_friend_request_error, viewModel.uiState.value.errorMessage)
            assertTrue(
                viewModel.uiState.value.items
                    .any { it.id == friendRequestId },
            )
        }

    @Test
    fun `answering a friend request does not skip the next page`() =
        runTest(mainDispatcherRule.dispatcher) {
            loadFirstPage()

            viewModel.onAcceptFriendRequest(friendRequestId)
            advanceUntilIdle()
            viewModel.loadMore()
            advanceUntilIdle()

            assertEquals(
                (2..8).toList(),
                viewModel.uiState.value.items
                    .map { it.id },
            )
        }

    @Test
    fun `a double tap on accept answers once without an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            loadFirstPage()

            viewModel.onAcceptFriendRequest(friendRequestId)
            viewModel.onAcceptFriendRequest(friendRequestId)
            advanceUntilIdle()

            assertNull(viewModel.uiState.value.errorMessage)
            assertTrue(
                viewModel.uiState.value.items
                    .none { it.id == friendRequestId },
            )
        }
}
