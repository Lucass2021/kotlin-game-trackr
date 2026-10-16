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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.LibraryStats
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.library.LibraryMockData

private val TEXT_SIZE = 15.sp

@Composable
fun LibraryStatsBar(
    stats: LibraryStats,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        Text(
            text = stringResource(R.string.library_stats_games, stats.totalGames),
            color = AppTextPrimary,
            fontSize = TEXT_SIZE,
            fontWeight = FontWeight.SemiBold,
        )
        Text(text = "•", color = AppTextSecondary, fontSize = TEXT_SIZE)
        Text(
            text = stringResource(R.string.library_stats_platinum, stats.platinum),
            color = AppTextSecondary,
            fontSize = TEXT_SIZE,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview
@Composable
private fun LibraryStatsBarPreview() {
    AppPreview {
        LibraryStatsBar(stats = LibraryMockData.stats)
    }
}
