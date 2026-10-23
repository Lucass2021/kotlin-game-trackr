package com.lucasdias.gametrackr.feature.app.search

import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.model.GamePlatform

data class SearchUiState(
    val scope: SearchScope = SearchScope.ALL,
    val query: String = "",
    val platformSlug: String? = null,
    val platforms: List<GamePlatform> = emptyList(),
    val games: List<Game> = emptyList(),
    val currentPage: Int = 0,
    val lastPage: Int = 1,
    val total: Int = 0,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isSearchPending: Boolean = false,
    val hasError: Boolean = false,
    val loadMoreFailed: Boolean = false,
) {
    val isWaiting: Boolean
        get() = games.isEmpty() && !hasError && (currentPage == 0 || isLoading || isSearchPending)

    val isEmpty: Boolean
        get() = currentPage > 0 && games.isEmpty() && !hasError && !isLoading && !isSearchPending

    val isSearching: Boolean
        get() = query.isNotBlank()

    val canLoadMore: Boolean
        get() = currentPage in 1 until lastPage && !isLoading && !isLoadingMore && !isSearchPending && !loadMoreFailed

    companion object {
        const val PAGE_SIZE = 20
    }
}
