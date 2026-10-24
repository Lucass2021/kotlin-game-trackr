package com.lucasdias.gametrackr.feature.auth.resetpassword

import com.lucasdias.gametrackr.core.ui.text.UiText

data class ResetPasswordUiState(
    val password: String = "",
    val confirmPassword: String = "",
    val passwordError: UiText? = null,
    val confirmPasswordError: UiText? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val done: Boolean = false,
)
