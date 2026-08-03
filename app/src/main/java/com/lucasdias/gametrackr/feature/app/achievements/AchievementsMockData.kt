package com.lucasdias.gametrackr.feature.app.achievements

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
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletStart

object AchievementsMockData {
    val games =
        listOf(
            GameAchievements(
                id = 1,
                title = "Elden Ring",
                platform = "PlayStation 5",
                coverStart = CoverEmeraldStart,
                coverEnd = CoverEmeraldEnd,
                achievements =
                    listOf(
                        Achievement(101, "Elden Ring", "Obtain all achievements", AchievementTier.PLATINUM, "Jul 2024"),
                        Achievement(102, "Elden Lord", "Complete the game", AchievementTier.GOLD, "Jun 2024"),
                        Achievement(
                            103,
                            "Age of the Stars",
                            "Reach the Age of the Stars ending",
                            AchievementTier.GOLD,
                            "Jun 2024",
                        ),
                        Achievement(
                            104,
                            "Legendary Armaments",
                            "Collect all legendary armaments",
                            AchievementTier.GOLD,
                            "Jul 2024",
                        ),
                        Achievement(
                            105,
                            "Great Rune",
                            "Restore your first Great Rune",
                            AchievementTier.SILVER,
                            "Apr 2024",
                        ),
                        Achievement(
                            106,
                            "Godrick the Grafted",
                            "Defeat Godrick the Grafted",
                            AchievementTier.SILVER,
                            "Apr 2024",
                        ),
                        Achievement(
                            107,
                            "Malenia, Blade of Miquella",
                            "Defeat Malenia",
                            AchievementTier.SILVER,
                            "Jun 2024",
                        ),
                        Achievement(
                            108,
                            "Roundtable Hold",
                            "Arrive at Roundtable Hold",
                            AchievementTier.BRONZE,
                            "Mar 2024",
                        ),
                    ),
            ),
            GameAchievements(
                id = 2,
                title = "Hollow Knight",
                platform = "Nintendo Switch",
                coverStart = CoverIndigoStart,
                coverEnd = CoverIndigoEnd,
                achievements =
                    listOf(
                        Achievement(201, "Hollow Knight", "Obtain all achievements", AchievementTier.PLATINUM),
                        Achievement(202, "Dream No More", "Achieve the final ending", AchievementTier.GOLD),
                        Achievement(203, "Pure Completion", "Achieve 112% completion", AchievementTier.GOLD),
                        Achievement(204, "Blackegg", "Complete the game", AchievementTier.SILVER, "Feb 2024"),
                        Achievement(
                            205,
                            "Grimm Troupe",
                            "Complete the Grimm Troupe quest",
                            AchievementTier.SILVER,
                            "Feb 2024",
                        ),
                        Achievement(
                            206,
                            "Mantis Lords",
                            "Defeat the Mantis Lords",
                            AchievementTier.SILVER,
                            "Jan 2024",
                        ),
                        Achievement(
                            207,
                            "Mothwing Cloak",
                            "Obtain the Mothwing Cloak",
                            AchievementTier.BRONZE,
                            "Jan 2024",
                        ),
                        Achievement(
                            208,
                            "Soul Master",
                            "Defeat the Soul Master",
                            AchievementTier.BRONZE,
                            "Jan 2024",
                        ),
                    ),
            ),
            GameAchievements(
                id = 3,
                title = "Chrono Trigger",
                platform = "PlayStation 5",
                coverStart = CoverCrimsonStart,
                coverEnd = CoverCrimsonEnd,
                achievements =
                    listOf(
                        Achievement(
                            301,
                            "Chrono Trigger",
                            "Obtain all achievements",
                            AchievementTier.PLATINUM,
                            "Dec 2023",
                        ),
                        Achievement(302, "Lavos Defeated", "Defeat Lavos", AchievementTier.GOLD, "Dec 2023"),
                        Achievement(303, "All Endings", "See every ending", AchievementTier.GOLD, "Dec 2023"),
                        Achievement(304, "The End of Time", "Reach the End of Time", AchievementTier.GOLD, "Nov 2023"),
                        Achievement(305, "New Game+", "Start a New Game+", AchievementTier.SILVER, "Dec 2023"),
                        Achievement(
                            306,
                            "First Jump",
                            "Use your first time gate",
                            AchievementTier.BRONZE,
                            "Nov 2023",
                        ),
                    ),
            ),
            GameAchievements(
                id = 4,
                title = "Outer Wilds",
                platform = "PC",
                coverStart = CoverCyanStart,
                coverEnd = CoverCyanEnd,
                achievements =
                    listOf(
                        Achievement(401, "Outer Wilds", "Obtain all achievements", AchievementTier.PLATINUM),
                        Achievement(
                            402,
                            "Quantum Moon",
                            "Land on the Quantum Moon",
                            AchievementTier.GOLD,
                            "Sep 2024",
                        ),
                        Achievement(403, "The Search", "Reach the Eye", AchievementTier.GOLD, isSecret = true),
                        Achievement(
                            404,
                            "Ash Twin Project",
                            "Reach the Ash Twin Project",
                            AchievementTier.SILVER,
                            "Sep 2024",
                        ),
                        Achievement(405, "Hotshot", "Land on the Sun Station", AchievementTier.SILVER, "Aug 2024"),
                        Achievement(
                            406,
                            "Escape Velocity",
                            "Leave the solar system",
                            AchievementTier.BRONZE,
                            "Aug 2024",
                        ),
                    ),
            ),
            GameAchievements(
                id = 5,
                title = "Cyberpunk 2077: Phantom Liberty",
                platform = "PlayStation 5",
                coverStart = CoverVioletStart,
                coverEnd = CoverVioletEnd,
                achievements =
                    listOf(
                        Achievement(501, "Phantom Liberty", "Obtain all achievements", AchievementTier.PLATINUM),
                        Achievement(502, "The Killing Moon", "Reach the Moon", AchievementTier.GOLD, isSecret = true),
                        Achievement(
                            503,
                            "Spy vs Spy",
                            "Complete the Songbird questline",
                            AchievementTier.SILVER,
                            isSecret = true,
                        ),
                        Achievement(504, "Relic Master", "Unlock every Relic perk", AchievementTier.SILVER),
                        Achievement(505, "Netrunner", "Perform 25 quickhacks", AchievementTier.BRONZE, "Feb 2025"),
                        Achievement(
                            506,
                            "Airdrop",
                            "Loot 10 airdrops in Dogtown",
                            AchievementTier.BRONZE,
                            "Jan 2025",
                        ),
                        Achievement(507, "Dogtown Local", "Arrive in Dogtown", AchievementTier.BRONZE, "Jan 2025"),
                    ),
            ),
            GameAchievements(
                id = 6,
                title = "Final Fantasy VII Rebirth",
                platform = "PlayStation 5",
                coverStart = CoverAzureStart,
                coverEnd = CoverAzureEnd,
                achievements =
                    listOf(
                        Achievement(601, "Rebirth", "Obtain all achievements", AchievementTier.PLATINUM),
                        Achievement(602, "The Promised Land", "Finish the story", AchievementTier.GOLD, isSecret = true),
                        Achievement(
                            603,
                            "Queen's Blood Champion",
                            "Win every Queen's Blood match",
                            AchievementTier.GOLD,
                        ),
                        Achievement(
                            604,
                            "Master Chocobo Rider",
                            "Reach rank S in chocobo racing",
                            AchievementTier.SILVER,
                        ),
                        Achievement(605, "Full Party", "Recruit every party member", AchievementTier.SILVER),
                        Achievement(606, "Gold Saucer", "Visit the Gold Saucer", AchievementTier.SILVER, "Mar 2025"),
                        Achievement(607, "A New Journey", "Complete Chapter 1", AchievementTier.BRONZE, "Feb 2025"),
                    ),
            ),
        )

    fun byId(id: Long): GameAchievements = games.firstOrNull { it.id == id } ?: games.first()
}
