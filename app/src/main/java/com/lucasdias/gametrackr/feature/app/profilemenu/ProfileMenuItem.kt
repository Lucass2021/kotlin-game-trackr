package com.lucasdias.gametrackr.feature.app.profilemenu

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon

enum class ProfileMenuItem(
    val icon: AppIcon,
    @param:StringRes val titleRes: Int,
) {
    EDIT_PROFILE(AppIcon.EDIT_PROFILE, R.string.menu_edit_profile),
    STATS(AppIcon.CHART, R.string.menu_my_stats),
    ACHIEVEMENTS(AppIcon.MEDAL, R.string.menu_achievements),
    SETTINGS(AppIcon.SETTINGS, R.string.menu_settings),
    HELP(AppIcon.HELP, R.string.menu_help),
    ABOUT(AppIcon.INFO, R.string.menu_about),
}
