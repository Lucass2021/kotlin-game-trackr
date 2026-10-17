package com.lucasdias.gametrackr.feature.app.profile

sealed interface ProfileHeaderMode {
    data object Own : ProfileHeaderMode

    data class Other(
        val isFriend: Boolean,
    ) : ProfileHeaderMode
}
