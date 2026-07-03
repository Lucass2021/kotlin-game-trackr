package com.lucasdias.gametrackr.feature.app.appshell

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.lucasdias.gametrackr.R

enum class AppTab(
    @StringRes val title: Int,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
) {
    HOME(R.string.tab_home, Icons.Outlined.Home, Icons.Filled.Home),
    LIBRARY(R.string.tab_library, Icons.Outlined.Layers, Icons.Filled.Layers),
    COMMUNITY(R.string.tab_community, Icons.Outlined.Groups, Icons.Filled.Groups),
    PROFILE(R.string.tab_profile, Icons.Outlined.Person, Icons.Filled.Person),
}
