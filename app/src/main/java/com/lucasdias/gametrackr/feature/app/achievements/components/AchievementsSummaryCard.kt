package com.lucasdias.gametrackr.feature.app.achievements.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.achievements.GameAchievements
import com.lucasdias.gametrackr.feature.app.achievements.achievementTotal
import com.lucasdias.gametrackr.feature.app.achievements.completedCount
import com.lucasdias.gametrackr.feature.app.achievements.platinumCount
import com.lucasdias.gametrackr.feature.app.achievements.unlockedFraction
import com.lucasdias.gametrackr.feature.app.achievements.unlockedPercent
import com.lucasdias.gametrackr.feature.app.achievements.unlockedTotal

@Composable
fun AchievementsSummaryCard(
    games: List<GameAchievements>,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(18.dp)
    val a11y = stringResource(R.string.achievements_summary_a11y, games.unlockedTotal, games.achievementTotal)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .padding(20.dp)
                .semantics(mergeDescendants = true) { contentDescription = a11y },
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = games.unlockedTotal.toString(),
                color = AppPrimary,
                style = AppType.headline(34.sp, FontWeight.ExtraBold),
            )
            Text(
                text = stringResource(R.string.achievements_summary_of, games.achievementTotal),
                color = AppTextSecondary,
                style = AppType.body(15.sp),
                modifier = Modifier.weight(1f),
            )
            Text(
                text = stringResource(R.string.achievements_summary_percent, games.unlockedPercent),
                color = AppTextPrimary,
                style = AppType.headline(20.sp),
            )
        }

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape)
                    .background(AppOutline),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(games.unlockedFraction)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(Brush.horizontalGradient(listOf(AppPrimary, AppSecondary))),
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(22.dp)) {
            MiniStat(
                icon = AppIcon.TROPHY,
                value = games.platinumCount.toString(),
                label = stringResource(R.string.achievements_stat_platinums),
            )
            MiniStat(
                icon = AppIcon.SUCCESS,
                value = games.completedCount.toString(),
                label = stringResource(R.string.achievements_stat_completed),
            )
            MiniStat(
                icon = AppIcon.BRAND,
                value = games.size.toString(),
                label = stringResource(R.string.achievements_stat_games),
            )
        }
    }
}

@Composable
private fun MiniStat(
    icon: AppIcon,
    value: String,
    label: String,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon.image(),
            contentDescription = null,
            tint = AppSecondary,
            modifier = Modifier.size(16.dp),
        )
        Column {
            Text(text = value, color = AppTextPrimary, style = AppType.label(15.sp))
            Text(text = label, color = AppTextSecondary, style = AppType.body(11.sp))
        }
    }
}
