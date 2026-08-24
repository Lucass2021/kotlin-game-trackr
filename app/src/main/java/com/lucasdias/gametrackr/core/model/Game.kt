package com.lucasdias.gametrackr.core.model

import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.core.network.dto.GradientPalette
import java.time.LocalDate

enum class GamePlatform(
    val label: String,
    val igdbSlugs: List<String>,
) {
    PC("PC", listOf("win", "linux", "mac", "dos", "browser")),
    PLAYSTATION("PlayStation", listOf("ps", "ps2", "ps3", "ps4--1", "ps5", "psp", "psvita", "psvr", "psvr2")),
    XBOX("Xbox", listOf("xbox", "xbox360", "xboxone", "series-x-s")),
    NINTENDO(
        "Nintendo",
        listOf("nes", "snes", "n64", "ngc", "wii", "wiiu", "gb", "gbc", "gba", "nds", "3ds", "switch", "switch-2"),
    ),
}

object PlatformLabel {
    private val abbreviations =
        mapOf(
            "win" to "PC",
            "linux" to "Linux",
            "mac" to "Mac",
            "dos" to "DOS",
            "browser" to "Web",
            "ps5" to "PS5",
            "ps4--1" to "PS4",
            "ps3" to "PS3",
            "ps2" to "PS2",
            "psvita" to "Vita",
            "psp" to "PSP",
            "series-x" to "Xbox Series",
            "series-x-s" to "Xbox Series",
            "xboxone" to "Xbox One",
            "xbox360" to "Xbox 360",
            "switch" to "Switch",
            "switch-2" to "Switch 2",
            "ios" to "iOS",
            "android" to "Android",
        )

    fun short(
        slug: String?,
        name: String?,
    ): String? = abbreviations[slug] ?: name?.substringBefore(" (")
}

data class Game(
    val id: Int,
    val name: String,
    val slug: String = "",
    val summary: String? = null,
    val releaseDate: LocalDate? = null,
    val rating: Double? = null,
    val coverUrl: String? = null,
    val platformNames: List<String> = emptyList(),
) {
    private val colors: Pair<Color, Color> = GradientPalette.pair(id)

    val coverStart: Color get() = colors.first
    val coverEnd: Color get() = colors.second

    val year: String get() = releaseDate?.year?.toString() ?: TBA

    val platformsLabel: String
        get() {
            if (platformNames.isEmpty()) return PLATFORM_TBA
            val visible = platformNames.take(VISIBLE_PLATFORMS).joinToString(", ")
            val remaining = platformNames.size - VISIBLE_PLATFORMS
            return if (remaining > 0) "$visible +$remaining" else visible
        }

    private companion object {
        const val TBA = "TBA"
        const val PLATFORM_TBA = "Platform TBA"
        const val VISIBLE_PLATFORMS = 3
    }
}
