package com.lucasdias.gametrackr.feature.auth.register

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.feature.auth.AuthValidation

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val acceptedTerms: Boolean = false,
    val isSubmitted: Boolean = false,
    val isLoading: Boolean = false,
    val isRegistered: Boolean = false,
    val toastMessage: UiText? = null,
) {
    val nameError: UiText? get() = if (isSubmitted) nameErrorFor(name) else null

    val emailError: UiText? get() = if (isSubmitted) AuthValidation.emailError(email) else null

    val passwordError: UiText? get() = if (isSubmitted) AuthValidation.passwordError(password) else null

    val confirmPasswordError: UiText?
        get() = if (isSubmitted) AuthValidation.confirmPasswordError(password, confirmPassword) else null

    val termsError: UiText? get() = if (isSubmitted) termsErrorFor(acceptedTerms) else null

    val canSubmit: Boolean
        get() =
            nameErrorFor(name) == null &&
                AuthValidation.emailError(email) == null &&
                AuthValidation.passwordError(password) == null &&
                AuthValidation.confirmPasswordError(password, confirmPassword) == null &&
                termsErrorFor(acceptedTerms) == null

    companion object {
        const val NAME_MIN_LENGTH = 3

        private fun nameErrorFor(name: String): UiText? {
            val trimmed = name.trim()
            return when {
                trimmed.isEmpty() -> UiText.Resource(R.string.validation_name_required)
                trimmed.length < NAME_MIN_LENGTH -> UiText.Resource(R.string.validation_name_too_short)
                else -> null
            }
        }

        private fun termsErrorFor(accepted: Boolean): UiText? = if (!accepted) UiText.Resource(R.string.validation_terms_required) else null
    }
}
