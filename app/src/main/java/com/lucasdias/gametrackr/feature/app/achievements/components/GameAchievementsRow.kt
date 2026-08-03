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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
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
import com.lucasdias.gametrackr.feature.app.home.components.GameCoverArt

@Composable
fun GameAchievementsRow(
    game: GameAchievements,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        GameCoverArt(start = game.coverStart, end = game.coverEnd, width = 56.dp, height = 74.dp)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = game.title,
                        color = AppTextPrimary,
                        style = AppType.label(16.sp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = game.platform,
                        color = AppTextSecondary,
                        style = AppType.body(12.sp),
                    )
                }

                if (game.hasPlatinum) {
                    Icon(
                        imageVector = AppIcon.TROPHY.image(filled = true),
                        contentDescription = stringResource(R.string.achievements_platinum_earned),
                        tint = AppSecondary,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(AppOutline),
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(game.fraction)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(if (game.isComplete) AppPrimary else AppSecondary.copy(alpha = 0.8f)),
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.achievements_count_of, game.unlockedCount, game.total),
                    color = AppTextSecondary,
                    style = AppType.body(12.sp),
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = stringResource(R.string.achievements_summary_percent, game.percent),
                    color = if (game.isComplete) AppPrimary else AppTextSecondary,
                    style = AppType.label(12.sp),
                )
            }
        }
    }
}
