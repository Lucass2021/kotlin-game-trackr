package com.lucasdias.gametrackr.feature.app.gamedetail

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
            discussions =
                listOf(
                    GameDiscussion(
                        author = "Kaelen_Vox",
                        timeAgo = "2h ago",
                        title = "Best builds for early game?",
                        preview = "Just started my playthrough on PS5. Should I focus on the tech tree or go full stealth?",
                        comments = 24,
                        likes = 142,
                        avatarStart = CoverPineStart,
                        avatarEnd = CoverPineEnd,
                    ),
                    GameDiscussion(
                        author = "CyberDrifter_99",
                        timeAgo = "5h ago",
                        title = "The level design is insane!",
                        preview = "The verticality in the Upper District is unlike anything I've seen in an open world.",
                        comments = 86,
                        likes = 856,
                        avatarStart = CoverAzureStart,
                        avatarEnd = CoverAzureEnd,
                    ),
                ),
            specs =
                listOf(
                    GameSpec("Developer", "Void Interactive"),
                    GameSpec("Publisher", "Nova Games"),
                    GameSpec("Storage", "85.4 GB"),
                    GameSpec("Language", "English, +12 more"),
                ),
            systemRequirements =
                listOf(
                    SystemRequirementTier(
                        name = "Minimum",
                        items =
                            listOf(
                                GameSpec("OS", "Windows 10 64-bit"),
                                GameSpec("CPU", "Intel Core i5-8400"),
                                GameSpec("GPU", "NVIDIA GTX 1060 6GB"),
                                GameSpec("Memory", "12 GB RAM"),
                            ),
                    ),
                    SystemRequirementTier(
                        name = "Recommended",
                        items =
                            listOf(
                                GameSpec("OS", "Windows 11 64-bit"),
                                GameSpec("CPU", "Intel Core i7-12700K"),
                                GameSpec("GPU", "NVIDIA RTX 3070 8GB"),
                                GameSpec("Memory", "16 GB RAM"),
                            ),
                    ),
                ),
        )
}
