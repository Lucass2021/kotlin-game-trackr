package com.lucasdias.gametrackr.feature.app.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.achievements.components.AchievementRow
import com.lucasdias.gametrackr.feature.app.home.components.GameCoverArt
import com.lucasdias.gametrackr.feature.app.stats.components.StatsSectionCard
import com.lucasdias.gametrackr.feature.app.stats.components.StatsTopBar

@Composable
fun GameAchievementsScreen(
    game: GameAchievements,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val unlocked = game.achievements.filter { it.isUnlocked }
    val locked = game.achievements.filterNot { it.isUnlocked }

    Column(modifier = modifier.fillMaxSize().background(AppBackground)) {
        StatsTopBar(
            title = stringResource(R.string.achievements_title),
            subtitle = game.title,
            onBack = onBack,
        )

        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item { GameHeader(game) }

            if (unlocked.isNotEmpty()) {
                item {
                    StatsSectionCard(
                        title = stringResource(R.string.achievements_section_unlocked),
                        icon = AppIcon.SUCCESS,
                    ) {
                        AchievementList(unlocked)
                    }
                }
            }

            if (locked.isNotEmpty()) {
                item {
                    StatsSectionCard(
                        title = stringResource(R.string.achievements_section_locked),
                        icon = AppIcon.EYE_SLASH,
                    ) {
                        AchievementList(locked)
                    }
                }
            }
        }
    }
}

@Composable
private fun AchievementList(achievements: List<Achievement>) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        achievements.forEach { AchievementRow(achievement = it) }
    }
}

@Composable
private fun GameHeader(game: GameAchievements) {
    val shape = RoundedCornerShape(18.dp)

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        GameCoverArt(start = game.coverStart, end = game.coverEnd, width = 72.dp, height = 96.dp)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = game.title,
                color = AppTextPrimary,
                style = AppType.headline(20.sp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = game.platform,
                color = AppTextSecondary,
                style = AppType.body(13.sp),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.achievements_ratio, game.unlockedCount, game.total),
                    color = AppPrimary,
                    style = AppType.headline(18.sp),
                )
                Text(
                    text = stringResource(R.string.achievements_percent_suffix, game.percent),
                    color = AppTextSecondary,
                    style = AppType.body(13.sp),
                )
                if (game.hasPlatinum) {
                    Icon(
                        imageVector = AppIcon.TROPHY.image(filled = true),
                        contentDescription = stringResource(R.string.achievements_platinum_earned),
                        tint = AppSecondary,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}
