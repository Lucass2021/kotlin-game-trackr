package com.lucasdias.gametrackr.core.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.CoverAzureEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverAzureStart
import com.lucasdias.gametrackr.core.ui.theme.CoverCrimsonEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverCrimsonStart
import com.lucasdias.gametrackr.core.ui.theme.CoverEmeraldEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverEmeraldStart
import com.lucasdias.gametrackr.core.ui.theme.CoverIndigoEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverIndigoStart
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletStart

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
        val start: Color,
        val end: Color,
        val badge: AvatarBadge? = null,
    ) : NotificationLeading

    data class IconBox(
        val icon: AppIcon,
        val tint: Color,
    ) : NotificationLeading

    data class Thumbnail(
        val start: Color,
        val end: Color,
    ) : NotificationLeading
}

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
