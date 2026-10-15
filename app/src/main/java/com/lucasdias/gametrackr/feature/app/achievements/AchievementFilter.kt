package com.lucasdias.gametrackr.feature.app.achievements

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.GameAchievements
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppNeutral
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import kotlin.math.roundToInt

enum class AchievementFilter(
    @param:StringRes val titleRes: Int,
) {
    ALL(R.string.achievements_filter_all),
    IN_PROGRESS(R.string.achievements_filter_in_progress),
    COMPLETED(R.string.achievements_filter_completed),
    ;

    fun matches(game: GameAchievements): Boolean =
        when (this) {
            ALL -> true
            IN_PROGRESS -> !game.isComplete && game.unlockedCount > 0
            COMPLETED -> game.isComplete
        }
}
