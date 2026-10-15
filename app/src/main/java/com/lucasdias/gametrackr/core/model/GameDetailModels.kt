package com.lucasdias.gametrackr.core.model

import androidx.compose.ui.graphics.Color
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
