package com.lucasdias.gametrackr.data.community

import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityMember
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.model.LikeStatus
import com.lucasdias.gametrackr.core.model.PostComment
import com.lucasdias.gametrackr.core.model.addingReply
import com.lucasdias.gametrackr.core.model.findComment
import com.lucasdias.gametrackr.core.model.replacingComment
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.core.pagination.fakePage
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeCommunityRepository(
    communities: List<Community> = CommunityMockData.all,
    posts: List<CommunityPost> = CommunityMockData.communityPosts,
    private val members: List<CommunityMember> = CommunityMockData.members,
    comments: List<PostComment> = CommunityMockData.comments,
    var failure: Throwable? = null,
) : CommunityRepository {
    private val communities = communities.toMutableList()
    private val posts = posts.toMutableList()
    private val comments = comments.toMutableList()
    private val joinedIds = communities.filter { it.isJoined }.map { it.id }.toMutableSet()

    var createCommunityCount = 0
        private set
    var fetchPostsCount = 0
        private set
    var fetchMembersCount = 0
        private set
    var likeCount = 0
        private set
    var deleteCount = 0
        private set
    var fetchCommentsCount = 0
        private set
    var sendCommentCount = 0
        private set
    var commentLikeCount = 0
        private set

    override suspend fun fetchCommunities(
        page: Int,
        perPage: Int,
        search: String?,
    ): Result<Page<Community>> =
        respond {
            val matches = communities.filter { search.isNullOrBlank() || it.name.contains(search, ignoreCase = true) }
            fakePage(matches.map { it.copy(isJoined = it.id in joinedIds) }, page, perPage)
        }

    override suspend fun fetchCommunity(id: Long): Result<Community> =
        respond { communities.firstOrNull { it.id == id }?.copy(isJoined = id in joinedIds) ?: throw ApiError.NotFound }

    override suspend fun fetchJoinedCommunities(): Result<List<Community>> =
        respond { communities.filter { it.id in joinedIds }.map { it.copy(isJoined = true) } }

    override suspend fun fetchCommunityMembers(id: Long): Result<List<CommunityMember>> {
        fetchMembersCount++
        return respond { members }
    }

    override suspend fun createCommunity(
        title: String,
        description: String,
    ): Result<Community> {
        createCommunityCount++
        return respond {
            val base = communities.first()
            val created =
                base.copy(
                    id = (communities.maxOf { it.id } + 1),
                    name = title,
                    description = description,
                    isJoined = true,
                    members = CREATOR_MEMBER_COUNT,
                )
            communities.add(0, created)
            joinedIds.add(created.id)
            created
        }
    }

    override suspend fun deleteCommunity(id: Long): Result<Unit> {
        deleteCount++
        return respond { if (!communities.removeIf { it.id == id }) throw ApiError.NotFound }
    }

    override suspend fun joinCommunity(id: Long): Result<Unit> = respond { joinedIds.add(id) }

    override suspend fun leaveCommunity(id: Long): Result<Unit> = respond { joinedIds.remove(id) }

    override suspend fun fetchPosts(
        page: Int,
        perPage: Int,
        communityId: Long?,
    ): Result<Page<CommunityPost>> {
        fetchPostsCount++
        return respond {
            fakePage(posts.filter { communityId == null || it.communityId == null || it.communityId == communityId }, page, perPage)
        }
    }

    override suspend fun fetchPostComments(postId: Long): Result<List<PostComment>> {
        fetchCommentsCount++
        return respond { comments.toList() }
    }

    override suspend fun createPost(
        title: String,
        description: String,
        communityId: Long,
    ): Result<CommunityPost> =
        respond {
            val created =
                posts.first().copy(
                    id = posts.maxOf { it.id } + 1,
                    title = title,
                    preview = description,
                    communityId = communityId,
                    likes = 0,
                    comments = 0,
                )
            posts.add(0, created)
            created
        }

    override suspend fun deletePost(id: Long): Result<Unit> = respond { if (!posts.removeIf { it.id == id }) throw ApiError.NotFound }

    override suspend fun togglePostLike(id: Long): Result<LikeStatus> {
        likeCount++
        return respond {
            val index = posts.indexOfFirst { it.id == id }
            if (index < 0) throw ApiError.NotFound
            val post = posts[index]
            val liked = !post.isLiked
            val updated = post.copy(isLiked = liked, likes = post.likes + if (liked) 1 else -1)
            posts[index] = updated
            LikeStatus(isLiked = updated.isLiked, likes = updated.likes)
        }
    }

    override suspend fun addComment(
        postId: Long,
        comment: String,
    ): Result<PostComment> {
        sendCommentCount++
        return respond { newComment(comment).also { comments.add(it) } }
    }

    override suspend fun replyToComment(
        postId: Long,
        commentId: Long,
        comment: String,
    ): Result<PostComment> {
        sendCommentCount++
        return respond {
            if (comment(commentId) == null) throw ApiError.NotFound
            newComment(comment).also { replaceComments(comments.addingReply(commentId, it)) }
        }
    }

    override suspend fun toggleCommentLike(
        postId: Long,
        commentId: Long,
    ): Result<LikeStatus> {
        commentLikeCount++
        return respond {
            val current = comment(commentId) ?: throw ApiError.NotFound
            val liked = !current.isLiked
            val status = LikeStatus(isLiked = liked, likes = current.likes + if (liked) 1 else -1)
            replaceComments(comments.replacingComment(commentId) { it.copy(isLiked = status.isLiked, likes = status.likes) })
            status
        }
    }

    fun isJoined(communityId: Long): Boolean = communityId in joinedIds

    fun post(id: Long): CommunityPost? = posts.firstOrNull { it.id == id }

    fun comment(id: Long): PostComment? = comments.findComment(id)

    private fun newComment(content: String): PostComment {
        val nextId = (comments.flatMap { listOf(it) + it.replies }.maxOfOrNull { it.id } ?: 0) + 1
        return CommunityMockData.comments.first().copy(
            id = nextId,
            author = "@you",
            timeAgo = "now",
            content = content,
            likes = 0,
            isLiked = false,
            replies = emptyList(),
        )
    }

    private fun replaceComments(updated: List<PostComment>) {
        comments.clear()
        comments.addAll(updated)
    }

    private suspend fun <T> respond(block: () -> T): Result<T> {
        val error = failure
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return error?.let { Result.failure(it) } ?: runCatching(block)
    }
}
