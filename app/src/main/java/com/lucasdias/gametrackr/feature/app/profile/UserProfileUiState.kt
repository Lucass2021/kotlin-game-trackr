package com.lucasdias.gametrackr.feature.app.profile

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.core.model.Profile
import com.lucasdias.gametrackr.core.model.ProfileOverview

data class UserProfileUiState(
    val profile: Profile? = null,
    val overview: ProfileOverview? = null,
    val isFriend: Boolean = false,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    @param:StringRes val toastMessage: Int? = null,
)
