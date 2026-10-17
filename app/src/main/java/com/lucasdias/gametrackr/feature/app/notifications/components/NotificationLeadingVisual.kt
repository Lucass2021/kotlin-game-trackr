package com.lucasdias.gametrackr.feature.app.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.core.model.AvatarBadge
import com.lucasdias.gametrackr.core.model.NotificationLeading
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.notifications.NotificationsMockData

private val VISUAL_SIZE = 48.dp
private val ICON_SIZE = 24.dp
private val BORDER_WIDTH = 1.dp
private val ONLINE_BADGE_SIZE = 13.dp
private val ACCEPTED_BADGE_SIZE = 16.dp

@Composable
fun NotificationLeadingVisual(
    leading: NotificationLeading,
    modifier: Modifier = Modifier,
) {
    val boxShape = RoundedCornerShape(Radius.md)

    when (leading) {
        is NotificationLeading.Avatar -> {
            Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier =
                        Modifier
                            .size(VISUAL_SIZE)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(leading.start, leading.end)))
                            .border(BORDER_WIDTH, AppOutline, CircleShape),
                )
                AvatarBadgeView(leading.badge)
            }
        }

        is NotificationLeading.IconBox -> {
            Box(
                modifier =
                    modifier
                        .size(VISUAL_SIZE)
                        .clip(boxShape)
                        .background(AppSurfaceCard)
                        .border(BORDER_WIDTH, AppOutline, boxShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = leading.icon.image(),
                    contentDescription = null,
                    tint = leading.tint,
                    modifier = Modifier.size(ICON_SIZE),
                )
            }
        }

        is NotificationLeading.Thumbnail -> {
            Box(
                modifier =
                    modifier
                        .size(VISUAL_SIZE)
                        .clip(boxShape)
                        .background(Brush.linearGradient(listOf(leading.start, leading.end)))
                        .border(BORDER_WIDTH, AppOutline, boxShape),
            )
        }
    }
}

@Composable
private fun AvatarBadgeView(badge: AvatarBadge?) {
    when (badge) {
        AvatarBadge.ONLINE -> {
            Box(
                modifier =
                    Modifier
                        .size(ONLINE_BADGE_SIZE)
                        .clip(CircleShape)
                        .background(AppBackground)
                        .padding(Spacing.xxxs)
                        .clip(CircleShape)
                        .background(AppPrimary),
            )
        }

        AvatarBadge.ACCEPTED -> {
            Box(
                modifier = Modifier.size(ACCEPTED_BADGE_SIZE).clip(CircleShape).background(AppBackground),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = AppIcon.SUCCESS.image(filled = true),
                    contentDescription = null,
                    tint = AppPrimary,
                    modifier = Modifier.size(ACCEPTED_BADGE_SIZE),
                )
            }
        }

        null -> {
            Unit
        }
    }
}

@Preview
@Composable
private fun NotificationLeadingVisualPreview() {
    AppPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
            NotificationsMockData.items.forEach { NotificationLeadingVisual(leading = it.leading) }
        }
    }
}
