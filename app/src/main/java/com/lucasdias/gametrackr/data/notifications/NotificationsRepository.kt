package com.lucasdias.gametrackr.data.notifications

import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.feature.app.notifications.NotificationItem

interface NotificationsRepository {
    suspend fun fetchNotifications(
        page: Int,
        perPage: Int,
    ): Result<Page<NotificationItem>>

    suspend fun markAllAsRead(): Result<Unit>

    suspend fun clearNotifications(): Result<Unit>
}
