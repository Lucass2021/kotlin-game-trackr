package com.lucasdias.gametrackr.feature.app.search

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

enum class GamePlatform(
    val label: String,
) {
    PC("PC"),
    PLAYSTATION("PlayStation"),
    XBOX("Xbox"),
    NINTENDO("Nintendo"),
}

data class SearchGame(
    val title: String,
    val year: String,
    val platformsLabel: String,
    val platforms: Set<GamePlatform>,
    val coverStart: Color,
    val coverEnd: Color,
)

object SearchMockData {
    val games =
        listOf(
            SearchGame(
                title = "Cyber-Drift 2099",
                year = "2023",
                platformsLabel = "PS5, PC",
                platforms = setOf(GamePlatform.PLAYSTATION, GamePlatform.PC),
                coverStart = CoverVioletStart,
                coverEnd = CoverVioletEnd,
            ),
            SearchGame(
                title = "Aether Reach",
                year = "2024",
                platformsLabel = "PC, Xbox",
                platforms = setOf(GamePlatform.PC, GamePlatform.XBOX),
                coverStart = CoverCyanStart,
                coverEnd = CoverCyanEnd,
            ),
            SearchGame(
                title = "Soul Ember",
                year = "2023",
                platformsLabel = "PS5",
                platforms = setOf(GamePlatform.PLAYSTATION),
                coverStart = CoverCrimsonStart,
                coverEnd = CoverCrimsonEnd,
            ),
            SearchGame(
                title = "Tactical Grid",
                year = "2024",
                platformsLabel = "PC",
                platforms = setOf(GamePlatform.PC),
                coverStart = CoverPineStart,
                coverEnd = CoverPineEnd,
            ),
            SearchGame(
                title = "V-Velocity",
                year = "2023",
                platformsLabel = "PS5, Switch",
                platforms = setOf(GamePlatform.PLAYSTATION, GamePlatform.NINTENDO),
                coverStart = CoverEmeraldStart,
                coverEnd = CoverEmeraldEnd,
            ),
            SearchGame(
                title = "Frost Bound",
                year = "2024",
                platformsLabel = "PC, Xbox",
                platforms = setOf(GamePlatform.PC, GamePlatform.XBOX),
                coverStart = CoverAzureStart,
                coverEnd = CoverAzureEnd,
            ),
            SearchGame(
                title = "Astral Vanguard",
                year = "2024",
                platformsLabel = "PS5, PC, Xbox",
                platforms = setOf(GamePlatform.PLAYSTATION, GamePlatform.PC, GamePlatform.XBOX),
                coverStart = CoverIndigoStart,
                coverEnd = CoverIndigoEnd,
            ),
            SearchGame(
                title = "Iron Requiem",
                year = "2022",
                platformsLabel = "PC, Switch",
                platforms = setOf(GamePlatform.PC, GamePlatform.NINTENDO),
                coverStart = CoverPineStart,
                coverEnd = CoverPineEnd,
            ),
        )
}
