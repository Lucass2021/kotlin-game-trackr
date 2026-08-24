package com.lucasdias.gametrackr.feature.app.gamedetail

import com.lucasdias.gametrackr.core.ui.theme.CoverCrimsonEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverCrimsonStart
import com.lucasdias.gametrackr.core.ui.theme.CoverCyanEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverCyanStart
import com.lucasdias.gametrackr.core.ui.theme.CoverEmeraldEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverEmeraldStart
import com.lucasdias.gametrackr.core.ui.theme.CoverIndigoEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverIndigoStart
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletStart

object GameDetailMockData {
    val game =
        GameDetail(
            title = "Neon Ascent: Revival",
            year = "2024",
            rating = 4.8,
            platforms = listOf("PS5", "PC"),
            genres = listOf("Action RPG", "Open World", "Cyberpunk"),
            coverStart = CoverVioletStart,
            coverEnd = CoverVioletEnd,
            heroUrl = null,
            screenshots =
                listOf(
                    GameScreenshot(CoverIndigoStart, CoverIndigoEnd),
                    GameScreenshot(CoverCyanStart, CoverCyanEnd),
                    GameScreenshot(CoverCrimsonStart, CoverCrimsonEnd),
                    GameScreenshot(CoverEmeraldStart, CoverEmeraldEnd),
                ),
            about =
                "Neon Ascent: Revival is the definitive next-gen cyberpunk experience. Set in " +
                    "the sprawling vertical megacity of Aethelgard, players take on the role of a " +
                    "rogue netrunner untangling a conspiracy that reaches from the neon-drenched " +
                    "undercity to the corporate spires above.",
            specs =
                listOf(
                    GameSpec("Developer", "Void Interactive"),
                    GameSpec("Publisher", "Nova Games"),
                    GameSpec("Released", "Mar 14, 2024"),
                    GameSpec("Modes", "Single player, Co-operative"),
                ),
        )
}
