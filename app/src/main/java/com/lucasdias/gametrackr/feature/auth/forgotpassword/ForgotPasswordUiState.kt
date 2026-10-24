package com.lucasdias.gametrackr.feature.auth.forgotpassword

import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.feature.auth.AuthValidation

data class ForgotPasswordUiState(
    val email: String = "",
    val isSubmitted: Boolean = false,
    val isLoading: Boolean = false,
    val sentToEmail: String? = null,
    val toastMessage: UiText? = null,
) {
    val emailError: UiText? get() = if (isSubmitted) AuthValidation.emailError(email) else null

    val canSubmit: Boolean get() = AuthValidation.emailError(email) == null
}
