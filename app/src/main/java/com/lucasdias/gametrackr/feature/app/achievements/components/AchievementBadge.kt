package com.lucasdias.gametrackr.feature.app.achievements.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.feature.app.achievements.Achievement

@Composable
fun AchievementBadge(
    achievement: Achievement,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
) {
    val unlocked = achievement.isUnlocked
    val tint = if (unlocked) achievement.tier.tint else AppTextSecondary
    val icon = if (achievement.isHidden) AppIcon.EYE_SLASH else achievement.tier.icon

    Box(
        modifier =
            modifier
                .size(size)
                .clip(CircleShape)
                .background(tint.copy(alpha = if (unlocked) 0.18f else 0.06f))
                .border(1.dp, tint.copy(alpha = if (unlocked) 0.55f else 0.2f), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon.image(filled = unlocked),
            contentDescription = null,
            tint = tint.copy(alpha = if (unlocked) 1f else 0.45f),
            modifier = Modifier.size(size * 0.46f),
        )
    }
}
