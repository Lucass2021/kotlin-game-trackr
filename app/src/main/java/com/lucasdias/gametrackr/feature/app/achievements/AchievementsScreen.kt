package com.lucasdias.gametrackr.feature.app.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.AchievementFilter
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.achievements.AchievementsMockData
import com.lucasdias.gametrackr.feature.app.achievements.components.AchievementFilterChips
import com.lucasdias.gametrackr.feature.app.achievements.components.AchievementsList
import com.lucasdias.gametrackr.feature.app.stats.components.StatsTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun AchievementsScreen(
    onBack: () -> Unit,
    onGameClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    ownerName: String? = null,
    viewModel: AchievementsViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) { viewModel.loadIfNeeded() }

    AchievementsScreen(
        state = state,
        ownerName = ownerName,
        onBack = onBack,
        onFilterChange = viewModel::onFilterChange,
        onRetry = viewModel::load,
        onLoadMore = viewModel::loadMore,
        onGameClick = onGameClick,
        modifier = modifier,
    )
}

@Composable
private fun AchievementsScreen(
    state: AchievementsUiState,
    ownerName: String?,
    onBack: () -> Unit,
    onFilterChange: (AchievementFilter) -> Unit,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    onGameClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(AppBackground)) {
        StatsTopBar(
            title = stringResource(R.string.achievements_title),
            subtitle = ownerName,
            onBack = onBack,
        )

        AchievementFilterChips(
            selection = state.filter,
            onSelect = onFilterChange,
            modifier = Modifier.padding(bottom = Spacing.xxs),
        )

        when {
            state.hasError -> {
                EmptyState(
                    icon = AppIcon.INFO,
                    title = stringResource(R.string.achievements_load_error_title),
                    message = stringResource(R.string.achievements_load_error_message),
                    actionTitle = stringResource(R.string.achievements_retry),
                    onAction = onRetry,
                    modifier = Modifier.weight(1f),
                )
            }

            state.currentPage == 0 -> {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppPrimary)
                }
            }

            state.games.isEmpty() -> {
                EmptyState(
                    icon = AppIcon.TROPHY,
                    title = stringResource(R.string.achievements_empty_title),
                    message = stringResource(R.string.achievements_empty_message),
                    modifier = Modifier.weight(1f),
                )
            }

            else -> {
                AchievementsList(
                    games = state.games,
                    summary = state.summary.takeIf { state.showsSummary },
                    canLoadMore = state.canLoadMore,
                    isLoadingMore = state.isLoadingMore,
                    loadMoreFailed = state.loadMoreFailed,
                    onLoadMore = onLoadMore,
                    onGameClick = onGameClick,
                )
            }
        }
    }
}

@Preview
@Composable
private fun AchievementsScreenPreview() {
    AppPreview {
        AchievementsScreen(
            state = AchievementsUiState(games = AchievementsMockData.games, currentPage = 1),
            ownerName = null,
            onBack = {},
            onFilterChange = {},
            onRetry = {},
            onLoadMore = {},
            onGameClick = {},
        )
    }
}

@Preview
@Composable
private fun AchievementsScreenErrorPreview() {
    AppPreview {
        AchievementsScreen(
            state = AchievementsUiState(hasError = true),
            ownerName = "Ana",
            onBack = {},
            onFilterChange = {},
            onRetry = {},
            onLoadMore = {},
            onGameClick = {},
        )
    }
}
