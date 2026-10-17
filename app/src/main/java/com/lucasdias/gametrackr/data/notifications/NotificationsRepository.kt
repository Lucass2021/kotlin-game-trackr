package com.lucasdias.gametrackr.data.notifications

import com.lucasdias.gametrackr.core.model.NotificationFilter
import com.lucasdias.gametrackr.core.model.NotificationItem
import com.lucasdias.gametrackr.core.pagination.Page

interface NotificationsRepository {
    suspend fun fetchNotifications(
        filter: NotificationFilter,
        page: Int,
        perPage: Int,
    ): Result<Page<NotificationItem>>

    suspend fun markAllAsRead(): Result<Unit>

    suspend fun clearNotifications(): Result<Unit>

    suspend fun respondToFriendRequest(
        id: Int,
        accept: Boolean,
    ): Result<Unit>
}
