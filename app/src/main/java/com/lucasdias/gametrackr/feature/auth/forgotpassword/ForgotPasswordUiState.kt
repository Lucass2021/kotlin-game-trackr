package com.lucasdias.gametrackr.feature.auth.forgotpassword

import com.lucasdias.gametrackr.core.ui.text.UiText

data class ForgotPasswordUiState(
    val email: String = "",
    val emailError: UiText? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val sentToEmail: String? = null,
)
