package com.lucasdias.gametrackr.core.network.dto

import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.core.ui.theme.CoverAzureEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverAzureStart
import com.lucasdias.gametrackr.core.ui.theme.CoverCrimsonEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverCrimsonStart
import com.lucasdias.gametrackr.core.ui.theme.CoverCyanEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverCyanStart
import com.lucasdias.gametrackr.core.ui.theme.CoverEmeraldEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverEmeraldStart
import com.lucasdias.gametrackr.core.ui.theme.CoverIndigoEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverIndigoStart
import com.lucasdias.gametrackr.core.ui.theme.CoverPineEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverPineStart
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletStart
import com.lucasdias.gametrackr.feature.app.community.Community
import com.lucasdias.gametrackr.feature.app.community.CommunityMember
import com.lucasdias.gametrackr.feature.app.community.CommunityPost
import com.lucasdias.gametrackr.feature.app.community.PostComment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.abs

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

object GradientPalette {
    private val pairs: List<Pair<Color, Color>> =
        listOf(
            CoverEmeraldStart to CoverEmeraldEnd,
            CoverCrimsonStart to CoverCrimsonEnd,
            CoverIndigoStart to CoverIndigoEnd,
            CoverAzureStart to CoverAzureEnd,
            CoverVioletStart to CoverVioletEnd,
            CoverCyanStart to CoverCyanEnd,
            CoverPineStart to CoverPineEnd,
        )

    fun pair(id: Int): Pair<Color, Color> = pairs[abs(id) % pairs.size]
}

fun CommunityDto.toDomain(): Community {
    val avatarMedia = media?.firstOrNull { it.collectionName == "avatar" }?.originalUrl
    val coverMedia = media?.firstOrNull { it.collectionName == "cover" }?.originalUrl
    val colors = GradientPalette.pair(id)
    val memberCount = members?.size ?: 0
    return Community(
        id = id.toLong(),
        name = title,
        category = "General",
        members = memberCount.abbreviatedCompact(),
        posts = "0",
        online = "--",
        description = description.orEmpty(),
        isJoined = isMember ?: false,
        iconStart = colors.first,
        iconEnd = colors.second,
    )
}

fun PostDto.toDomain(): CommunityPost {
    val firstImage = media?.firstOrNull()?.originalUrl
    val avatarColors = GradientPalette.pair(id)
    val mediaColors = GradientPalette.pair(id + 3)
    return CommunityPost(
        id = id.toLong(),
        author = "@${author?.name ?: "Unknown"}",
        timeAgo = formatTimeAgo(createdAt),
        communityName = community?.title.orEmpty(),
        title = title,
        preview = description.orEmpty(),
        likes = likes,
        comments = comments?.size ?: 0,
        hasMedia = firstImage != null,
        isLiked = isLiked ?: false,
        avatarStart = avatarColors.first,
        avatarEnd = avatarColors.second,
        mediaStart = mediaColors.first,
        mediaEnd = mediaColors.second,
    )
}

fun CommentDto.toDomain(): PostComment {
    val colors = GradientPalette.pair(id)
    return PostComment(
        id = id.toLong(),
        author = "@${author?.name ?: "Unknown"}",
        timeAgo = formatTimeAgoShort(createdAt),
        content = content,
        likes = likes,
        isLiked = isLiked ?: false,
        avatarStart = colors.first,
        avatarEnd = colors.second,
    )
}

fun CommunityUserDto.toDomain(): CommunityMember {
    val colors = GradientPalette.pair(id)
    return CommunityMember(
        id = id.toLong(),
        author = "@$name",
        role = "Member",
        avatarStart = colors.first,
        avatarEnd = colors.second,
    )
}

private fun formatTimeAgo(iso: String?): String {
    if (iso == null) return ""
    val instant = parseIso(iso) ?: return ""
    val now = Instant.now()
    val seconds = ChronoUnit.SECONDS.between(instant, now)
    return when {
        seconds < 60 -> {
            "now"
        }

        seconds < 3600 -> {
            "${seconds / 60}m ago"
        }

        seconds < 86400 -> {
            "${seconds / 3600}h ago"
        }

        seconds < 604800 -> {
            "${seconds / 86400}d ago"
        }

        else -> {
            val zoned = instant.atZone(java.time.ZoneId.systemDefault())
            DateTimeFormatter.ofPattern("MMM d").format(zoned)
        }
    }
}

private fun formatTimeAgoShort(iso: String?): String {
    if (iso == null) return ""
    val instant = parseIso(iso) ?: return ""
    val now = Instant.now()
    val seconds = ChronoUnit.SECONDS.between(instant, now)
    return when {
        seconds < 60 -> "now"
        seconds < 3600 -> "${seconds / 60}m"
        seconds < 86400 -> "${seconds / 3600}h"
        else -> "${seconds / 86400}d"
    }
}

private fun parseIso(value: String): Instant? =
    runCatching { ZonedDateTime.parse(value).toInstant() }
        .recoverCatching { Instant.parse(value) }
        .getOrNull()

private fun Int.abbreviatedCompact(): String {
    if (this < 1000) return toString()
    val thousands = this / 1000.0
    return "%.1fk".format(thousands).replace(".0k", "k")
}
