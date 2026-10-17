package com.lucasdias.gametrackr.feature.app.community.communitydetail

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.core.model.CommunityMember
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.feature.app.community.CommunityDetailTab

data class CommunityDetailUiState(
    val isJoined: Boolean = false,
    val tab: CommunityDetailTab = CommunityDetailTab.POSTS,
    val posts: List<CommunityPost> = emptyList(),
    val currentPage: Int = 0,
    val lastPage: Int = 1,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val hasError: Boolean = false,
    val loadMoreFailed: Boolean = false,
    val members: List<CommunityMember> = emptyList(),
    val membersFailed: Boolean = false,
    val showLeaveConfirmation: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val isDeleted: Boolean = false,
    @param:StringRes val toastMessage: Int? = null,
) {
    val isEmpty: Boolean
        get() = currentPage > 0 && posts.isEmpty() && !hasError

    val canLoadMore: Boolean
        get() = currentPage in 1 until lastPage && !isLoading && !isLoadingMore && !loadMoreFailed

    companion object {
        const val PAGE_SIZE = 30
    }
}
