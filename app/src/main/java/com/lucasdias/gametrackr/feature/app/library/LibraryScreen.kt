package com.lucasdias.gametrackr.feature.app.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.library.LibraryMockData
import com.lucasdias.gametrackr.feature.app.library.components.LibraryFilterChips
import com.lucasdias.gametrackr.feature.app.library.components.LibraryList
import com.lucasdias.gametrackr.feature.app.library.components.LibraryStatsBar

@Composable
fun LibraryScreen(
    state: LibraryUiState,
    isGuest: Boolean,
    onFilterChange: (LibraryStatus?) -> Unit,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    onErrorShown: () -> Unit,
    modifier: Modifier = Modifier,
    onCreateAccount: () -> Unit = {},
    onBrowseGames: () -> Unit = {},
    onGameClick: (String) -> Unit = {},
) {
    if (isGuest) {
        LibraryGuestState(onCreateAccount = onCreateAccount)
        return
    }

    Box(modifier = modifier.fillMaxSize().background(AppBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            val stats = state.stats?.takeUnless { state.isLibraryEmpty }
            if (stats != null) {
                LibraryStatsBar(
                    stats = stats,
                    modifier = Modifier.padding(start = Spacing.lg, end = Spacing.lg, top = Spacing.sm, bottom = Spacing.md),
                )
            }

            LibraryFilterChips(
                selection = state.filter,
                onSelect = onFilterChange,
                modifier =
                    Modifier
                        .then(if (stats == null) Modifier.padding(top = Spacing.sm) else Modifier)
                        .padding(bottom = Spacing.sm),
            )

            LibraryContent(
                state = state,
                onRetry = onRetry,
                onLoadMore = onLoadMore,
                onToggleFavorite = onToggleFavorite,
                onBrowseGames = onBrowseGames,
                onGameClick = onGameClick,
            )
        }

        Toast(
            message = state.errorMessage?.let { stringResource(it) },
            onDismiss = onErrorShown,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Composable
private fun LibraryContent(
    state: LibraryUiState,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    onBrowseGames: () -> Unit,
    onGameClick: (String) -> Unit,
) {
    when {
        state.hasError -> {
            EmptyState(
                icon = AppIcon.INFO,
                title = stringResource(R.string.library_load_error_title),
                message = stringResource(R.string.library_load_error_message),
                actionTitle = stringResource(R.string.library_retry),
                onAction = onRetry,
                modifier = Modifier.fillMaxSize(),
            )
        }

        state.currentPage == 0 -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppPrimary)
            }
        }

        state.isLibraryEmpty -> {
            EmptyState(
                icon = AppIcon.BRAND,
                title = stringResource(R.string.library_empty_title),
                message = stringResource(R.string.library_empty_subtitle),
                actionTitle = stringResource(R.string.library_browse_games),
                actionIcon = AppIcon.FORWARD,
                onAction = onBrowseGames,
                modifier = Modifier.fillMaxSize(),
            )
        }

        state.entries.isEmpty() -> {
            FilterEmptyState(status = state.filter)
        }

        else -> {
            LibraryList(
                entries = state.entries,
                canLoadMore = state.canLoadMore,
                isLoadingMore = state.isLoadingMore,
                loadMoreFailed = state.loadMoreFailed,
                onLoadMore = onLoadMore,
                onToggleFavorite = onToggleFavorite,
                onGameClick = onGameClick,
            )
        }
    }
}

@Composable
private fun LibraryGuestState(onCreateAccount: () -> Unit) {
    EmptyState(
        icon = AppIcon.BRAND,
        title = stringResource(R.string.library_guest_title),
        message = stringResource(R.string.library_guest_message),
        actionTitle = stringResource(R.string.library_guest_action),
        onAction = onCreateAccount,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun FilterEmptyState(status: LibraryStatus?) {
    EmptyState(
        icon = AppIcon.LIBRARY,
        title = stringResource(R.string.library_filter_empty_title),
        message =
            stringResource(
                R.string.library_filter_empty_subtitle,
                status?.let { stringResource(it.labelRes) } ?: "",
            ),
        modifier = Modifier.fillMaxSize(),
    )
}

@Preview
@Composable
private fun LibraryScreenPreview() {
    AppPreview {
        LibraryScreen(
            state = LibraryUiState(entries = LibraryMockData.entries, stats = LibraryMockData.stats, currentPage = 1),
            isGuest = false,
            onFilterChange = {},
            onRetry = {},
            onLoadMore = {},
            onToggleFavorite = {},
            onErrorShown = {},
        )
    }
}

@Preview
@Composable
private fun LibraryScreenErrorPreview() {
    AppPreview {
        LibraryScreen(
            state = LibraryUiState(hasError = true),
            isGuest = false,
            onFilterChange = {},
            onRetry = {},
            onLoadMore = {},
            onToggleFavorite = {},
            onErrorShown = {},
        )
    }
}
