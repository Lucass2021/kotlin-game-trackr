package com.lucasdias.gametrackr.data.community

import com.lucasdias.gametrackr.core.network.CommunityApi
import com.lucasdias.gametrackr.core.network.apiCall
import com.lucasdias.gametrackr.core.network.dto.CommentBody
import com.lucasdias.gametrackr.core.network.dto.CommentDto
import com.lucasdias.gametrackr.core.network.dto.CreateCommunityRequest
import com.lucasdias.gametrackr.core.network.dto.CreatePostRequest
import com.lucasdias.gametrackr.core.network.dto.LikeResponse
import com.lucasdias.gametrackr.core.network.dto.PaginatedResponse
import com.lucasdias.gametrackr.core.network.dto.toDomain
import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.feature.app.community.Community
import com.lucasdias.gametrackr.feature.app.community.CommunityMember
import com.lucasdias.gametrackr.feature.app.community.CommunityPost
import com.lucasdias.gametrackr.feature.app.community.LikeStatus
import com.lucasdias.gametrackr.feature.app.community.PostComment
import kotlinx.serialization.json.Json

class RemoteCommunityRepository(
    private val api: CommunityApi,
    private val json: Json,
) : CommunityRepository {
    override suspend fun fetchCommunities(
        page: Int,
        perPage: Int,
        search: String?,
    ): Result<Page<Community>> = apiCall(json) { api.getCommunities(search, perPage, page).toPage { it.toDomain() } }

    override suspend fun fetchJoinedCommunities(): Result<List<Community>> = apiCall(json) { api.getJoinedCommunities().data.map { it.toDomain() } }

    override suspend fun fetchCommunityMembers(id: Long): Result<List<CommunityMember>> =
        apiCall(json) {
            api
                .getCommunity(id)
                .members
                .orEmpty()
                .map { it.toDomain() }
        }

    override suspend fun createCommunity(
        title: String,
        description: String,
    ): Result<Community> = apiCall(json) { api.createCommunity(CreateCommunityRequest(title, description)).community.toDomain() }

    override suspend fun deleteCommunity(id: Long): Result<Unit> = apiCall(json) { api.deleteCommunity(id) }.map { }

    override suspend fun joinCommunity(id: Long): Result<Unit> = apiCall(json) { api.joinCommunity(id) }.map { }

    override suspend fun leaveCommunity(id: Long): Result<Unit> = apiCall(json) { api.leaveCommunity(id) }.map { }

    override suspend fun fetchPosts(
        page: Int,
        perPage: Int,
        communityId: Long?,
    ): Result<Page<CommunityPost>> = apiCall(json) { api.getPosts(communityId = communityId, perPage = perPage, page = page).toPage { it.toDomain() } }

    override suspend fun fetchPostComments(postId: Long): Result<List<PostComment>> =
        apiCall(json) {
            api
                .getPost(postId)
                .comments
                .orEmpty()
                .toThreads()
        }

    override suspend fun createPost(
        title: String,
        description: String,
        communityId: Long,
    ): Result<CommunityPost> =
        apiCall(json) {
            val created = api.createPost(CreatePostRequest(title, description, communityId)).post
            api.getPost(created.id.toLong()).toDomain()
        }

    override suspend fun deletePost(id: Long): Result<Unit> = apiCall(json) { api.deletePost(id) }.map { }

    override suspend fun togglePostLike(id: Long): Result<LikeStatus> = apiCall(json) { api.toggleLike(id).toLikeStatus() }

    override suspend fun addComment(
        postId: Long,
        comment: String,
    ): Result<PostComment> = apiCall(json) { api.addComment(postId, CommentBody(comment)).comment.toDomain() }

    override suspend fun replyToComment(
        postId: Long,
        commentId: Long,
        comment: String,
    ): Result<PostComment> = apiCall(json) { api.replyToComment(postId, commentId, CommentBody(comment)).reply.toDomain() }

    override suspend fun toggleCommentLike(
        postId: Long,
        commentId: Long,
    ): Result<LikeStatus> = apiCall(json) { api.toggleCommentLike(postId, commentId).toLikeStatus() }

    private fun <Dto, Item> PaginatedResponse<Dto>.toPage(transform: (Dto) -> Item): Page<Item> =
        Page(
            items = data.map(transform),
            currentPage = currentPage,
            lastPage = lastPage,
            total = total,
        )

    private fun LikeResponse.toLikeStatus() = LikeStatus(isLiked = isLiked, likes = likes)

    private fun List<CommentDto>.toThreads(): List<PostComment> {
        val byId = associateBy { it.id }

        fun rootId(comment: CommentDto): Int {
            var current = comment
            while (true) {
                current = current.parentId?.let(byId::get) ?: return current.id
            }
        }

        val replies = filter { it.parentId != null }.groupBy { rootId(it) }
        return filter { it.parentId == null }.map { comment ->
            comment.toDomain().copy(replies = replies[comment.id].orEmpty().map { it.toDomain() })
        }
    }
}
