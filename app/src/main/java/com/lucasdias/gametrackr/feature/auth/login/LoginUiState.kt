package com.lucasdias.gametrackr.feature.auth.login

import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.feature.auth.AuthValidation

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isSubmitted: Boolean = false,
    val isLoading: Boolean = false,
    val toastMessage: UiText? = null,
) {
    val emailError: UiText? get() = if (isSubmitted) AuthValidation.emailError(email) else null

    val passwordError: UiText? get() = if (isSubmitted) AuthValidation.passwordError(password) else null

    val canSubmit: Boolean
        get() = AuthValidation.emailError(email) == null && AuthValidation.passwordError(password) == null
}
