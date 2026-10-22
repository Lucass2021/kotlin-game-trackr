package com.lucasdias.gametrackr.feature.app.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.game.HomeMockData

private val COVER_HEIGHT = 200.dp
private val NAME_SIZE = 15.sp
private val DETAIL_SIZE = 13.sp
private val CARD_WIDTH = 150.dp

@Composable
fun NewReleaseCard(
    game: Game,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(CARD_WIDTH),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        GameCoverArt(
            start = game.coverStart,
            end = game.coverEnd,
            url = game.coverUrl,
            width = CARD_WIDTH,
            height = COVER_HEIGHT,
        )
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxxs)) {
            Text(
                text = game.name,
                color = AppTextPrimary,
                style = AppType.label(NAME_SIZE),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = game.platformsLabel,
                color = AppTextSecondary,
                style = AppType.body(DETAIL_SIZE),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
private fun NewReleaseCardPreview() {
    AppPreview {
        NewReleaseCard(game = HomeMockData.sampleGame, modifier = Modifier.padding(Spacing.lg))
    }
}
