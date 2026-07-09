package com.lucasdias.gametrackr.feature.app.library.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.feature.app.library.LibraryStats

@Composable
fun LibraryStatsBar(
    stats: LibraryStats,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.library_stats_games, stats.totalGames),
            color = AppTextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Text(text = "•", color = AppTextSecondary, fontSize = 15.sp)
        Text(
            text = stringResource(R.string.library_stats_platinum, stats.platinum),
            color = AppTextSecondary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
