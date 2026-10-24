package com.lucasdias.gametrackr.feature.auth.resetpassword

import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.feature.auth.AuthValidation

data class ResetPasswordUiState(
    val password: String = "",
    val confirmPassword: String = "",
    val isSubmitted: Boolean = false,
    val isLoading: Boolean = false,
    val isReset: Boolean = false,
    val toastMessage: UiText? = null,
) {
    val passwordError: UiText? get() = if (isSubmitted) AuthValidation.passwordError(password) else null

    val confirmPasswordError: UiText?
        get() = if (isSubmitted) AuthValidation.confirmPasswordError(password, confirmPassword) else null

    val canSubmit: Boolean
        get() =
            AuthValidation.passwordError(password) == null &&
                AuthValidation.confirmPasswordError(password, confirmPassword) == null
}
