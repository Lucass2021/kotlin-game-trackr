package com.lucasdias.gametrackr.data.notifications

import com.lucasdias.gametrackr.core.model.NotificationFilter
import com.lucasdias.gametrackr.core.network.ApiError
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FakeNotificationsRepositoryTest {
    private val perPage = 4

    @Test
    fun `the unread filter returns only unread notifications`() =
        runTest {
            val page = FakeNotificationsRepository().fetchNotifications(NotificationFilter.UNREAD, 1, perPage).getOrThrow()

            assertEquals(listOf(1, 2, 3), page.items.map { it.id })
        }

    @Test
    fun `clearing removes the notifications that were not loaded yet`() =
        runTest {
            val repository = FakeNotificationsRepository()

            repository.clearNotifications()
            val page = repository.fetchNotifications(NotificationFilter.ALL, 2, perPage).getOrThrow()

            assertTrue(page.items.isEmpty())
            assertEquals(0, page.total)
        }

    @Test
    fun `answering an unknown friend request fails with not found`() =
        runTest {
            val result = FakeNotificationsRepository().respondToFriendRequest(id = 2, accept = true)

            assertEquals(ApiError.NotFound, result.exceptionOrNull())
        }
}
