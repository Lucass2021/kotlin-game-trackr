package com.lucasdias.gametrackr.feature.app.gamedetail

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.core.model.GameDetail

data class GameDetailUiState(
    val game: GameDetail? = null,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val isInLibrary: Boolean = false,
    @param:StringRes val toastMessage: Int? = null,
)
