package com.lucasdias.gametrackr.core.network

import com.lucasdias.gametrackr.core.network.dto.CommentBody
import com.lucasdias.gametrackr.core.network.dto.CommentResponse
import com.lucasdias.gametrackr.core.network.dto.CommunityDto
import com.lucasdias.gametrackr.core.network.dto.CreatePostRequest
import com.lucasdias.gametrackr.core.network.dto.CreatePostResponse
import com.lucasdias.gametrackr.core.network.dto.LikeResponse
import com.lucasdias.gametrackr.core.network.dto.MessageResponse
import com.lucasdias.gametrackr.core.network.dto.PaginatedResponse
import com.lucasdias.gametrackr.core.network.dto.PostDto
import com.lucasdias.gametrackr.core.network.dto.ReplyResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommunityApi {
    @GET("communities")
    suspend fun getCommunities(
        @Query("search") search: String? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("page") page: Int? = null,
    ): PaginatedResponse<CommunityDto>

    @GET("communities/joined")
    suspend fun getJoinedCommunities(): PaginatedResponse<CommunityDto>

    @GET("communities/{id}")
    suspend fun getCommunity(
        @Path("id") id: Long,
    ): CommunityDto

    @POST("communities/join/{id}")
    suspend fun joinCommunity(
        @Path("id") id: Long,
    ): MessageResponse

    @POST("communities/leave/{id}")
    suspend fun leaveCommunity(
        @Path("id") id: Long,
    ): MessageResponse

    @GET("posts")
    suspend fun getPosts(
        @Query("search") search: String? = null,
        @Query("community_id") communityId: Long? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("page") page: Int? = null,
    ): PaginatedResponse<PostDto>

    @GET("posts/{id}")
    suspend fun getPost(
        @Path("id") id: Long,
    ): PostDto

    @POST("posts")
    suspend fun createPost(
        @Body body: CreatePostRequest,
    ): CreatePostResponse

    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Long,
    ): MessageResponse

    @POST("posts/{id}/like")
    suspend fun toggleLike(
        @Path("id") id: Long,
    ): LikeResponse

    @POST("posts/{id}/comment")
    suspend fun addComment(
        @Path("id") postId: Long,
        @Body body: CommentBody,
    ): CommentResponse

    @POST("posts/{postId}/comment/{commentId}/reply")
    suspend fun replyToComment(
        @Path("postId") postId: Long,
        @Path("commentId") commentId: Long,
        @Body body: CommentBody,
    ): ReplyResponse

    @POST("posts/{postId}/comment/{commentId}/like")
    suspend fun toggleCommentLike(
        @Path("postId") postId: Long,
        @Path("commentId") commentId: Long,
    ): LikeResponse
}
