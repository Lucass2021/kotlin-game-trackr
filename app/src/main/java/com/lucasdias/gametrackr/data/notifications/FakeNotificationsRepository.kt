package com.lucasdias.gametrackr.data.notifications

import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.core.pagination.fakePage
import com.lucasdias.gametrackr.data.FakeLatency
import com.lucasdias.gametrackr.feature.app.notifications.NotificationItem
import com.lucasdias.gametrackr.feature.app.notifications.NotificationsMockData
import kotlinx.coroutines.delay

class FakeNotificationsRepository : NotificationsRepository {
    override suspend fun fetchNotifications(
        page: Int,
        perPage: Int,
    ): Result<Page<NotificationItem>> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(fakePage(NotificationsMockData.items, page, perPage))
    }

    override suspend fun markAllAsRead(): Result<Unit> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(Unit)
    }

    override suspend fun clearNotifications(): Result<Unit> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(Unit)
    }
}
