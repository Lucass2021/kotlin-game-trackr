package com.lucasdias.gametrackr.feature.app.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.pagination.PaginationState
import com.lucasdias.gametrackr.data.community.CommunityRepository
import com.lucasdias.gametrackr.data.community.CommunityUpdates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommunityViewModel(
    private val repository: CommunityRepository,
    private val communityUpdates: CommunityUpdates,
) : ViewModel() {
    val communitiesPagination = PaginationState<Community>()

    val communities get() = communitiesPagination.items

    private val _isLoadingCommunities = MutableStateFlow(false)
    val isLoadingCommunities: StateFlow<Boolean> = _isLoadingCommunities.asStateFlow()

    private val _communitiesError = MutableStateFlow(false)
    val communitiesError: StateFlow<Boolean> = _communitiesError.asStateFlow()

    init {
        viewModelScope.launch {
            communityUpdates.joins.collect { join ->
                val index = communities.indexOfFirst { it.id == join.communityId }
                if (index >= 0) communities[index] = communities[index].copy(isJoined = join.isJoined)
            }
        }
    }

    fun loadCommunities(
        search: String? = null,
        reset: Boolean = true,
    ) {
        if (reset) {
            if (_isLoadingCommunities.value) return
            _isLoadingCommunities.value = true
            _communitiesError.value = false
            communitiesPagination.reset()
        } else {
            if (!communitiesPagination.canLoadMore) return
            communitiesPagination.setLoading(true)
        }

        val nextPage = communitiesPagination.currentPage + 1

        viewModelScope.launch {
            repository
                .fetchCommunities(page = nextPage, perPage = COMMUNITIES_PAGE_SIZE, search = search)
                .onSuccess { communitiesPagination.append(it) }
                .onFailure { if (reset) _communitiesError.value = true }
            if (reset) _isLoadingCommunities.value = false
            communitiesPagination.setLoading(false)
        }
    }

    fun loadMoreCommunities(search: String? = null) = loadCommunities(search = search, reset = false)

    fun joinCommunity(community: Community) = setJoined(community, true)

    fun leaveCommunity(community: Community) = setJoined(community, false)

    private fun setJoined(
        community: Community,
        joined: Boolean,
    ) {
        val index = communities.indexOfFirst { it.id == community.id }
        if (index < 0) return
        communities[index] = community.copy(isJoined = joined)

        viewModelScope.launch {
            val result =
                if (joined) {
                    repository.joinCommunity(community.id)
                } else {
                    repository.leaveCommunity(community.id)
                }
            result.onSuccess { communityUpdates.publish(community.id, joined) }
            result.onFailure {
                val i = communities.indexOfFirst { it.id == community.id }
                if (i >= 0) {
                    communities[i] = communities[i].copy(isJoined = !joined)
                }
            }
        }
    }

    fun addCommunity(community: Community) {
        communities.add(0, community)
    }

    fun removeCommunity(id: Long) {
        communities.removeAll { it.id == id }
    }

    private companion object {
        const val COMMUNITIES_PAGE_SIZE = 30
    }
}
