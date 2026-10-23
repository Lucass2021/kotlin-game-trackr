package com.lucasdias.gametrackr.feature.app.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.pagination.InfiniteGridScrollEffect
import com.lucasdias.gametrackr.core.pagination.LoadMoreErrorRow
import com.lucasdias.gametrackr.core.pagination.LoadingMoreIndicator
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.game.HomeMockData
import com.lucasdias.gametrackr.feature.app.search.SearchUiState

private const val GRID_COLUMNS = 2
private val TITLE_SIZE = 22.sp
private val COUNT_SIZE = 14.sp

@Composable
fun SearchResultsGrid(
    state: SearchUiState,
    onLoadMore: () -> Unit,
    onGameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyGridState()

    InfiniteGridScrollEffect(gridState = gridState, canLoadMore = state.canLoadMore, onLoadMore = onLoadMore)

    LazyVerticalGrid(
        columns = GridCells.Fixed(GRID_COLUMNS),
        state = gridState,
        contentPadding = PaddingValues(start = Spacing.lg, end = Spacing.lg, top = Spacing.xxs, bottom = Spacing.xl),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
        modifier = modifier.fillMaxSize(),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            SectionHeader(state = state)
        }
        items(state.games.size) { index ->
            val game = state.games[index]
            val interactionSource = remember { MutableInteractionSource() }
            SearchResultCard(
                game = game,
                modifier =
                    Modifier
                        .pressScale(interactionSource)
                        .clickable(interactionSource = interactionSource, indication = null, role = Role.Button) {
                            onGameClick(game.slug)
                        },
            )
        }
        if (state.isLoadingMore) {
            item(span = { GridItemSpan(maxLineSpan) }) { LoadingMoreIndicator() }
        }
        if (state.loadMoreFailed) {
            item(span = { GridItemSpan(maxLineSpan) }) { LoadMoreErrorRow(onRetry = onLoadMore) }
        }
    }
}

@Composable
private fun SectionHeader(state: SearchUiState) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = Spacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text =
                when {
                    state.isSearching -> stringResource(R.string.search_results)
                    state.scope.isFiltered -> stringResource(state.scope.titleRes)
                    else -> stringResource(R.string.search_popular)
                },
            color = AppTextPrimary,
            style = AppType.headline(TITLE_SIZE),
        )
        Text(
            text = pluralStringResource(R.plurals.search_result_count, state.total, state.total),
            color = AppTextSecondary,
            style = AppType.body(COUNT_SIZE),
        )
    }
}

@Preview
@Composable
private fun SearchResultsGridPreview() {
    AppPreview {
        SearchResultsGrid(
            state = SearchUiState(games = listOf(HomeMockData.sampleGame), currentPage = 1, total = 1),
            onLoadMore = {},
            onGameClick = {},
        )
    }
}
