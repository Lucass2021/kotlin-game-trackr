package com.lucasdias.gametrackr.data.achievements

import com.lucasdias.gametrackr.core.model.Achievement
import com.lucasdias.gametrackr.core.model.AchievementTier
import com.lucasdias.gametrackr.core.model.CoverPalette
import com.lucasdias.gametrackr.core.model.GameAchievements

object AchievementsMockData {
    val games = listOf(eldenRing, hollowKnight, chronoTrigger, outerWilds, phantomLiberty, rebirth)
}

private val eldenRing =
    GameAchievements(
        id = 1,
        title = "Elden Ring",
        platform = "PlayStation 5",
        cover = CoverPalette.EMERALD,
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
    )

private val hollowKnight =
    GameAchievements(
        id = 2,
        title = "Hollow Knight",
        platform = "Nintendo Switch",
        cover = CoverPalette.INDIGO,
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
    )

private val chronoTrigger =
    GameAchievements(
        id = 3,
        title = "Chrono Trigger",
        platform = "PlayStation 5",
        cover = CoverPalette.CRIMSON,
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
    )
