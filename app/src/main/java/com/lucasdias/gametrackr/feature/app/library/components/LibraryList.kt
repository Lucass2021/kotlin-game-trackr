package com.lucasdias.gametrackr.feature.app.library.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.core.model.LibraryEntry
import com.lucasdias.gametrackr.core.pagination.InfiniteScrollEffect
import com.lucasdias.gametrackr.core.pagination.LoadMoreErrorRow
import com.lucasdias.gametrackr.core.pagination.LoadingMoreIndicator
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.library.LibraryMockData

@Composable
fun LibraryList(
    entries: List<LibraryEntry>,
    canLoadMore: Boolean,
    isLoadingMore: Boolean,
    loadMoreFailed: Boolean,
    onLoadMore: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    onGameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    InfiniteScrollEffect(listState = listState, canLoadMore = canLoadMore, onLoadMore = onLoadMore)

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(start = Spacing.lg, end = Spacing.lg, bottom = Spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        modifier = modifier.fillMaxSize(),
    ) {
        items(entries, key = { it.slug }) { entry ->
            LibraryEntryRow(
                entry = entry,
                onFavorite = { onToggleFavorite(entry.slug) },
                modifier = Modifier.clickable { onGameClick(entry.slug) },
            )
        }

        if (isLoadingMore) {
            item { LoadingMoreIndicator() }
        }

        if (loadMoreFailed) {
            item { LoadMoreErrorRow(onRetry = onLoadMore) }
        }
    }
}

@Preview
@Composable
private fun LibraryListPreview() {
    AppPreview {
        LibraryList(
            entries = LibraryMockData.entries,
            canLoadMore = false,
            isLoadingMore = false,
            loadMoreFailed = true,
            onLoadMore = {},
            onToggleFavorite = {},
            onGameClick = {},
        )
    }
}
