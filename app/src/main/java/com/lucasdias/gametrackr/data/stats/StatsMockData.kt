package com.lucasdias.gametrackr.data.stats

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Achievement
import com.lucasdias.gametrackr.core.model.AchievementSpotlight
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.model.StatAccent
import com.lucasdias.gametrackr.core.model.StatBar
import com.lucasdias.gametrackr.core.model.StatHighlight
import com.lucasdias.gametrackr.core.model.StatusShare
import com.lucasdias.gametrackr.core.model.UserStats
import com.lucasdias.gametrackr.core.model.YearCount

object StatsMockData {
    val stats =
        UserStats(
            highlights =
                listOf(
                    StatHighlight(R.string.stats_highlight_games, "142", "+12 this month"),
                    StatHighlight(R.string.stats_highlight_hours, "3,420", "~24h / week avg"),
                    StatHighlight(R.string.stats_highlight_platinum, "15%", "Top 5% of users"),
                ),
            statusShare =
                listOf(
                    StatusShare(LibraryStatus.COMPLETED, 45),
                    StatusShare(LibraryStatus.PLAYING, 30),
                    StatusShare(LibraryStatus.BACKLOG, 25),
                ),
            genres =
                listOf(
                    StatBar("RPG", "42 games", 1.0f, StatAccent.PRIMARY),
                    StatBar("Action", "38 games", 0.9f, StatAccent.SECONDARY),
                    StatBar("Strategy", "21 games", 0.5f, StatAccent.PRIMARY),
                ),
            platforms =
                listOf(
                    StatBar("PlayStation 5", "60%", 0.6f, StatAccent.SECONDARY),
                    StatBar("PC Master Race", "25%", 0.25f, StatAccent.PRIMARY),
                    StatBar("Nintendo Switch", "15%", 0.15f, StatAccent.SECONDARY),
                ),
            yearsCompleted =
                listOf(
                    YearCount("2021", 9, StatAccent.SECONDARY_MUTED),
                    YearCount("2022", 14, StatAccent.PRIMARY_MUTED),
                    YearCount("2023", 22, StatAccent.SECONDARY),
                    YearCount("2024", 24, StatAccent.PRIMARY),
                ),
            spotlight =
                AchievementSpotlight(
                    title = "Achievement Master",
                    message =
                        "You've completed more games than 98% of people in your region this quarter. " +
                            "Keep the streak alive!",
                    actionTitle = "View achievements",
                ),
        )
}
