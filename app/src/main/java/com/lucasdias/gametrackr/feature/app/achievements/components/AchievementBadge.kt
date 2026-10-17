package com.lucasdias.gametrackr.feature.app.achievements.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.core.model.Achievement
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.achievements.AchievementsMockData

private val DEFAULT_SIZE = 44.dp
private val BORDER_WIDTH = 1.dp
private const val ICON_SCALE = 0.46f
private const val UNLOCKED_FILL_ALPHA = 0.18f
private const val LOCKED_FILL_ALPHA = 0.06f
private const val UNLOCKED_BORDER_ALPHA = 0.55f
private const val LOCKED_BORDER_ALPHA = 0.2f
private const val LOCKED_ICON_ALPHA = 0.45f

@Composable
fun AchievementBadge(
    achievement: Achievement,
    modifier: Modifier = Modifier,
    size: Dp = DEFAULT_SIZE,
) {
    val unlocked = achievement.isUnlocked
    val tint = if (unlocked) achievement.tier.tint else AppTextSecondary
    val icon = if (achievement.isHidden) AppIcon.EYE_SLASH else achievement.tier.icon
    val fillAlpha = if (unlocked) UNLOCKED_FILL_ALPHA else LOCKED_FILL_ALPHA
    val borderAlpha = if (unlocked) UNLOCKED_BORDER_ALPHA else LOCKED_BORDER_ALPHA
    val iconAlpha = if (unlocked) 1f else LOCKED_ICON_ALPHA

    Box(
        modifier =
            modifier
                .size(size)
                .clip(CircleShape)
                .background(tint.copy(alpha = fillAlpha))
                .border(BORDER_WIDTH, tint.copy(alpha = borderAlpha), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon.image(filled = unlocked),
            contentDescription = null,
            tint = tint.copy(alpha = iconAlpha),
            modifier = Modifier.size(size * ICON_SCALE),
        )
    }
}

@Preview
@Composable
private fun AchievementBadgePreview() {
    AppPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
            AchievementsMockData.games[3].achievements.forEach { AchievementBadge(achievement = it) }
        }
    }
}
