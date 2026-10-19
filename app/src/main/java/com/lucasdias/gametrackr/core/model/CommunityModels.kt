package com.lucasdias.gametrackr.core.model

import androidx.compose.ui.graphics.Color

data class Community(
    val id: Long,
    val name: String,
    val authorId: Int? = null,
    val category: String,
    val members: String,
    val posts: String,
    val online: String,
    val description: String,
    val isJoined: Boolean,
    val iconStart: Color,
    val iconEnd: Color,
    val createdMonth: String? = null,
) {
    val subtitle: String get() = "#$category · $members members"
    val detailSubtitle: String get() = "$members members · $posts posts · #$category"
}

data class CommunityPost(
    val id: Long,
    val author: String,
    val authorId: Int? = null,
    val timeAgo: String,
    val communityName: String,
    val communityId: Long? = null,
    val title: String,
    val preview: String,
    val likes: Int,
    val comments: Int,
    val hasMedia: Boolean,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val avatarStart: Color,
    val avatarEnd: Color,
    val mediaStart: Color,
    val mediaEnd: Color,
)

data class CommunityMember(
    val id: Long,
    val author: String,
    val role: String,
    val avatarStart: Color,
    val avatarEnd: Color,
)

data class PostComment(
    val id: Long,
    val author: String,
    val authorId: Int? = null,
    val timeAgo: String,
    val content: String,
    val likes: Int,
    val isLiked: Boolean = false,
    val avatarStart: Color,
    val avatarEnd: Color,
    val replies: List<PostComment> = emptyList(),
)

fun List<PostComment>.totalCount(): Int = sumOf { 1 + it.replies.size }

data class LikeStatus(
    val isLiked: Boolean,
    val likes: Int,
)
