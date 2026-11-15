package com.lucasdias.gametrackr.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResponse<T>(
    val data: List<T>,
    @SerialName("current_page") val currentPage: Int,
    @SerialName("last_page") val lastPage: Int,
    @SerialName("per_page") val perPage: Int,
    val total: Int,
)

@Serializable
data class CommunityDto(
    val id: Int,
    val title: String,
    val slug: String,
    val description: String? = null,
    @SerialName("author_id") val authorId: Int,
    val author: CommunityUserDto? = null,
    val members: List<CommunityUserDto>? = null,
    val media: List<MediaDto>? = null,
    @SerialName("is_member") val isMember: Boolean? = null,
    @SerialName("created_at") val createdAt: String? = null,
)

@Serializable
data class PostDto(
    val id: Int,
    val title: String,
    val slug: String,
    val description: String? = null,
    @SerialName("community_id") val communityId: Int,
    @SerialName("author_id") val authorId: Int,
    val likes: Int = 0,
    @SerialName("is_liked") val isLiked: Boolean? = null,
    val author: CommunityUserDto? = null,
    val community: CommunityDto? = null,
    val media: List<MediaDto>? = null,
    val comments: List<CommentDto>? = null,
    @SerialName("created_at") val createdAt: String? = null,
)

@Serializable
data class CommentDto(
    val id: Int,
    val content: String,
    @SerialName("author_id") val authorId: Int,
    @SerialName("post_id") val postId: Int,
    @SerialName("parent_id") val parentId: Int? = null,
    val likes: Int = 0,
    @SerialName("is_liked") val isLiked: Boolean? = null,
    val author: CommunityUserDto? = null,
    @SerialName("created_at") val createdAt: String? = null,
)

@Serializable
data class CommunityUserDto(
    val id: Int,
    val name: String,
    val email: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    val username: String? = null,
)

@Serializable
data class MediaDto(
    val id: Int? = null,
    @SerialName("original_url") val originalUrl: String,
    @SerialName("collection_name") val collectionName: String? = null,
)

@Serializable
data class LikeResponse(
    val message: String,
    @SerialName("is_liked") val isLiked: Boolean,
    val likes: Int,
)

@Serializable
data class CommentResponse(
    val message: String,
    val comment: CommentDto,
)

@Serializable
data class ReplyResponse(
    val message: String,
    val reply: CommentDto,
)

@Serializable
data class CreateCommunityRequest(
    val title: String,
    val description: String,
)

@Serializable
data class CreateCommunityResponse(
    val message: String,
    val community: CommunityDto,
)

@Serializable
data class CreatePostRequest(
    val title: String,
    val description: String?,
    @SerialName("community_id") val communityId: Long,
)

@Serializable
data class CreatePostResponse(
    val message: String,
    val post: PostDto,
)

@Serializable
data class CommentBody(
    val comment: String,
)
