package com.lucasdias.gametrackr.feature.app.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType

private val CardWidth = 260.dp

@Composable
fun AnticipatedCard(
    game: Game,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(CardWidth),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box {
            GameCoverArt(
                start = game.coverStart,
                end = game.coverEnd,
                url = game.coverUrl,
                width = CardWidth,
                height = 150.dp,
            )
            Text(
                text = game.year,
                color = AppOnPrimary,
                style = AppType.label(13.sp),
                modifier =
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .background(
                            if (game.releaseDate == null) AppSecondary else AppPrimary,
                            RoundedCornerShape(8.dp),
                        ).padding(horizontal = 10.dp, vertical = 5.dp),
            )
        }
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
