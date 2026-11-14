package com.lucasdias.gametrackr.core.network.dto

import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityMember
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.model.CoverPalette
import com.lucasdias.gametrackr.core.model.PostComment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.TimeZone

private const val MEDIA_PALETTE_OFFSET = 3

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

fun CommunityDto.toDomain(): Community {
    val avatarMedia = media?.firstOrNull { it.collectionName == "avatar" }?.originalUrl
    val coverMedia = media?.firstOrNull { it.collectionName == "cover" }?.originalUrl
    val palette = CoverPalette.forGradientId(id)
    val memberCount = members?.size ?: 0
    return Community(
        id = id.toLong(),
        name = title,
        authorId = authorId,
        category = "General",
        members = memberCount.abbreviatedCompact(),
        posts = "0",
        online = "--",
        description = description.orEmpty(),
        isJoined = isMember ?: false,
        icon = palette,
        createdMonth = formatMonthYear(createdAt),
    )
}

fun PostDto.toDomain(): CommunityPost {
    val firstImage = media?.firstOrNull()?.originalUrl
    val avatar = CoverPalette.forGradientId(id)
    val media = CoverPalette.forGradientId(id + MEDIA_PALETTE_OFFSET)
    return CommunityPost(
        id = id.toLong(),
        author = "@${author?.name ?: "Unknown"}",
        authorId = authorId,
        authorUsername = author?.username,
        timeAgo = formatTimeAgo(createdAt),
        communityName = community?.title.orEmpty(),
        communityId = communityId.toLong(),
        title = title,
        preview = description.orEmpty(),
        likes = likes,
        comments = comments?.size ?: 0,
        hasMedia = firstImage != null,
        isLiked = isLiked ?: false,
        avatar = avatar,
        media = media,
    )
}

fun CommentDto.toDomain(): PostComment {
    val palette = CoverPalette.forGradientId(id)
    return PostComment(
        id = id.toLong(),
        author = "@${author?.name ?: "Unknown"}",
        authorId = authorId,
        timeAgo = formatTimeAgoShort(createdAt),
        content = content,
        likes = likes,
        isLiked = isLiked ?: false,
        avatar = palette,
    )
}

fun CommunityUserDto.toDomain(): CommunityMember {
    val palette = CoverPalette.forGradientId(id)
    return CommunityMember(
        id = id.toLong(),
        author = "@$name",
        role = "Member",
        avatar = palette,
        username = username,
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

private fun formatMonthYear(iso: String?): String? {
    val day = iso?.take(ISO_DATE_LENGTH) ?: return null
    val parser =
        SimpleDateFormat(ISO_DATE_PATTERN, Locale.ROOT).apply {
            timeZone = TimeZone.getTimeZone(UTC)
            isLenient = false
        }
    val date = runCatching { parser.parse(day) }.getOrNull() ?: return null
    return SimpleDateFormat(MONTH_YEAR_PATTERN, Locale.ENGLISH).apply { timeZone = TimeZone.getTimeZone(UTC) }.format(date)
}

private const val ISO_DATE_LENGTH = 10
private const val ISO_DATE_PATTERN = "yyyy-MM-dd"
private const val MONTH_YEAR_PATTERN = "MMMM yyyy"
private const val UTC = "UTC"
