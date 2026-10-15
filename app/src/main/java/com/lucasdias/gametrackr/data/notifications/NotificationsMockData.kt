package com.lucasdias.gametrackr.data.notifications

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.AvatarBadge
import com.lucasdias.gametrackr.core.model.NotificationItem
import com.lucasdias.gametrackr.core.model.NotificationLeading
import com.lucasdias.gametrackr.core.model.NotificationSection
import com.lucasdias.gametrackr.core.model.NotificationSpan
import com.lucasdias.gametrackr.core.model.NotificationSpanStyle
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

object NotificationsMockData {
    val items =
        listOf(
            NotificationItem(
                id = 1,
                section = NotificationSection.NEW,
                leading = NotificationLeading.Avatar(CoverEmeraldStart, CoverEmeraldEnd, AvatarBadge.ONLINE),
                spans =
                    listOf(
                        NotificationSpan("@NeonShadow_X", NotificationSpanStyle.ACTOR),
                        NotificationSpan(" sent you a friend request", NotificationSpanStyle.TEXT),
                    ),
                timestamp = "2m",
                unread = true,
                isFriendRequest = true,
            ),
            NotificationItem(
                id = 2,
                section = NotificationSection.NEW,
                leading = NotificationLeading.Avatar(CoverAzureStart, CoverAzureEnd, AvatarBadge.ONLINE),
                spans =
                    listOf(
                        NotificationSpan("@QuestMaster", NotificationSpanStyle.ACTOR),
                        NotificationSpan(" replied to your post", NotificationSpanStyle.TEXT),
                    ),
                secondary = "Great points about the boss design…",
                secondaryIsQuote = true,
                timestamp = "1h",
                unread = true,
            ),
            NotificationItem(
                id = 3,
                section = NotificationSection.NEW,
                leading = NotificationLeading.IconBox(AppIcon.LIKE, AppPrimary),
                spans =
                    listOf(
                        NotificationSpan("@RetroGamer", NotificationSpanStyle.ACTOR),
                        NotificationSpan(" liked your review of ", NotificationSpanStyle.TEXT),
                        NotificationSpan("Elden Ring", NotificationSpanStyle.LINK),
                    ),
                timestamp = "4h",
                unread = true,
                trailingDot = true,
            ),
            NotificationItem(
                id = 4,
                section = NotificationSection.TODAY,
                leading = NotificationLeading.Thumbnail(CoverIndigoStart, CoverIndigoEnd),
                spans =
                    listOf(
                        NotificationSpan("New post in ", NotificationSpanStyle.TEXT),
                        NotificationSpan("Neon Arcade // Elite", NotificationSpanStyle.LINK),
                    ),
                secondary = "Tournament registration is now open…",
                timestamp = "6h",
            ),
            NotificationItem(
                id = 5,
                section = NotificationSection.TODAY,
                leading = NotificationLeading.Avatar(CoverVioletStart, CoverVioletEnd),
                spans =
                    listOf(
                        NotificationSpan("@cyber_queen", NotificationSpanStyle.ACTOR),
                        NotificationSpan(" mentioned you in a comment", NotificationSpanStyle.TEXT),
                    ),
                timestamp = "9h",
                isMention = true,
            ),
            NotificationItem(
                id = 6,
                section = NotificationSection.TODAY,
                leading = NotificationLeading.IconBox(AppIcon.MEDAL, AppPrimary),
                spans =
                    listOf(
                        NotificationSpan("You unlocked the ", NotificationSpanStyle.TEXT),
                        NotificationSpan("Platinum Hunter", NotificationSpanStyle.HIGHLIGHT),
                        NotificationSpan(" badge", NotificationSpanStyle.TEXT),
                    ),
                timestamp = "12h",
            ),
            NotificationItem(
                id = 7,
                section = NotificationSection.THIS_WEEK,
                leading = NotificationLeading.Avatar(CoverCrimsonStart, CoverCrimsonEnd, AvatarBadge.ACCEPTED),
                spans =
                    listOf(
                        NotificationSpan("@PixelWarrior", NotificationSpanStyle.ACTOR),
                        NotificationSpan(" accepted your friend request", NotificationSpanStyle.TEXT),
                    ),
                timestamp = "2d",
            ),
            NotificationItem(
                id = 8,
                section = NotificationSection.THIS_WEEK,
                leading = NotificationLeading.IconBox(AppIcon.ENVELOPE, AppTextSecondary),
                spans =
                    listOf(
                        NotificationSpan("@vortex_king", NotificationSpanStyle.ACTOR),
                        NotificationSpan(" sent you a message", NotificationSpanStyle.TEXT),
                    ),
                timestamp = "3d",
            ),
        )
}
