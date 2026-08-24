package com.lucasdias.gametrackr.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameImageDto(
    val id: Int? = null,
    val url: String? = null,
)

@Serializable
data class GameNamedDto(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
)

@Serializable
data class GameCompanyDto(
    val name: String? = null,
)

@Serializable
data class GameInvolvedCompanyDto(
    val company: GameCompanyDto? = null,
    val developer: Boolean = false,
    val publisher: Boolean = false,
)

@Serializable
data class GameReleaseDateDto(
    val date: Long? = null,
    val human: String? = null,
)

@Serializable
data class GameDetailPlatformDto(
    val name: String? = null,
    val slug: String? = null,
    val abbreviation: String? = null,
)

@Serializable
data class GameDetailDto(
    val id: Int,
    val name: String,
    val slug: String? = null,
    val summary: String? = null,
    val storyline: String? = null,
    @SerialName("first_release_date") val firstReleaseDate: Long? = null,
    @SerialName("total_rating") val totalRating: Double? = null,
    @SerialName("aggregated_rating") val aggregatedRating: Double? = null,
    val rating: Double? = null,
    val cover: GameImageDto? = null,
    val artworks: List<GameImageDto>? = null,
    val screenshots: List<GameImageDto>? = null,
    val platforms: List<GameDetailPlatformDto>? = null,
    val genres: List<GameNamedDto>? = null,
    val themes: List<GameNamedDto>? = null,
    @SerialName("game_modes") val gameModes: List<GameNamedDto>? = null,
    @SerialName("player_perspectives") val playerPerspectives: List<GameNamedDto>? = null,
    @SerialName("game_engines") val gameEngines: List<GameNamedDto>? = null,
    @SerialName("involved_companies") val involvedCompanies: List<GameInvolvedCompanyDto>? = null,
    @SerialName("release_dates") val releaseDates: List<GameReleaseDateDto>? = null,
)

@Serializable
data class GameDetailResponse(
    val message: String? = null,
    val data: GameDetailDto,
)
