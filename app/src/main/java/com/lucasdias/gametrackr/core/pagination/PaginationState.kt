package com.lucasdias.gametrackr.core.pagination

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lucasdias.gametrackr.core.network.dto.PaginatedResponse

class PaginationState<T> {
    val items = mutableStateListOf<T>()
    var currentPage by mutableIntStateOf(0)
        private set
    var lastPage by mutableIntStateOf(1)
        private set
    var total by mutableIntStateOf(0)
        private set
    var isLoadingMore by mutableStateOf(false)
        private set

    val canLoadMore: Boolean get() = currentPage < lastPage && !isLoadingMore

    fun reset() {
        items.clear()
        currentPage = 0
        lastPage = 1
        total = 0
    }

    fun append(
        response: PaginatedResponse<*>,
        newItems: List<T>,
    ) {
        items.addAll(newItems)
        currentPage = response.currentPage
        lastPage = response.lastPage
        total = response.total
    }

    fun snapshot(): PaginationSnapshot<T> = PaginationSnapshot(items.toList(), currentPage, lastPage, total)

    fun restore(snapshot: PaginationSnapshot<T>) {
        items.clear()
        items.addAll(snapshot.items)
        currentPage = snapshot.currentPage
        lastPage = snapshot.lastPage
        total = snapshot.total
    }

    fun setLoading(value: Boolean) {
        isLoadingMore = value
    }
}

data class PaginationSnapshot<T>(
    val items: List<T>,
    val currentPage: Int,
    val lastPage: Int,
    val total: Int,
)
