package com.lucasdias.gametrackr.feature.app.community

import androidx.compose.ui.graphics.Color

enum class CommunitySegment(
    val label: String,
) {
    MY_FEED("My Feed"),
    DISCOVER("Discover"),
}

enum class CommunityDetailTab(
    val label: String,
) {
    POSTS("Posts"),
    ABOUT("About"),
    MEMBERS("Members"),
}

data class Community(
    val id: Long,
    val name: String,
    val category: String,
    val members: String,
    val posts: String,
    val online: String,
    val description: String,
    val isJoined: Boolean,
    val iconStart: Color,
    val iconEnd: Color,
) {
    val subtitle: String get() = "#$category · $members members"
    val detailSubtitle: String get() = "$members members · $posts posts · #$category"
}

data class CommunityPost(
    val id: Long,
    val author: String,
    val timeAgo: String,
    val communityName: String,
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
    val timeAgo: String,
    val content: String,
    val likes: Int,
    val isLiked: Boolean = false,
    val avatarStart: Color,
    val avatarEnd: Color,
    val replies: List<PostComment> = emptyList(),
    val hiddenReplies: Int = 0,
)
