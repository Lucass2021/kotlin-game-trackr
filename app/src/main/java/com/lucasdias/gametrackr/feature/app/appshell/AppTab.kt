package com.lucasdias.gametrackr.feature.app.appshell

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon

enum class AppTab(
    @StringRes val title: Int,
    val icon: AppIcon,
) {
    HOME(R.string.tab_home, AppIcon.HOME),
    LIBRARY(R.string.tab_library, AppIcon.LIBRARY),
    COMMUNITY(R.string.tab_community, AppIcon.COMMUNITY),
    PROFILE(R.string.tab_profile, AppIcon.PROFILE),
}
