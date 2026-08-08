package com.lucasdias.gametrackr.feature.app.community

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.core.network.CommunityApi
import com.lucasdias.gametrackr.core.network.dto.CommentBody
import com.lucasdias.gametrackr.core.network.dto.CreatePostRequest
import com.lucasdias.gametrackr.core.network.dto.toDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommunityViewModel(
    private val api: CommunityApi,
) : ViewModel() {
    val feed: SnapshotStateList<CommunityPost> = mutableStateListOf()
    val communities: SnapshotStateList<Community> = mutableStateListOf()

    private val _isLoadingFeed = MutableStateFlow(false)
    val isLoadingFeed: StateFlow<Boolean> = _isLoadingFeed.asStateFlow()

    private val _isLoadingCommunities = MutableStateFlow(false)
    val isLoadingCommunities: StateFlow<Boolean> = _isLoadingCommunities.asStateFlow()

    private val _feedError = MutableStateFlow(false)
    val feedError: StateFlow<Boolean> = _feedError.asStateFlow()

    private val _communitiesError = MutableStateFlow(false)
    val communitiesError: StateFlow<Boolean> = _communitiesError.asStateFlow()

    fun loadFeed() {
        if (_isLoadingFeed.value) return
        _isLoadingFeed.value = true
        _feedError.value = false

        viewModelScope.launch {
            try {
                val response = api.getPosts(perPage = 20)
                feed.clear()
                feed.addAll(response.data.map { it.toDomain() })
            } catch (_: Exception) {
                _feedError.value = true
            }
            _isLoadingFeed.value = false
        }
    }

    fun loadCommunities(search: String? = null) {
        if (_isLoadingCommunities.value) return
        _isLoadingCommunities.value = true
        _communitiesError.value = false

        viewModelScope.launch {
            try {
                val response = api.getCommunities(search = search, perPage = 30)
                communities.clear()
                communities.addAll(response.data.map { it.toDomain() })
            } catch (_: Exception) {
                _communitiesError.value = true
            }
            _isLoadingCommunities.value = false
        }
    }

    fun toggleLike(post: CommunityPost) {
        val index = feed.indexOfFirst { it.id == post.id }
        if (index < 0) return

        feed[index] =
            post.copy(
                isLiked = !post.isLiked,
                likes = post.likes + if (post.isLiked) -1 else 1,
            )

        viewModelScope.launch {
            try {
                val response = api.toggleLike(post.id)
                val i = feed.indexOfFirst { it.id == post.id }
                if (i >= 0) {
                    feed[i] = feed[i].copy(isLiked = response.isLiked, likes = response.likes)
                }
            } catch (_: Exception) {
                val i = feed.indexOfFirst { it.id == post.id }
                if (i >= 0) {
                    feed[i] =
                        feed[i].copy(
                            isLiked = !feed[i].isLiked,
                            likes = feed[i].likes + if (feed[i].isLiked) -1 else 1,
                        )
                }
            }
        }
    }

    fun toggleBookmark(post: CommunityPost) {
        val index = feed.indexOfFirst { it.id == post.id }
        if (index < 0) return
        feed[index] = feed[index].copy(isBookmarked = !feed[index].isBookmarked)
    }

    fun toggleJoin(community: Community) {
        val index = communities.indexOfFirst { it.id == community.id }
        if (index < 0) return
        val wasJoined = community.isJoined
        communities[index] = community.copy(isJoined = !wasJoined)

        viewModelScope.launch {
            try {
                if (wasJoined) {
                    api.leaveCommunity(community.id)
                } else {
                    api.joinCommunity(community.id)
                }
            } catch (_: Exception) {
                val i = communities.indexOfFirst { it.id == community.id }
                if (i >= 0) {
                    communities[i] = communities[i].copy(isJoined = wasJoined)
                }
            }
        }
    }

    fun createPost(
        title: String,
        body: String,
        communityId: Long,
    ): CommunityPost? {
        var result: CommunityPost? = null
        viewModelScope.launch {
            try {
                val request =
                    CreatePostRequest(
                        title = title,
                        description = body,
                        communityId = communityId,
                    )
                val response = api.createPost(request)
                val post = response.post.toDomain()
                feed.add(0, post)
                result = post
            } catch (_: Exception) {
            }
        }
        return result
    }

    suspend fun createPostSuspend(
        title: String,
        body: String,
        communityId: Long,
    ): CommunityPost? =
        try {
            val request =
                CreatePostRequest(
                    title = title,
                    description = body,
                    communityId = communityId,
                )
            val response = api.createPost(request)
            val post = response.post.toDomain()
            feed.add(0, post)
            post
        } catch (_: Exception) {
            null
        }
}
