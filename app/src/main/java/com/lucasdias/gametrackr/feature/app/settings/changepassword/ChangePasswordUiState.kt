package com.lucasdias.gametrackr.feature.app.settings.changepassword

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.feature.auth.AuthValidation

data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isSubmitted: Boolean = false,
    val isSaving: Boolean = false,
    val isChanged: Boolean = false,
    val toastMessage: UiText? = null,
) {
    val currentPasswordError: UiText? get() = if (isSubmitted) currentPasswordErrorFor(currentPassword) else null

    val newPasswordError: UiText? get() = if (isSubmitted) newPasswordErrorFor(newPassword, currentPassword) else null

    val confirmPasswordError: UiText? get() = if (isSubmitted) confirmPasswordErrorFor(confirmPassword, newPassword) else null

    val canSave: Boolean
        get() =
            currentPasswordErrorFor(currentPassword) == null &&
                newPasswordErrorFor(newPassword, currentPassword) == null &&
                confirmPasswordErrorFor(confirmPassword, newPassword) == null

    companion object {
        private fun currentPasswordErrorFor(password: String): UiText? =
            if (password.isEmpty()) UiText.Resource(R.string.change_password_error_current_required) else null

        private fun newPasswordErrorFor(
            password: String,
            currentPassword: String,
        ): UiText? =
            when {
                password.isEmpty() -> UiText.Resource(R.string.change_password_error_new_required)
                password.length < AuthValidation.PASSWORD_MIN_LENGTH -> AuthValidation.passwordTooShort
                password == currentPassword -> UiText.Resource(R.string.change_password_error_same_as_current)
                else -> null
            }

        private fun confirmPasswordErrorFor(
            confirmation: String,
            password: String,
        ): UiText? =
            when {
                confirmation.isEmpty() -> UiText.Resource(R.string.change_password_error_confirm_required)
                confirmation != password -> UiText.Resource(R.string.validation_passwords_dont_match)
                else -> null
            }
    }
}
