package com.lucasdias.gametrackr.feature.app.profile

import com.lucasdias.gametrackr.core.model.ProfileOverview

data class ProfileUiState(
    val overview: ProfileOverview? = null,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
)
