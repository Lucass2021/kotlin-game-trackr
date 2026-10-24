package com.lucasdias.gametrackr.feature.app.profilemenu

data class ProfileMenuActions(
    val onBack: () -> Unit = {},
    val onItemSelect: (ProfileMenuItem) -> Unit = {},
    val onLogout: () -> Unit = {},
)
