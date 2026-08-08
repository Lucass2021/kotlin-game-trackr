package com.lucasdias.gametrackr.core.pagination

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

class MockPaginationState<T>(
    private val allItems: List<T>,
    private val pageSize: Int = 6,
) {
    val items = mutableStateListOf<T>()
    var isLoadingMore by mutableStateOf(false)
        private set

    init {
        items.addAll(allItems.take(pageSize))
    }

    val canLoadMore: Boolean get() = items.size < allItems.size && !isLoadingMore

    suspend fun loadNextPage() {
        if (!canLoadMore) return
        isLoadingMore = true
        delay(600)
        val start = items.size
        val end = minOf(start + pageSize, allItems.size)
        items.addAll(allItems.subList(start, end))
        isLoadingMore = false
    }

    suspend fun reset() {
        items.clear()
        loadNextPage()
    }
}
