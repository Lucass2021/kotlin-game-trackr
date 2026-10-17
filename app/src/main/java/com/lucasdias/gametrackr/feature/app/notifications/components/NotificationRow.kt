package com.lucasdias.gametrackr.feature.app.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.NotificationItem
import com.lucasdias.gametrackr.core.model.NotificationSection
import com.lucasdias.gametrackr.core.model.NotificationSpan
import com.lucasdias.gametrackr.core.model.NotificationSpanStyle
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.notifications.NotificationsMockData

private val BORDER_WIDTH = 1.dp
private val UNREAD_DOT_SIZE = 10.dp
private val QUOTE_BAR_WIDTH = 2.dp
private const val FRIEND_REQUEST_FILL_ALPHA = 0.06f
private const val FRIEND_REQUEST_BORDER_ALPHA = 0.45f
private val LINE_SIZE = 15.sp
private val LINE_HEIGHT = 20.sp
private val TIMESTAMP_SIZE = 12.sp
private val SECONDARY_SIZE = 14.sp

@Composable
fun NotificationRow(
    item: NotificationItem,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().notificationBackground(item).padding(Spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        NotificationLeadingVisual(item.leading)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                Text(
                    text = styledLine(item.spans),
                    fontSize = LINE_SIZE,
                    lineHeight = LINE_HEIGHT,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = item.timestamp,
                    color = AppTextSecondary,
                    fontSize = TIMESTAMP_SIZE,
                )
                if (item.unread && item.trailingDot) {
                    Box(
                        modifier =
                            Modifier
                                .padding(top = Spacing.xxs)
                                .size(UNREAD_DOT_SIZE)
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

private fun Modifier.notificationBackground(item: NotificationItem): Modifier {
    if (item.section != NotificationSection.NEW) return this
    val shape = RoundedCornerShape(Radius.lg)
    val fill = if (item.isFriendRequest) AppPrimary.copy(alpha = FRIEND_REQUEST_FILL_ALPHA) else AppSurfaceCard
    val border = if (item.isFriendRequest) AppPrimary.copy(alpha = FRIEND_REQUEST_BORDER_ALPHA) else AppOutline
    return clip(shape)
        .background(fill)
        .border(BORDER_WIDTH, border, shape)
}

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
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Box(modifier = Modifier.width(QUOTE_BAR_WIDTH).fillMaxHeight().background(AppOutline))
            Text(
                text = text,
                color = AppTextSecondary,
                fontSize = SECONDARY_SIZE,
                fontStyle = FontStyle.Italic,
            )
        }
    } else {
        Text(
            text = text,
            color = AppTextSecondary,
            fontSize = SECONDARY_SIZE,
        )
    }
}

@Preview
@Composable
private fun NotificationRowPreview() {
    AppPreview {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
            listOf(0, 2, 6).forEach { index ->
                NotificationRow(item = NotificationsMockData.items[index], onAccept = {}, onDecline = {})
            }
        }
    }
}
