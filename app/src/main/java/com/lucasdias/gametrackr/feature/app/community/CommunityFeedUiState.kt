package com.lucasdias.gametrackr.feature.app.community

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityFeedFilter
import com.lucasdias.gametrackr.core.model.CommunityPost

data class CommunityFeedUiState(
    val filter: CommunityFeedFilter = CommunityFeedFilter.LATEST,
    val posts: List<CommunityPost> = emptyList(),
    val currentPage: Int = 0,
    val lastPage: Int = 1,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val hasError: Boolean = false,
    val loadMoreFailed: Boolean = false,
    val suggestedCommunity: Community? = null,
    @param:StringRes val toastMessage: Int? = null,
) {
    val isEmpty: Boolean
        get() = currentPage > 0 && posts.isEmpty() && !hasError

    val canLoadMore: Boolean
        get() = currentPage in 1 until lastPage && !isLoading && !isLoadingMore && !isRefreshing && !loadMoreFailed

    companion object {
        const val PAGE_SIZE = 20
    }
}
