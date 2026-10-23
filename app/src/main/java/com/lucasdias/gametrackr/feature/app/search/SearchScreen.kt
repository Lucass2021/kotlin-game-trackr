package com.lucasdias.gametrackr.feature.app.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.game.HomeMockData
import com.lucasdias.gametrackr.feature.app.search.components.SearchFilterChips
import com.lucasdias.gametrackr.feature.app.search.components.SearchResultsEmptyState
import com.lucasdias.gametrackr.feature.app.search.components.SearchResultsGrid
import com.lucasdias.gametrackr.feature.app.search.components.SearchTopBar
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onGameClick: (String) -> Unit,
    scope: SearchScope = SearchScope.ALL,
    viewModel: SearchViewModel = koinViewModel { parametersOf(scope) },
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) { viewModel.loadIfNeeded() }

    SearchScreen(
        state = state,
        onBack = onBack,
        onQueryChange = viewModel::onQueryChange,
        onPlatformChange = viewModel::onPlatformChange,
        onClearFilters = viewModel::onClearFilters,
        onRetry = viewModel::load,
        onLoadMore = viewModel::loadMore,
        onGameClick = onGameClick,
    )
}

@Composable
private fun SearchScreen(
    state: SearchUiState,
    onBack: () -> Unit = {},
    onQueryChange: (String) -> Unit = {},
    onPlatformChange: (String?) -> Unit = {},
    onClearFilters: () -> Unit = {},
    onRetry: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    onGameClick: (String) -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
        SearchTopBar(query = state.query, onQueryChange = onQueryChange, onBack = onBack)

        SearchFilterChips(
            platforms = state.platforms,
            selection = state.platforms.firstOrNull { it.slug == state.platformSlug },
            onSelect = { onPlatformChange(it?.slug) },
            modifier = Modifier.padding(top = Spacing.xxxs, bottom = Spacing.sm),
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.hasError -> {
                    EmptyState(
                        icon = AppIcon.INFO,
                        title = stringResource(R.string.search_error_title),
                        message = stringResource(R.string.search_error_message),
                        actionTitle = stringResource(R.string.search_retry),
                        onAction = onRetry,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                state.isWaiting -> {
                    CircularProgressIndicator(color = AppPrimary, modifier = Modifier.align(Alignment.Center))
                }

                state.isEmpty -> {
                    SearchResultsEmptyState(query = state.query, onClear = onClearFilters, modifier = Modifier.fillMaxSize())
                }

                else -> {
                    SearchResultsGrid(state = state, onLoadMore = onLoadMore, onGameClick = onGameClick)
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    AppPreview {
        SearchScreen(
            state =
                SearchUiState(
                    platforms = HomeMockData.platforms,
                    games = listOf(HomeMockData.sampleGame),
                    currentPage = 1,
                    total = 1,
                ),
        )
    }
}
