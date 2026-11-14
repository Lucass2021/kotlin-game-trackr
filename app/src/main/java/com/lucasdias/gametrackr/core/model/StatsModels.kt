package com.lucasdias.gametrackr.core.model

import androidx.annotation.StringRes

data class StatHighlight(
    @param:StringRes val labelRes: Int,
    val value: String,
    val caption: String,
)

data class StatusShare(
    val status: LibraryStatus,
    val percent: Int,
)

data class StatBar(
    val label: String,
    val value: String,
    val fraction: Float,
    val accent: StatAccent,
)

data class YearCount(
    val year: String,
    val count: Int,
    val accent: StatAccent,
)

enum class StatAccent {
    PRIMARY,
    SECONDARY,
    PRIMARY_MUTED,
    SECONDARY_MUTED,
}

data class AchievementSpotlight(
    val title: String,
    val message: String,
    val actionTitle: String,
)

data class UserStats(
    val highlights: List<StatHighlight>,
    val statusShare: List<StatusShare>,
    val genres: List<StatBar>,
    val platforms: List<StatBar>,
    val yearsCompleted: List<YearCount>,
    val spotlight: AchievementSpotlight,
)
