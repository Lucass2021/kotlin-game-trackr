package com.lucasdias.gametrackr.feature.app.stats

import com.lucasdias.gametrackr.core.model.UserStats

data class StatsUiState(
    val stats: UserStats? = null,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
)
