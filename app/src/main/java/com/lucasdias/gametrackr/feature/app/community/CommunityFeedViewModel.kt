package com.lucasdias.gametrackr.feature.app.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.CommunityFeedFilter
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.data.community.CommunityFeedRepository
import com.lucasdias.gametrackr.data.community.CommunityRepository
import com.lucasdias.gametrackr.data.community.CommunityUpdates
import com.lucasdias.gametrackr.data.community.PostUpdates
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommunityFeedViewModel(
    private val feedRepository: CommunityFeedRepository,
    private val repository: CommunityRepository,
    private val postUpdates: PostUpdates,
    private val communityUpdates: CommunityUpdates,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CommunityFeedUiState())
    val uiState: StateFlow<CommunityFeedUiState> = _uiState.asStateFlow()

    private var postsJob: Job? = null
    private var isJoinPending = false
    private val postActions =
        PostListActions(
            scope = viewModelScope,
            repository = repository,
            feedRepository = feedRepository,
            postUpdates = postUpdates,
            posts = { _uiState.value.posts },
            replacePost = ::replacePost,
            showToast = { message -> _uiState.update { it.copy(toastMessage = message) } },
        )

    init {
        viewModelScope.launch {
            communityUpdates.joins.collect { join ->
                _uiState.update { state ->
                    val suggestion = state.suggestedCommunity?.takeIf { it.id == join.communityId }
                    state.copy(suggestedCommunity = suggestion?.copy(isJoined = join.isJoined) ?: state.suggestedCommunity)
                }
            }
        }
    }

    fun loadIfNeeded() {
        val state = _uiState.value
        if (state.currentPage > 0 || state.isLoading) return
        load()
    }

    fun load() {
        loadPosts(page = 1, isRefresh = false)
        loadSuggestion()
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        loadPosts(page = 1, isRefresh = true)
        loadSuggestion()
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.currentPage == 0 || state.currentPage >= state.lastPage) return
        if (state.isLoading || state.isLoadingMore || state.isRefreshing) return
        loadPosts(page = state.currentPage + 1, isRefresh = false)
    }

    fun onFilterChange(filter: CommunityFeedFilter) {
        if (filter == _uiState.value.filter) return
        _uiState.update { it.copy(filter = filter) }
        loadPosts(page = 1, isRefresh = false)
    }

    fun onLike(postId: Long) = postActions.onLike(postId)

    fun onBookmark(postId: Long) = postActions.onBookmark(postId)

    fun onJoinSuggested() {
        val community = _uiState.value.suggestedCommunity ?: return
        if (community.isJoined || isJoinPending) return
        isJoinPending = true
        _uiState.update { it.copy(suggestedCommunity = community.copy(isJoined = true)) }
        viewModelScope.launch {
            repository
                .joinCommunity(community.id)
                .onSuccess { communityUpdates.publish(community.id, isJoined = true) }
                .onFailure {
                    _uiState.update { it.copy(suggestedCommunity = community, toastMessage = R.string.community_join_error) }
                }
            isJoinPending = false
        }
    }

    fun onPostCreated(post: CommunityPost) {
        _uiState.update { state -> state.copy(posts = listOf(post) + state.posts.filterNot { it.id == post.id }) }
    }

    fun onPostDeleted(postId: Long) {
        _uiState.update { state -> state.copy(posts = state.posts.filterNot { it.id == postId }) }
    }

    fun onCommunityRemoved(communityId: Long) {
        _uiState.update { state ->
            state.copy(
                posts = state.posts.filterNot { it.communityId == communityId },
                suggestedCommunity = state.suggestedCommunity?.takeUnless { it.id == communityId },
            )
        }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    private fun replacePost(
        postId: Long,
        transform: (CommunityPost) -> CommunityPost,
    ) {
        _uiState.update { state -> state.copy(posts = state.posts.map { if (it.id == postId) transform(it) else it }) }
    }

    private fun loadSuggestion() {
        viewModelScope.launch {
            feedRepository.fetchSuggestedCommunity().onSuccess { community ->
                _uiState.update { it.copy(suggestedCommunity = community) }
            }
        }
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
        val filter = _uiState.value.filter
        postsJob =
            viewModelScope.launch {
                feedRepository
                    .fetchFeed(filter = filter, page = page, perPage = CommunityFeedUiState.PAGE_SIZE)
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
}
