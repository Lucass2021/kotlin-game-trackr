package com.lucasdias.gametrackr.core.model

data class GameScreenshot(
    val cover: CoverPalette,
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
    val cover: CoverPalette,
    val heroUrl: String?,
    val screenshots: List<GameScreenshot>,
    val about: String,
    val specs: List<GameSpec>,
)
