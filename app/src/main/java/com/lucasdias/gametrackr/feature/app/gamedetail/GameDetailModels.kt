package com.lucasdias.gametrackr.feature.app.gamedetail

import androidx.compose.ui.graphics.Color

data class GameScreenshot(
    val start: Color,
    val end: Color,
)

data class GameDiscussion(
    val author: String,
    val timeAgo: String,
    val title: String,
    val preview: String,
    val comments: Int,
    val likes: Int,
    val avatarStart: Color,
    val avatarEnd: Color,
)

data class GameSpec(
    val label: String,
    val value: String,
)

data class SystemRequirementTier(
    val name: String,
    val items: List<GameSpec>,
)

data class GameDetail(
    val title: String,
    val year: String,
    val rating: Double,
    val platforms: List<String>,
    val genres: List<String>,
    val coverStart: Color,
    val coverEnd: Color,
    val screenshots: List<GameScreenshot>,
    val about: String,
    val discussions: List<GameDiscussion>,
    val specs: List<GameSpec>,
    val systemRequirements: List<SystemRequirementTier>,
) {
    val supportsPC: Boolean
        get() = platforms.any { it.equals("PC", ignoreCase = true) }
}
