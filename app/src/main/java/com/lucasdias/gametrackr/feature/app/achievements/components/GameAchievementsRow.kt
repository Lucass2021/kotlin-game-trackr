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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.GameAchievements
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.achievements.AchievementsMockData
import com.lucasdias.gametrackr.feature.app.home.components.GameCoverArt

private val BORDER_WIDTH = 1.dp
private val COVER_WIDTH = 56.dp
private val COVER_HEIGHT = 74.dp
private val PLATINUM_ICON_SIZE = 18.dp
private val BAR_HEIGHT = 6.dp
private const val IN_PROGRESS_ALPHA = 0.8f
private val TITLE_SIZE = 16.sp
private val DETAIL_SIZE = 12.sp

@Composable
fun GameAchievementsRow(
    game: GameAchievements,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(Radius.lg)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppOutline, shape)
                .padding(Spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        GameCoverArt(start = game.coverStart, end = game.coverEnd, width = COVER_WIDTH, height = COVER_HEIGHT)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.xxxs)) {
                    Text(
                        text = game.title,
                        color = AppTextPrimary,
                        style = AppType.label(TITLE_SIZE),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = game.platform,
                        color = AppTextSecondary,
                        style = AppType.body(DETAIL_SIZE),
                    )
                }

                if (game.hasPlatinum) {
                    Icon(
                        imageVector = AppIcon.TROPHY.image(filled = true),
                        contentDescription = stringResource(R.string.achievements_platinum_earned),
                        tint = AppSecondary,
                        modifier = Modifier.size(PLATINUM_ICON_SIZE),
                    )
                }
            }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(BAR_HEIGHT)
                        .clip(CircleShape)
                        .background(AppOutline),
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(game.fraction)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(if (game.isComplete) AppPrimary else AppSecondary.copy(alpha = IN_PROGRESS_ALPHA)),
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.achievements_count_of, game.unlockedCount, game.total),
                    color = AppTextSecondary,
                    style = AppType.body(DETAIL_SIZE),
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = stringResource(R.string.achievements_summary_percent, game.percent),
                    color = if (game.isComplete) AppPrimary else AppTextSecondary,
                    style = AppType.label(DETAIL_SIZE),
                )
            }
        }
    }
}

@Preview
@Composable
private fun GameAchievementsRowPreview() {
    AppPreview {
        GameAchievementsRow(game = AchievementsMockData.games[0])
    }
}
