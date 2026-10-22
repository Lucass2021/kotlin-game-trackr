package com.lucasdias.gametrackr.feature.app.community.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.data.community.CommunityUpdates
import com.lucasdias.gametrackr.data.community.DiscoverCommunitiesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiscoverCommunitiesViewModel(
    private val repository: DiscoverCommunitiesRepository,
    private val communityUpdates: CommunityUpdates,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiscoverCommunitiesUiState())
    val uiState: StateFlow<DiscoverCommunitiesUiState> = _uiState.asStateFlow()

    private var pageJob: Job? = null
    private var searchJob: Job? = null
    private val pendingJoinIds = mutableSetOf<Long>()
    private var appliedCategory: String? = null
    private var appliedSearch: String? = null

    init {
        viewModelScope.launch {
            communityUpdates.joins.collect { join -> replaceJoined(join.communityId, join.isJoined) }
        }
    }

    fun loadIfNeeded() {
        val state = _uiState.value
        if (state.currentPage > 0 || state.isLoading) return
        load()
    }

    fun load() {
        searchJob?.cancel()
        loadPage(page = 1)
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.currentPage == 0 || state.currentPage >= state.lastPage || state.isLoading || state.isLoadingMore) return
        loadPage(page = state.currentPage + 1)
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        searchJob?.cancel()
        searchJob =
            viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_MS)
                loadPage(page = 1)
            }
    }

    fun onCategoryChange(category: String) {
        if (category == _uiState.value.category) return
        _uiState.update { it.copy(category = category) }
        load()
    }

    fun onJoinTap(community: Community) {
        if (community.id in pendingJoinIds) return
        if (community.isJoined) {
            _uiState.update { it.copy(communityToLeave = community) }
        } else {
            setJoined(community, joined = true)
        }
    }

    fun onLeaveConfirmed() {
        val community = _uiState.value.communityToLeave ?: return
        _uiState.update { it.copy(communityToLeave = null) }
        setJoined(community, joined = false)
    }

    fun onLeaveDismissed() {
        _uiState.update { it.copy(communityToLeave = null) }
    }

    fun onCommunityCreated(community: Community) {
        if (appliedCategory != null || appliedSearch != null) return
        _uiState.update { state -> state.copy(communities = listOf(community) + state.communities.filterNot { it.id == community.id }) }
    }

    fun onCommunityRemoved(communityId: Long) {
        _uiState.update { state -> state.copy(communities = state.communities.filterNot { it.id == communityId }) }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    private fun setJoined(
        community: Community,
        joined: Boolean,
    ) {
        pendingJoinIds.add(community.id)
        replaceJoined(community.id, joined)
        viewModelScope.launch {
            repository
                .setJoined(community.id, joined)
                .onSuccess { communityUpdates.publish(community.id, joined) }
                .onFailure {
                    replaceJoined(community.id, !joined)
                    val message = if (joined) R.string.community_join_error else R.string.community_leave_error
                    _uiState.update { it.copy(toastMessage = message) }
                }
            pendingJoinIds.remove(community.id)
        }
    }

    private fun replaceJoined(
        communityId: Long,
        joined: Boolean,
    ) {
        _uiState.update { state ->
            state.copy(communities = state.communities.map { if (it.id == communityId) it.copy(isJoined = joined) else it })
        }
    }

    private fun loadPage(page: Int) {
        pageJob?.cancel()
        _uiState.update {
            if (page == 1) {
                it.copy(communities = emptyList(), currentPage = 0, lastPage = 1, isLoading = true, isLoadingMore = false)
            } else {
                it.copy(isLoadingMore = true)
            }.copy(hasError = false, loadMoreFailed = false)
        }
        if (page == 1) {
            val state = _uiState.value
            appliedCategory = state.category.takeUnless { it == DiscoverCommunitiesUiState.ALL_CATEGORY }
            appliedSearch = state.query.trim().ifBlank { null }
        }
        val category = appliedCategory
        val search = appliedSearch
        pageJob =
            viewModelScope.launch {
                repository
                    .fetchCommunities(category = category, search = search, page = page, perPage = DiscoverCommunitiesUiState.PAGE_SIZE)
                    .onSuccess { result ->
                        _uiState.update {
                            it.copy(
                                communities = if (page == 1) result.items else it.communities + result.items,
                                currentPage = result.currentPage,
                                lastPage = result.lastPage,
                                isLoading = false,
                                isLoadingMore = false,
                            )
                        }
                    }.onFailure {
                        _uiState.update {
                            it.copy(isLoading = false, isLoadingMore = false, hasError = page == 1, loadMoreFailed = page > 1)
                        }
                    }
            }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 300L
    }
}
