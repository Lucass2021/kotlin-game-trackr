package com.lucasdias.gametrackr.feature.app.achievements.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
private val COVER_WIDTH = 72.dp
private val COVER_HEIGHT = 96.dp
private val PLATINUM_ICON_SIZE = 16.dp
private val TITLE_SIZE = 20.sp
private val RATIO_SIZE = 18.sp
private val DETAIL_SIZE = 13.sp

@Composable
fun GameAchievementsHeader(
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
                .padding(Spacing.md),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        GameCoverArt(start = game.coverStart, end = game.coverEnd, width = COVER_WIDTH, height = COVER_HEIGHT)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Text(
                text = game.title,
                color = AppTextPrimary,
                style = AppType.headline(TITLE_SIZE),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = game.platform,
                color = AppTextSecondary,
                style = AppType.body(DETAIL_SIZE),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.achievements_ratio, game.unlockedCount, game.total),
                    color = AppPrimary,
                    style = AppType.headline(RATIO_SIZE),
                )
                Text(
                    text = stringResource(R.string.achievements_percent_suffix, game.percent),
                    color = AppTextSecondary,
                    style = AppType.body(DETAIL_SIZE),
                )
                if (game.hasPlatinum) {
                    Icon(
                        imageVector = AppIcon.TROPHY.image(filled = true),
                        contentDescription = stringResource(R.string.achievements_platinum_earned),
                        tint = AppSecondary,
                        modifier = Modifier.size(PLATINUM_ICON_SIZE),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GameAchievementsHeaderPreview() {
    AppPreview {
        GameAchievementsHeader(game = AchievementsMockData.games[0])
    }
}
