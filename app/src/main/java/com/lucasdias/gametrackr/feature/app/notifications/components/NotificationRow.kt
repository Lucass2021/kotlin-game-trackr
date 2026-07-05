package com.lucasdias.gametrackr.feature.app.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.feature.app.notifications.AvatarBadge
import com.lucasdias.gametrackr.feature.app.notifications.NotificationItem
import com.lucasdias.gametrackr.feature.app.notifications.NotificationLeading
import com.lucasdias.gametrackr.feature.app.notifications.NotificationSection
import com.lucasdias.gametrackr.feature.app.notifications.NotificationSpan
import com.lucasdias.gametrackr.feature.app.notifications.NotificationSpanStyle

@Composable
fun NotificationRow(
    item: NotificationItem,
    markedAllRead: Boolean,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isUnread = item.unread && !markedAllRead
    val base =
        if (item.section == NotificationSection.NEW) {
            val fill = if (item.isFriendRequest) AppPrimary.copy(alpha = 0.06f) else AppSurfaceCard
            val border = if (item.isFriendRequest) AppPrimary.copy(alpha = 0.45f) else AppOutline
            Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(fill)
                .border(1.dp, border, RoundedCornerShape(16.dp))
        } else {
            Modifier
        }

    Row(
        modifier = modifier.fillMaxWidth().then(base).padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        LeadingVisual(item.leading)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = styledLine(item.spans),
                    fontSize = 15.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = item.timestamp,
                    color = AppTextSecondary,
                    fontSize = 12.sp,
                )
                if (isUnread && item.trailingDot) {
                    Box(
                        modifier =
                            Modifier
                                .padding(top = 4.dp)
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(AppPrimary),
                    )
                }
            }

            item.secondary?.let { Secondary(text = it, isQuote = item.secondaryIsQuote) }

            if (item.isFriendRequest) {
                FriendRequestActions(onAccept = onAccept, onDecline = onDecline)
            }
        }
    }
}

@Composable
private fun styledLine(spans: List<NotificationSpan>): AnnotatedString =
    buildAnnotatedString {
        spans.forEach { span ->
            val (color, weight) =
                when (span.style) {
                    NotificationSpanStyle.ACTOR -> AppTextPrimary to FontWeight.SemiBold
                    NotificationSpanStyle.TEXT -> AppTextSecondary to FontWeight.Normal
                    NotificationSpanStyle.LINK -> AppSecondary to FontWeight.SemiBold
                    NotificationSpanStyle.HIGHLIGHT -> AppPrimary to FontWeight.SemiBold
                }
            withStyle(SpanStyle(color = color, fontWeight = weight)) { append(span.text) }
        }
    }

@Composable
private fun Secondary(
    text: String,
    isQuote: Boolean,
) {
    if (isQuote) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(modifier = Modifier.width(2.dp).fillMaxHeight().background(AppOutline))
            Text(
                text = text,
                color = AppTextSecondary,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
            )
        }
    } else {
        Text(
            text = text,
            color = AppTextSecondary,
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun FriendRequestActions(
    onAccept: () -> Unit,
    onDecline: () -> Unit,
) {
    val acceptSource = remember { MutableInteractionSource() }
    val declineSource = remember { MutableInteractionSource() }
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.notifications_accept),
            color = AppOnPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier =
                Modifier
                    .pressScale(acceptSource)
                    .clip(CircleShape)
                    .clickable(interactionSource = acceptSource, indication = null, onClick = onAccept)
                    .background(AppPrimary)
                    .padding(horizontal = 22.dp, vertical = 9.dp),
        )
        Text(
            text = stringResource(R.string.notifications_decline),
            color = AppTertiary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier =
                Modifier
                    .pressScale(declineSource)
                    .clickable(interactionSource = declineSource, indication = null, onClick = onDecline)
                    .padding(vertical = 9.dp),
        )
    }
}

@Composable
private fun LeadingVisual(leading: NotificationLeading) {
    when (leading) {
        is NotificationLeading.Avatar -> {
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier =
                        Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(leading.start, leading.end)))
                            .border(1.dp, AppOutline, CircleShape),
                )
                AvatarBadgeView(leading.badge)
            }
        }

        is NotificationLeading.IconBox -> {
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(AppSurfaceCard)
                        .border(1.dp, AppOutline, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = leading.icon.image(),
                    contentDescription = null,
                    tint = leading.tint,
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        is NotificationLeading.Thumbnail -> {
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.linearGradient(listOf(leading.start, leading.end)))
                        .border(1.dp, AppOutline, RoundedCornerShape(12.dp)),
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
                        .size(13.dp)
                        .clip(CircleShape)
                        .background(AppBackground)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(AppPrimary),
            )
        }

        AvatarBadge.ACCEPTED -> {
            Box(
                modifier = Modifier.size(16.dp).clip(CircleShape).background(AppBackground),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = AppIcon.SUCCESS.image(filled = true),
                    contentDescription = null,
                    tint = AppPrimary,
                    modifier = Modifier.size(16.dp),
                )
            }
        }

        null -> {
            Unit
        }
    }
}
