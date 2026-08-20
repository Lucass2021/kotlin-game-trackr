package com.lucasdias.gametrackr.core.pagination

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

@Composable
fun InfiniteScrollEffect(
    listState: LazyListState,
    canLoadMore: Boolean,
    threshold: Int = 3,
    onLoadMore: () -> Unit,
) {
    val shouldLoad =
        remember(canLoadMore) {
            derivedStateOf {
                if (!canLoadMore) return@derivedStateOf false
                val total = listState.layoutInfo.totalItemsCount
                if (total == 0) return@derivedStateOf false
                val lastVisible =
                    listState.layoutInfo.visibleItemsInfo
                        .lastOrNull()
                        ?.index ?: return@derivedStateOf false
                lastVisible >= total - threshold
            }
        }
    LaunchedEffect(shouldLoad.value) {
        if (shouldLoad.value) onLoadMore()
    }
}

@Composable
fun InfiniteGridScrollEffect(
    gridState: LazyGridState,
    canLoadMore: Boolean,
    threshold: Int = 3,
    onLoadMore: () -> Unit,
) {
    val shouldLoad =
        remember(canLoadMore) {
            derivedStateOf {
                if (!canLoadMore) return@derivedStateOf false
                val total = gridState.layoutInfo.totalItemsCount
                if (total == 0) return@derivedStateOf false
                val lastVisible =
                    gridState.layoutInfo.visibleItemsInfo
                        .lastOrNull()
                        ?.index ?: return@derivedStateOf false
                lastVisible >= total - threshold
            }
        }
    LaunchedEffect(shouldLoad.value) {
        if (shouldLoad.value) onLoadMore()
    }
}
