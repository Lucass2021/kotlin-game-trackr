package com.lucasdias.gametrackr.data.notifications

import com.lucasdias.gametrackr.core.model.AvatarBadge
import com.lucasdias.gametrackr.core.model.CoverPalette
import com.lucasdias.gametrackr.core.model.NotificationIconKind
import com.lucasdias.gametrackr.core.model.NotificationItem
import com.lucasdias.gametrackr.core.model.NotificationLeading
import com.lucasdias.gametrackr.core.model.NotificationSection
import com.lucasdias.gametrackr.core.model.NotificationSpan
import com.lucasdias.gametrackr.core.model.NotificationSpanStyle

object NotificationsMockData {
    val items =
        listOf(
            NotificationItem(
                id = 1,
                section = NotificationSection.NEW,
                leading = NotificationLeading.Avatar(CoverPalette.EMERALD, AvatarBadge.ONLINE),
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
                leading = NotificationLeading.Avatar(CoverPalette.AZURE, AvatarBadge.ONLINE),
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
                leading = NotificationLeading.IconBox(NotificationIconKind.LIKE),
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
                leading = NotificationLeading.Thumbnail(CoverPalette.INDIGO),
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
                leading = NotificationLeading.Avatar(CoverPalette.VIOLET),
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
                leading = NotificationLeading.IconBox(NotificationIconKind.ACHIEVEMENT),
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
                leading = NotificationLeading.Avatar(CoverPalette.CRIMSON, AvatarBadge.ACCEPTED),
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
                leading = NotificationLeading.IconBox(NotificationIconKind.MESSAGE),
                spans =
                    listOf(
                        NotificationSpan("@vortex_king", NotificationSpanStyle.ACTOR),
                        NotificationSpan(" sent you a message", NotificationSpanStyle.TEXT),
                    ),
                timestamp = "3d",
            ),
        )
}
