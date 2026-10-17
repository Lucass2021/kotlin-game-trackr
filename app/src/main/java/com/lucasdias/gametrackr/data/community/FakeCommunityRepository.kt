package com.lucasdias.gametrackr.data.community

import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityMember
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.model.LikeStatus
import com.lucasdias.gametrackr.core.model.PostComment
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.core.pagination.fakePage
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeCommunityRepository(
    communities: List<Community> = CommunityMockData.all,
    posts: List<CommunityPost> = CommunityMockData.communityPosts,
    private val members: List<CommunityMember> = CommunityMockData.members,
    private val comments: List<PostComment> = CommunityMockData.comments,
    var failure: Throwable? = null,
) : CommunityRepository {
    private val communities = communities.toMutableList()
    private val posts = posts.toMutableList()
    private val joinedIds = communities.filter { it.isJoined }.map { it.id }.toMutableSet()

    var fetchPostsCount = 0
        private set
    var fetchMembersCount = 0
        private set
    var likeCount = 0
        private set
    var deleteCount = 0
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

    override suspend fun fetchJoinedCommunities(): Result<List<Community>> =
        respond { communities.filter { it.id in joinedIds }.map { it.copy(isJoined = true) } }

    override suspend fun fetchCommunityMembers(id: Long): Result<List<CommunityMember>> {
        fetchMembersCount++
        return respond { members }
    }

    override suspend fun createCommunity(
        title: String,
        description: String,
    ): Result<Community> =
        respond {
            val base = communities.first()
            val created =
                base.copy(id = (communities.maxOf { it.id } + 1), name = title, description = description, isJoined = true)
            communities.add(0, created)
            joinedIds.add(created.id)
            created
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

    override suspend fun fetchPostComments(postId: Long): Result<List<PostComment>> = respond { comments }

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
    ): Result<PostComment> = respond { comments.first().copy(content = comment, likes = 0, replies = emptyList()) }

    override suspend fun replyToComment(
        postId: Long,
        commentId: Long,
        comment: String,
    ): Result<PostComment> = respond { comments.first().copy(content = comment, likes = 0, replies = emptyList()) }

    override suspend fun toggleCommentLike(
        postId: Long,
        commentId: Long,
    ): Result<LikeStatus> = respond { LikeStatus(isLiked = true, likes = 1) }

    fun isJoined(communityId: Long): Boolean = communityId in joinedIds

    fun post(id: Long): CommunityPost? = posts.firstOrNull { it.id == id }

    private suspend fun <T> respond(block: () -> T): Result<T> {
        val error = failure
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return error?.let { Result.failure(it) } ?: runCatching(block)
    }
}
