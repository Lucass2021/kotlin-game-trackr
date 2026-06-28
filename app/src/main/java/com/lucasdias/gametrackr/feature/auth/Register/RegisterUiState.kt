package com.lucasdias.gametrackr.feature.auth.register

import androidx.annotation.StringRes

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val acceptedTerms: Boolean = false,
    @param:StringRes val nameError: Int? = null,
    @param:StringRes val emailError: Int? = null,
    @param:StringRes val passwordError: Int? = null,
    @param:StringRes val confirmPasswordError: Int? = null,
    @param:StringRes val termsError: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registered: Boolean = false
)
