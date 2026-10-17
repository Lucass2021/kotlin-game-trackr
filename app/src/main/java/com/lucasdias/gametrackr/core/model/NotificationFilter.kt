package com.lucasdias.gametrackr.core.model

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.R

enum class NotificationFilter(
    @StringRes val label: Int,
) {
    ALL(R.string.notifications_filter_all),
    UNREAD(R.string.notifications_filter_unread),
    MENTIONS(R.string.notifications_filter_mentions),
}
