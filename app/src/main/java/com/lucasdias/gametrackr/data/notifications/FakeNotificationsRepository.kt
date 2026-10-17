package com.lucasdias.gametrackr.data.notifications

import com.lucasdias.gametrackr.core.model.NotificationFilter
import com.lucasdias.gametrackr.core.model.NotificationItem
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.core.pagination.fakePage
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeNotificationsRepository(
    items: List<NotificationItem> = NotificationsMockData.items,
    var failure: Throwable? = null,
) : NotificationsRepository {
    private val items = items.toMutableList()

    var fetchCount = 0
        private set

    val storedItems: List<NotificationItem>
        get() = items.toList()

    override suspend fun fetchNotifications(
        filter: NotificationFilter,
        page: Int,
        perPage: Int,
    ): Result<Page<NotificationItem>> {
        fetchCount++
        return respond { fakePage(items.filter { it.matches(filter) }, page, perPage) }
    }

    override suspend fun markAllAsRead(): Result<Unit> =
        respond {
            items.replaceAll { it.copy(unread = false) }
        }

    override suspend fun clearNotifications(): Result<Unit> = respond { items.clear() }

    override suspend fun respondToFriendRequest(
        id: Int,
        accept: Boolean,
    ): Result<Unit> =
        respond {
            val removed = items.removeIf { it.id == id && it.isFriendRequest }
            if (!removed) throw ApiError.NotFound
        }

    private fun NotificationItem.matches(filter: NotificationFilter): Boolean =
        when (filter) {
            NotificationFilter.ALL -> true
            NotificationFilter.UNREAD -> unread
            NotificationFilter.MENTIONS -> isMention
        }

    private suspend fun <T> respond(block: () -> T): Result<T> {
        val error = failure
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return error?.let { Result.failure(it) } ?: runCatching(block)
    }
}
