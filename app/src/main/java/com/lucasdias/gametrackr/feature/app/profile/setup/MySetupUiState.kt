package com.lucasdias.gametrackr.feature.app.profile.setup

import com.lucasdias.gametrackr.core.model.SetupItem

data class MySetupUiState(
    val setups: List<SetupItem> = emptyList(),
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
)
