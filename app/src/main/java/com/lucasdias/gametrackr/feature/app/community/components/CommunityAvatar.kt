package com.lucasdias.gametrackr.feature.app.community.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletStart
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val AVATAR_SIZE = 40.dp
private val ICON_SIZE = 56.dp
private val BORDER_WIDTH = 1.dp
private const val AVATAR_ICON_ALPHA = 0.55f
private const val AVATAR_ICON_RATIO = 0.55f
private const val ICON_ALPHA = 0.65f
private const val ICON_RATIO = 0.42f

@Composable
fun CommunityAvatar(
    start: Color,
    end: Color,
    modifier: Modifier = Modifier,
    size: Dp = AVATAR_SIZE,
) {
    Box(
        modifier =
            modifier
                .size(size)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(start, end))),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = AppIcon.AVATAR.image(),
            contentDescription = null,
            tint = Color.White.copy(alpha = AVATAR_ICON_ALPHA),
            modifier = Modifier.size(size * AVATAR_ICON_RATIO),
        )
    }
}

@Composable
fun CommunityIcon(
    start: Color,
    end: Color,
    modifier: Modifier = Modifier,
    size: Dp = ICON_SIZE,
    cornerRadius: Dp = Radius.md,
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier =
            modifier
                .size(size)
                .clip(shape)
                .background(Brush.linearGradient(listOf(start, end)))
                .border(BORDER_WIDTH, AppOutline, shape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = AppIcon.COMMUNITY.image(),
            contentDescription = null,
            tint = Color.White.copy(alpha = ICON_ALPHA),
            modifier = Modifier.size(size * ICON_RATIO),
        )
    }
}

@Preview
@Composable
private fun CommunityAvatarPreview() {
    AppPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.padding(Spacing.md)) {
            CommunityAvatar(start = CoverVioletStart, end = CoverVioletEnd)
            CommunityIcon(start = CoverVioletStart, end = CoverVioletEnd)
        }
    }
}
