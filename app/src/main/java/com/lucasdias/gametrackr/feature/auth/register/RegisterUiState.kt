package com.lucasdias.gametrackr.feature.auth.register

import com.lucasdias.gametrackr.core.ui.text.UiText

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val acceptedTerms: Boolean = false,
    val nameError: UiText? = null,
    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val confirmPasswordError: UiText? = null,
    val termsError: UiText? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registered: Boolean = false,
)
