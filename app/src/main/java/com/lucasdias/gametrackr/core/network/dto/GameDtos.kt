package com.lucasdias.gametrackr.core.network.dto

import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.model.GamePlatform
import com.lucasdias.gametrackr.core.model.PlatformLabel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId

@Serializable
data class GameCoverDto(
    val id: Int? = null,
    val url: String? = null,
)

@Serializable
data class GamePlatformDto(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
)

@Serializable
data class GameDto(
    val id: Int,
    val name: String,
    val slug: String? = null,
    val summary: String? = null,
    @SerialName("first_release_date") val firstReleaseDate: Long? = null,
    @SerialName("total_rating") val totalRating: Double? = null,
    val rating: Double? = null,
    val cover: GameCoverDto? = null,
    val platforms: List<GamePlatformDto>? = null,
)

@Serializable
data class PlatformsResponse(
    val message: String? = null,
    val data: List<GamePlatformDto>,
)

@Serializable
data class GamesResponse(
    val message: String? = null,
    val data: List<GameDto>,
)

@Serializable
data class GamesMeta(
    val page: Int,
    @SerialName("per_page") val perPage: Int,
    val total: Int,
    @SerialName("last_page") val lastPage: Int,
    @SerialName("has_more") val hasMore: Boolean,
)

@Serializable
data class PaginatedGamesResponse(
    val message: String? = null,
    val data: List<GameDto>,
    val meta: GamesMeta,
) {
    fun toPaginated(): PaginatedResponse<GameDto> =
        PaginatedResponse(
            data = data,
            currentPage = meta.page,
            lastPage = meta.lastPage,
            perPage = meta.perPage,
            total = meta.total,
        )
}

fun GameDto.toDomain(): Game {
    val dtoPlatforms = platforms.orEmpty()
    return Game(
        id = id,
        name = name,
        slug = slug.orEmpty(),
        summary = summary,
        releaseDate =
            firstReleaseDate?.let {
                Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).toLocalDate()
            },
        rating = totalRating ?: rating,
        coverUrl = cover?.url,
        platformNames = dtoPlatforms.mapNotNull { PlatformLabel.short(it.slug, it.name) }.distinct(),
    )
}

fun GamePlatformDto.toDomain(): GamePlatform? {
    val id = id ?: return null
    val slug = slug ?: return null
    val name = name ?: return null
    return GamePlatform(id = id, slug = slug, name = name)
}
