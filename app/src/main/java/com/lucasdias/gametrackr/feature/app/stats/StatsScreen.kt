package com.lucasdias.gametrackr.feature.app.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.feature.app.stats.components.AchievementSpotlightCard
import com.lucasdias.gametrackr.feature.app.stats.components.StatBarList
import com.lucasdias.gametrackr.feature.app.stats.components.StatHighlightCard
import com.lucasdias.gametrackr.feature.app.stats.components.StatsSectionCard
import com.lucasdias.gametrackr.feature.app.stats.components.StatsTopBar
import com.lucasdias.gametrackr.feature.app.stats.components.StatusShareBar
import com.lucasdias.gametrackr.feature.app.stats.components.YearBarChart

@Composable
fun StatsScreen(
    onBack: () -> Unit,
    onAchievements: () -> Unit,
    modifier: Modifier = Modifier,
    stats: UserStats = StatsMockData.stats,
    ownerName: String? = null,
) {
    val title =
        if (ownerName == null) {
            stringResource(R.string.stats_title_own)
        } else {
            stringResource(R.string.stats_title_other, ownerName)
        }

    Column(modifier = modifier.fillMaxSize().background(AppBackground)) {
        StatsTopBar(title = title, onBack = onBack, onShare = {})

        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            items(stats.highlights, key = { it.labelRes }) { highlight ->
                StatHighlightCard(highlight = highlight)
            }

            item {
                StatsSectionCard(title = stringResource(R.string.stats_section_status), icon = AppIcon.CHART) {
                    StatusShareBar(shares = stats.statusShare)
                }
            }

            item {
                StatsSectionCard(title = stringResource(R.string.stats_section_genres), icon = AppIcon.BRAND) {
                    StatBarList(bars = stats.genres)
                }
            }

            item {
                StatsSectionCard(title = stringResource(R.string.stats_section_platforms), icon = AppIcon.DEVICES) {
                    StatBarList(bars = stats.platforms)
                }
            }

            item {
                StatsSectionCard(title = stringResource(R.string.stats_section_years), icon = AppIcon.CALENDAR) {
                    YearBarChart(years = stats.yearsCompleted)
                }
            }

            item {
                AchievementSpotlightCard(spotlight = stats.spotlight, onAction = onAchievements)
            }
        }
    }
}
