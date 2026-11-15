package com.lucasdias.gametrackr.core.model

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.R

enum class NotificationSection(
    @StringRes val title: Int,
) {
    NEW(R.string.notifications_section_new),
    TODAY(R.string.notifications_section_today),
    THIS_WEEK(R.string.notifications_section_this_week),
}

enum class NotificationSpanStyle { ACTOR, TEXT, LINK, HIGHLIGHT }

data class NotificationSpan(
    val text: String,
    val style: NotificationSpanStyle,
)

enum class AvatarBadge { ONLINE, ACCEPTED }

sealed interface NotificationLeading {
    data class Avatar(
        val cover: CoverPalette,
        val badge: AvatarBadge? = null,
    ) : NotificationLeading

    data class IconBox(
        val kind: NotificationIconKind,
    ) : NotificationLeading

    data class Thumbnail(
        val cover: CoverPalette,
    ) : NotificationLeading
}

enum class NotificationIconKind { LIKE, ACHIEVEMENT, MESSAGE }

data class NotificationItem(
    val id: Int,
    val section: NotificationSection,
    val leading: NotificationLeading,
    val spans: List<NotificationSpan>,
    val secondary: String? = null,
    val secondaryIsQuote: Boolean = false,
    val timestamp: String,
    val unread: Boolean = false,
    val trailingDot: Boolean = false,
    val isMention: Boolean = false,
    val isFriendRequest: Boolean = false,
)
