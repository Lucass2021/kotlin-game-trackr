package com.lucasdias.gametrackr.feature.app.achievements.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Achievement
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.achievements.AchievementsMockData

private val CHECK_ICON_SIZE = 12.dp
private const val LOCKED_TIER_ALPHA = 0.5f
private val TITLE_SIZE = 15.sp
private val TIER_SIZE = 11.sp
private val DETAIL_SIZE = 13.sp
private val UNLOCKED_AT_SIZE = 12.sp

@Composable
fun AchievementRow(
    achievement: Achievement,
    modifier: Modifier = Modifier,
) {
    val title =
        if (achievement.isHidden) {
            stringResource(R.string.achievements_hidden_title)
        } else {
            achievement.title
        }
    val detail =
        if (achievement.isHidden) {
            stringResource(R.string.achievements_hidden_detail)
        } else {
            achievement.detail
        }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        AchievementBadge(achievement = achievement)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    color = if (achievement.isUnlocked) AppTextPrimary else AppTextSecondary,
                    style = AppType.label(TITLE_SIZE),
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = stringResource(achievement.tier.titleRes),
                    color = achievement.tier.tint.copy(alpha = if (achievement.isUnlocked) 1f else LOCKED_TIER_ALPHA),
                    style = AppType.body(TIER_SIZE),
                )
            }

            Text(text = detail, color = AppTextSecondary, style = AppType.body(DETAIL_SIZE))

            if (achievement.unlockedAt != null) {
                Row(
                    modifier = Modifier.padding(top = Spacing.xxxs),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = AppIcon.CHECK.image(),
                        contentDescription = null,
                        tint = AppPrimary,
                        modifier = Modifier.size(CHECK_ICON_SIZE),
                    )
                    Text(
                        text = achievement.unlockedAt,
                        color = AppPrimary,
                        style = AppType.body(UNLOCKED_AT_SIZE),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AchievementRowPreview() {
    AppPreview {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
            AchievementsMockData.games[3].achievements.forEach { AchievementRow(achievement = it) }
        }
    }
}
