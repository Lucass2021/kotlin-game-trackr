package com.lucasdias.gametrackr.feature.auth.forgotpassword

import androidx.annotation.StringRes

data class ForgotPasswordUiState(
    val email: String = "",
    @param:StringRes val emailError: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val sentToEmail: String? = null
)
