package com.lucasdias.gametrackr.data.game

import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.model.GamePlatform
import java.time.LocalDate

object HomeMockData {
    val sampleGame =
        Game(
            id = 1,
            name = "Final Fantasy VII Rebirth",
            releaseDate = LocalDate.of(2024, 3, 1),
            platformNames = listOf("PS5"),
        )

    val platforms =
        listOf(
            GamePlatform(id = 6, slug = "win", name = "PC (Microsoft Windows)"),
            GamePlatform(id = 167, slug = "ps5", name = "PlayStation 5"),
            GamePlatform(id = 169, slug = "series-x-s", name = "Xbox Series X|S"),
        )
}
