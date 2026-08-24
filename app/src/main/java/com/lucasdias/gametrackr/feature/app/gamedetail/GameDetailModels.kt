package com.lucasdias.gametrackr.feature.app.gamedetail

import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.core.model.PlatformLabel
import com.lucasdias.gametrackr.core.network.dto.GameDetailDto
import com.lucasdias.gametrackr.core.network.dto.GameNamedDto
import com.lucasdias.gametrackr.core.network.dto.GradientPalette
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

data class GameScreenshot(
    val start: Color,
    val end: Color,
    val url: String? = null,
)

data class GameSpec(
    val label: String,
    val value: String,
)

data class GameDetail(
    val title: String,
    val year: String,
    val rating: Double?,
    val platforms: List<String>,
    val genres: List<String>,
    val coverStart: Color,
    val coverEnd: Color,
    val heroUrl: String?,
    val screenshots: List<GameScreenshot>,
    val about: String,
    val specs: List<GameSpec>,
)

fun GameDetailDto.toDomain(): GameDetail {
    val palette = GradientPalette.pair(id)
    val releaseDate = firstReleaseDate?.let { Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).toLocalDate() }

    return GameDetail(
        title = name,
        year = releaseDate?.year?.toString() ?: TBA,
        rating = (totalRating ?: aggregatedRating ?: rating)?.div(RATING_SCALE),
        platforms = platforms.orEmpty().mapNotNull { it.abbreviation ?: PlatformLabel.short(it.slug, it.name) },
        genres = genres.names() + themes.names(),
        coverStart = palette.first,
        coverEnd = palette.second,
        heroUrl = screenshots?.firstOrNull()?.url ?: artworks?.firstOrNull()?.url ?: cover?.url,
        screenshots = screenshots.orEmpty().map { GameScreenshot(palette.first, palette.second, it.url) },
        about = summary ?: storyline ?: NO_DESCRIPTION,
        specs = specs(releaseDate?.format(RELEASE_FORMATTER)),
    )
}

private fun List<GameNamedDto>?.names(): List<String> = orEmpty().mapNotNull { it.name }

private fun GameDetailDto.specs(formattedRelease: String?): List<GameSpec> {
    val companies = involvedCompanies.orEmpty()

    return listOfNotNull(
        companies
            .firstOrNull { it.developer }
            ?.company
            ?.name
            ?.let { GameSpec("Developer", it) },
        companies
            .firstOrNull { it.publisher }
            ?.company
            ?.name
            ?.let { GameSpec("Publisher", it) },
        (releaseDates?.firstOrNull()?.human ?: formattedRelease)?.let { GameSpec("Released", it) },
        gameEngines.names().firstOrNull()?.let { GameSpec("Engine", it) },
        gameModes
            .names()
            .joinToString(", ")
            .ifEmpty { null }
            ?.let { GameSpec("Modes", it) },
        playerPerspectives
            .names()
            .joinToString(", ")
            .ifEmpty { null }
            ?.let { GameSpec("Perspective", it) },
    )
}

private const val TBA = "TBA"
private const val RATING_SCALE = 10
private const val NO_DESCRIPTION = "No description available yet."
private val RELEASE_FORMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
