package com.lucasdias.gametrackr.feature.app.stats

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.feature.app.library.LibraryStatus

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
    val tint: Color,
)

data class YearCount(
    val year: String,
    val count: Int,
    val tint: Color,
)

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
