package com.lucasdias.gametrackr.feature.app.community.discover

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.core.model.Community

data class DiscoverCommunitiesUiState(
    val query: String = "",
    val category: String = ALL_CATEGORY,
    val communities: List<Community> = emptyList(),
    val currentPage: Int = 0,
    val lastPage: Int = 1,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasError: Boolean = false,
    val loadMoreFailed: Boolean = false,
    val communityToLeave: Community? = null,
    @param:StringRes val toastMessage: Int? = null,
) {
    val isEmpty: Boolean
        get() = currentPage > 0 && communities.isEmpty() && !hasError

    val canLoadMore: Boolean
        get() = currentPage in 1 until lastPage && !isLoading && !isLoadingMore && !loadMoreFailed

    companion object {
        const val ALL_CATEGORY = "All"
        const val PAGE_SIZE = 30
    }
}
