package com.lucasdias.gametrackr.feature.app.stats

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.feature.app.library.LibraryStatus

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
                    StatBar("RPG", "42 games", 1.0f, AppPrimary),
                    StatBar("Action", "38 games", 0.9f, AppSecondary),
                    StatBar("Strategy", "21 games", 0.5f, AppPrimary),
                ),
            platforms =
                listOf(
                    StatBar("PlayStation 5", "60%", 0.6f, AppSecondary),
                    StatBar("PC Master Race", "25%", 0.25f, AppPrimary),
                    StatBar("Nintendo Switch", "15%", 0.15f, AppSecondary),
                ),
            yearsCompleted =
                listOf(
                    YearCount("2021", 9, AppSecondary.copy(alpha = 0.45f)),
                    YearCount("2022", 14, AppPrimary.copy(alpha = 0.5f)),
                    YearCount("2023", 22, AppSecondary),
                    YearCount("2024", 24, AppPrimary),
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
