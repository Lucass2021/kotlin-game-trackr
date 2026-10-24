package com.lucasdias.gametrackr.feature.app.community.communitydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.model.prependingPost
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.data.community.CommunityFeedRepository
import com.lucasdias.gametrackr.data.community.CommunityRepository
import com.lucasdias.gametrackr.data.community.CommunityUpdates
import com.lucasdias.gametrackr.data.community.PostUpdates
import com.lucasdias.gametrackr.feature.app.community.CommunityDetailTab
import com.lucasdias.gametrackr.feature.app.community.PostListActions
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommunityDetailViewModel(
    private val repository: CommunityRepository,
    private val feedRepository: CommunityFeedRepository,
    private val postUpdates: PostUpdates,
    private val communityUpdates: CommunityUpdates,
    private val community: Community,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CommunityDetailUiState(isJoined = community.isJoined))
    val uiState: StateFlow<CommunityDetailUiState> = _uiState.asStateFlow()

    private var postsJob: Job? = null
    private var membersJob: Job? = null
    private var isJoinPending = false
    private val postActions =
        PostListActions(
            scope = viewModelScope,
            repository = repository,
            feedRepository = feedRepository,
            postUpdates = postUpdates,
            posts = { _uiState.value.posts },
            replacePost = ::replacePost,
            addPost = { post -> _uiState.update { it.copy(posts = it.posts.prependingPost(post)) } },
            showToast = { message -> _uiState.update { it.copy(toastMessage = message) } },
            communityId = community.id,
        )

    fun loadIfNeeded() {
        val state = _uiState.value
        if (state.currentPage > 0 || state.isLoading) return
        load()
    }

    fun load() {
        loadPosts(page = 1, isRefresh = false)
        loadMembers()
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        loadPosts(page = 1, isRefresh = true)
        loadMembers()
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.currentPage == 0 || state.currentPage >= state.lastPage || state.isLoading || state.isLoadingMore) return
        loadPosts(page = state.currentPage + 1, isRefresh = false)
    }

    fun retryMembers() = loadMembers()

    fun onTabSelect(tab: CommunityDetailTab) {
        _uiState.update { it.copy(tab = tab) }
    }

    fun onJoinTap() {
        if (isJoinPending) return
        if (_uiState.value.isJoined) {
            _uiState.update { it.copy(showLeaveConfirmation = true) }
        } else {
            setJoined(true)
        }
    }

    fun onLeaveConfirmed() {
        _uiState.update { it.copy(showLeaveConfirmation = false) }
        setJoined(false)
    }

    fun onLeaveDismissed() {
        _uiState.update { it.copy(showLeaveConfirmation = false) }
    }

    fun onDeleteRequest() {
        _uiState.update { it.copy(showDeleteConfirmation = true) }
    }

    fun onDeleteDismissed() {
        _uiState.update { it.copy(showDeleteConfirmation = false) }
    }

    fun onDeleteConfirmed() {
        _uiState.update { it.copy(showDeleteConfirmation = false) }
        viewModelScope.launch {
            repository
                .deleteCommunity(community.id)
                .onSuccess { _uiState.update { it.copy(isDeleted = true) } }
                .onFailure { error ->
                    val message =
                        if (error is ApiError.Forbidden) R.string.community_delete_forbidden else R.string.community_delete_error
                    _uiState.update { it.copy(toastMessage = message) }
                }
        }
    }

    fun onLike(postId: Long) = postActions.onLike(postId)

    fun onBookmark(postId: Long) = postActions.onBookmark(postId)

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    private fun setJoined(joined: Boolean) {
        isJoinPending = true
        _uiState.update { it.copy(isJoined = joined) }
        viewModelScope.launch {
            val result = if (joined) repository.joinCommunity(community.id) else repository.leaveCommunity(community.id)
            result.onSuccess { communityUpdates.publish(community.id, joined) }
            result.onFailure {
                val message = if (joined) R.string.community_join_error else R.string.community_leave_error
                _uiState.update { it.copy(isJoined = !joined, toastMessage = message) }
            }
            isJoinPending = false
        }
    }

    private fun replacePost(
        postId: Long,
        transform: (CommunityPost) -> CommunityPost,
    ) {
        _uiState.update { state -> state.copy(posts = state.posts.map { if (it.id == postId) transform(it) else it }) }
    }

    private fun loadPosts(
        page: Int,
        isRefresh: Boolean,
    ) {
        postsJob?.cancel()
        _uiState.update {
            when {
                isRefresh -> it
                page == 1 -> it.copy(posts = emptyList(), currentPage = 0, lastPage = 1, isLoading = true, isLoadingMore = false)
                else -> it.copy(isLoadingMore = true)
            }.copy(hasError = false, loadMoreFailed = false)
        }
        postsJob =
            viewModelScope.launch {
                repository
                    .fetchPosts(page = page, perPage = CommunityDetailUiState.PAGE_SIZE, communityId = community.id)
                    .onSuccess { result ->
                        _uiState.update {
                            it.copy(
                                posts = if (page == 1) result.items else it.posts + result.items,
                                currentPage = result.currentPage,
                                lastPage = result.lastPage,
                                isLoading = false,
                                isLoadingMore = false,
                                isRefreshing = false,
                            )
                        }
                    }.onFailure {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoadingMore = false,
                                isRefreshing = false,
                                hasError = page == 1 && it.posts.isEmpty(),
                                loadMoreFailed = page > 1,
                                toastMessage = if (isRefresh && it.posts.isNotEmpty()) R.string.community_refresh_error else it.toastMessage,
                            )
                        }
                    }
            }
    }

    private fun loadMembers() {
        membersJob?.cancel()
        _uiState.update { it.copy(membersFailed = false) }
        membersJob =
            viewModelScope.launch {
                repository
                    .fetchCommunityMembers(community.id)
                    .onSuccess { members -> _uiState.update { it.copy(members = members) } }
                    .onFailure { _uiState.update { it.copy(membersFailed = true) } }
            }
    }
}
