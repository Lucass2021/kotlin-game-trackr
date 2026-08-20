package com.lucasdias.gametrackr.feature.app.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType

private val CardWidth = 150.dp

@Composable
fun NewReleaseCard(
    game: Game,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(CardWidth),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        GameCoverArt(
            start = game.coverStart,
            end = game.coverEnd,
            url = game.coverUrl,
            width = CardWidth,
            height = 200.dp,
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = game.name,
                color = AppTextPrimary,
                style = AppType.label(15.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = game.platformsLabel,
                color = AppTextSecondary,
                style = AppType.body(13.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
