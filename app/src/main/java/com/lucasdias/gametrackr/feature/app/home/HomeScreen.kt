package com.lucasdias.gametrackr.feature.app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.game.HomeMockData
import com.lucasdias.gametrackr.feature.app.home.components.AnticipatedCard
import com.lucasdias.gametrackr.feature.app.home.components.HomeFeedSection
import com.lucasdias.gametrackr.feature.app.home.components.NewReleaseCard
import com.lucasdias.gametrackr.feature.app.search.SearchScope
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onViewAll: (SearchScope) -> Unit = {},
    onGameClick: (String) -> Unit = {},
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) { viewModel.loadIfNeeded() }

    HomeScreen(
        state = state,
        modifier = modifier,
        onRefresh = viewModel::refresh,
        onRetry = viewModel::onRetry,
        onViewAll = onViewAll,
        onGameClick = { onGameClick(it.slug) },
        onToastShown = viewModel::onToastShown,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    state: HomeUiState,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    onRetry: (HomeSection) -> Unit = {},
    onViewAll: (SearchScope) -> Unit = {},
    onGameClick: (Game) -> Unit = {},
    onToastShown: () -> Unit = {},
) {
    Box(modifier = modifier.fillMaxSize().background(AppBackground)) {
        PullToRefreshBox(isRefreshing = state.isRefreshing, onRefresh = onRefresh) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(top = Spacing.sm, bottom = Spacing.xl),
                verticalArrangement = Arrangement.spacedBy(Spacing.xl),
            ) {
                HomeFeedSection(
                    title = stringResource(R.string.home_new_releases),
                    feed = state.newReleases,
                    onViewAll = { onViewAll(SearchScope.NEW_RELEASES) },
                    onRetry = { onRetry(HomeSection.NEW_RELEASES) },
                    onGameClick = onGameClick,
                ) { game -> NewReleaseCard(game = game) }

                HomeFeedSection(
                    title = stringResource(R.string.home_most_anticipated),
                    feed = state.mostAnticipated,
                    onViewAll = { onViewAll(SearchScope.MOST_ANTICIPATED) },
                    onRetry = { onRetry(HomeSection.MOST_ANTICIPATED) },
                    onGameClick = onGameClick,
                ) { game -> AnticipatedCard(game = game) }
            }
        }

        Toast(
            message = state.toastMessage?.let { stringResource(it) },
            onDismiss = onToastShown,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    AppPreview {
        HomeScreen(
            state =
                HomeUiState(
                    newReleases = HomeFeed(games = listOf(HomeMockData.sampleGame), hasLoaded = true),
                    mostAnticipated = HomeFeed(hasLoaded = true, hasError = true),
                ),
        )
    }
}
