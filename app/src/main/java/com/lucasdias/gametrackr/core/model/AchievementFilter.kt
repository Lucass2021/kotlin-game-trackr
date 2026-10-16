package com.lucasdias.gametrackr.core.model

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.R

enum class AchievementFilter(
    @param:StringRes val titleRes: Int,
) {
    ALL(R.string.achievements_filter_all),
    IN_PROGRESS(R.string.achievements_filter_in_progress),
    COMPLETED(R.string.achievements_filter_completed),
}
