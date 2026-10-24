package com.lucasdias.gametrackr.feature.app.community

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.data.community.CommunityFeedRepository
import com.lucasdias.gametrackr.data.community.CommunityRepository
import com.lucasdias.gametrackr.data.community.PostUpdates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PostListActions(
    private val scope: CoroutineScope,
    private val repository: CommunityRepository,
    private val feedRepository: CommunityFeedRepository,
    private val postUpdates: PostUpdates,
    private val posts: () -> List<CommunityPost>,
    private val replacePost: (Long, (CommunityPost) -> CommunityPost) -> Unit,
    private val addPost: (CommunityPost) -> Unit,
    private val showToast: (Int) -> Unit,
    private val communityId: Long? = null,
) {
    private val pendingLikeIds = mutableSetOf<Long>()
    private val pendingBookmarkIds = mutableSetOf<Long>()

    init {
        scope.launch { postUpdates.posts.collect { post -> replacePost(post.id) { post } } }
        scope.launch {
            postUpdates.createdPosts.collect { post ->
                if (communityId == null || post.communityId == communityId) addPost(post)
            }
        }
    }

    fun onLike(postId: Long) {
        val previous = post(postId) ?: return
        if (!pendingLikeIds.add(postId)) return
        val liked = !previous.isLiked
        replacePost(postId) { it.copy(isLiked = liked, likes = previous.likes + if (liked) 1 else -1) }
        scope.launch {
            repository
                .togglePostLike(postId)
                .onSuccess { status ->
                    replacePost(postId) { it.copy(isLiked = status.isLiked, likes = status.likes) }
                    publish(postId)
                }.onFailure {
                    replacePost(postId) { it.copy(isLiked = previous.isLiked, likes = previous.likes) }
                    showToast(R.string.community_like_error)
                }
            pendingLikeIds.remove(postId)
        }
    }

    fun onBookmark(postId: Long) {
        val previous = post(postId) ?: return
        if (!pendingBookmarkIds.add(postId)) return
        replacePost(postId) { it.copy(isBookmarked = !previous.isBookmarked) }
        scope.launch {
            feedRepository
                .setBookmark(postId, !previous.isBookmarked)
                .onSuccess { publish(postId) }
                .onFailure {
                    replacePost(postId) { it.copy(isBookmarked = previous.isBookmarked) }
                    showToast(R.string.community_bookmark_error)
                }
            pendingBookmarkIds.remove(postId)
        }
    }

    private fun post(postId: Long): CommunityPost? = posts().firstOrNull { it.id == postId }

    private fun publish(postId: Long) {
        post(postId)?.let(postUpdates::publish)
    }
}
