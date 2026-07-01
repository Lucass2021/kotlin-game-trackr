package com.lucasdias.gametrackr.feature.auth.resetpassword

import androidx.annotation.StringRes

data class ResetPasswordUiState(
    val password: String = "",
    val confirmPassword: String = "",
    @param:StringRes val passwordError: Int? = null,
    @param:StringRes val confirmPasswordError: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val done: Boolean = false,
)
