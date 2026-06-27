package com.lucasdias.gametrackr.feature.auth.login

import androidx.annotation.StringRes

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    @param:StringRes val emailError: Int? = null,
    @param:StringRes val passwordError: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
