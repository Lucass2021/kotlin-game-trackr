package com.lucasdias.gametrackr.core.network.dto

import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityMember
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.model.CoverPalette
import com.lucasdias.gametrackr.core.model.PostComment
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.TimeZone

private const val MEDIA_PALETTE_OFFSET = 3

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
