package com.lucasdias.gametrackr.feature.app.library

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.core.model.LibraryEntry
import com.lucasdias.gametrackr.core.model.LibraryStats
import com.lucasdias.gametrackr.core.model.LibraryStatus

data class LibraryUiState(
    val entries: List<LibraryEntry> = emptyList(),
    val stats: LibraryStats? = null,
    val filter: LibraryStatus? = null,
    val currentPage: Int = 0,
    val lastPage: Int = 1,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasError: Boolean = false,
    val loadMoreFailed: Boolean = false,
    @param:StringRes val errorMessage: Int? = null,
) {
    val canLoadMore: Boolean
        get() = currentPage in 1 until lastPage && !isLoading && !isLoadingMore && !loadMoreFailed

    val isLibraryEmpty: Boolean
        get() = currentPage > 0 && entries.isEmpty() && (filter == null || stats?.totalGames == 0)

    companion object {
        const val PAGE_SIZE = 3
    }
}
