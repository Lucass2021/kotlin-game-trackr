package com.lucasdias.gametrackr.data.community

import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityMember
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.model.LikeStatus
import com.lucasdias.gametrackr.core.model.PostComment
import com.lucasdias.gametrackr.core.pagination.Page

interface CommunityRepository {
    suspend fun fetchCommunities(
        page: Int,
        perPage: Int,
        search: String?,
    ): Result<Page<Community>>

    suspend fun fetchJoinedCommunities(): Result<List<Community>>

    suspend fun fetchCommunityMembers(id: Long): Result<List<CommunityMember>>

    suspend fun createCommunity(
        title: String,
        description: String,
    ): Result<Community>

    suspend fun deleteCommunity(id: Long): Result<Unit>

    suspend fun joinCommunity(id: Long): Result<Unit>

    suspend fun leaveCommunity(id: Long): Result<Unit>

    suspend fun fetchPosts(
        page: Int,
        perPage: Int,
        communityId: Long?,
    ): Result<Page<CommunityPost>>

    suspend fun fetchPostComments(postId: Long): Result<List<PostComment>>

    suspend fun createPost(
        title: String,
        description: String,
        communityId: Long,
    ): Result<CommunityPost>

    suspend fun deletePost(id: Long): Result<Unit>

    suspend fun togglePostLike(id: Long): Result<LikeStatus>

    suspend fun addComment(
        postId: Long,
        comment: String,
    ): Result<PostComment>

    suspend fun replyToComment(
        postId: Long,
        commentId: Long,
        comment: String,
    ): Result<PostComment>

    suspend fun toggleCommentLike(
        postId: Long,
        commentId: Long,
    ): Result<LikeStatus>
}
