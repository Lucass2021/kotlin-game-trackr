package com.lucasdias.gametrackr.feature.app.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.feature.app.home.components.GameCoverArt
import com.lucasdias.gametrackr.feature.app.search.SearchGame

@Composable
fun SearchResultCard(
    game: SearchGame,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        GameCoverArt(
            start = game.coverStart,
            end = game.coverEnd,
            modifier = Modifier.fillMaxWidth().aspectRatio(0.72f),
        )
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = game.title,
                color = AppTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${game.year} • ${game.platformsLabel}",
                color = AppTextSecondary,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
