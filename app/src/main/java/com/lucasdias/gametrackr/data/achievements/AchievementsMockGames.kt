package com.lucasdias.gametrackr.data.achievements

import com.lucasdias.gametrackr.core.model.Achievement
import com.lucasdias.gametrackr.core.model.AchievementTier
import com.lucasdias.gametrackr.core.model.CoverPalette
import com.lucasdias.gametrackr.core.model.GameAchievements

internal val outerWilds =
    GameAchievements(
        id = 4,
        title = "Outer Wilds",
        platform = "PC",
        cover = CoverPalette.CYAN,
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
    )

internal val phantomLiberty =
    GameAchievements(
        id = 5,
        title = "Cyberpunk 2077: Phantom Liberty",
        platform = "PlayStation 5",
        cover = CoverPalette.VIOLET,
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
    )

internal val rebirth =
    GameAchievements(
        id = 6,
        title = "Final Fantasy VII Rebirth",
        platform = "PlayStation 5",
        cover = CoverPalette.AZURE,
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
    )
