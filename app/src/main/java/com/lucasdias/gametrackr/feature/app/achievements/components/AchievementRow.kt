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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.achievements.Achievement

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
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        AchievementBadge(achievement = achievement)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    color = if (achievement.isUnlocked) AppTextPrimary else AppTextSecondary,
                    style = AppType.label(15.sp),
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = stringResource(achievement.tier.titleRes),
                    color = achievement.tier.tint.copy(alpha = if (achievement.isUnlocked) 1f else 0.5f),
                    style = AppType.body(11.sp),
                )
            }

            Text(text = detail, color = AppTextSecondary, style = AppType.body(13.sp))

            if (achievement.unlockedAt != null) {
                Row(
                    modifier = Modifier.padding(top = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = AppIcon.CHECK.image(),
                        contentDescription = null,
                        tint = AppPrimary,
                        modifier = Modifier.size(12.dp),
                    )
                    Text(
                        text = achievement.unlockedAt,
                        color = AppPrimary,
                        style = AppType.body(12.sp),
                    )
                }
            }
        }
    }
}
