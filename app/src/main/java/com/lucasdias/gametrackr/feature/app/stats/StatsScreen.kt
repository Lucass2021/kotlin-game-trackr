package com.lucasdias.gametrackr.feature.app.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.UserStats
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.shareText
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.stats.StatsMockData
import com.lucasdias.gametrackr.feature.app.stats.components.AchievementSpotlightCard
import com.lucasdias.gametrackr.feature.app.stats.components.StatBarList
import com.lucasdias.gametrackr.feature.app.stats.components.StatHighlightCard
import com.lucasdias.gametrackr.feature.app.stats.components.StatsSectionCard
import com.lucasdias.gametrackr.feature.app.stats.components.StatsTopBar
import com.lucasdias.gametrackr.feature.app.stats.components.StatusShareBar
import com.lucasdias.gametrackr.feature.app.stats.components.YearBarChart
import org.koin.androidx.compose.koinViewModel

@Composable
fun StatsScreen(
    onBack: () -> Unit,
    onAchievements: () -> Unit,
    modifier: Modifier = Modifier,
    ownerName: String? = null,
    viewModel: StatsViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) { viewModel.loadIfNeeded() }

    StatsScreen(
        state = state,
        ownerName = ownerName,
        onBack = onBack,
        onAchievements = onAchievements,
        onRetry = viewModel::load,
        modifier = modifier,
    )
}

@Composable
private fun StatsScreen(
    state: StatsUiState,
    ownerName: String?,
    onBack: () -> Unit,
    onAchievements: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val title = stringResource(if (ownerName == null) R.string.stats_title_own else R.string.stats_title)
    val shareMessage =
        if (ownerName == null) {
            stringResource(R.string.stats_share_message_own)
        } else {
            stringResource(R.string.stats_share_message, ownerName)
        }

    Column(modifier = modifier.fillMaxSize().background(AppBackground)) {
        StatsTopBar(
            title = title,
            subtitle = ownerName,
            onBack = onBack,
            onShare = { context.shareText(shareMessage) },
        )

        val stats = state.stats
        when {
            stats != null -> {
                StatsList(stats = stats, onAchievements = onAchievements)
            }

            state.hasError -> {
                EmptyState(
                    icon = AppIcon.INFO,
                    title = stringResource(R.string.stats_load_error_title),
                    message = stringResource(R.string.stats_load_error_message),
                    actionTitle = stringResource(R.string.stats_retry),
                    onAction = onRetry,
                    modifier = Modifier.weight(1f),
                )
            }

            else -> {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppPrimary)
                }
            }
        }
    }
}

@Composable
private fun StatsList(
    stats: UserStats,
    onAchievements: () -> Unit,
) {
    LazyColumn(
        contentPadding =
            PaddingValues(start = Spacing.lg, end = Spacing.lg, top = Spacing.sm, bottom = Spacing.xxl),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
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

@Preview
@Composable
private fun StatsScreenPreview() {
    AppPreview {
        StatsScreen(
            state = StatsUiState(stats = StatsMockData.stats),
            ownerName = null,
            onBack = {},
            onAchievements = {},
            onRetry = {},
        )
    }
}

@Preview
@Composable
private fun StatsScreenErrorPreview() {
    AppPreview {
        StatsScreen(
            state = StatsUiState(hasError = true),
            ownerName = "Ana",
            onBack = {},
            onAchievements = {},
            onRetry = {},
        )
    }
}
