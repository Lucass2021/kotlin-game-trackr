package com.lucasdias.gametrackr.feature.app.settings.changepassword

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.text.UiText

data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isSubmitted: Boolean = false,
    val isSaving: Boolean = false,
    val isChanged: Boolean = false,
    val toastMessage: UiText? = null,
) {
    @get:StringRes
    val currentPasswordError: Int? get() = if (isSubmitted) currentPasswordErrorFor(currentPassword) else null

    @get:StringRes
    val newPasswordError: Int? get() = if (isSubmitted) newPasswordErrorFor(newPassword) else null

    @get:StringRes
    val confirmPasswordError: Int? get() = if (isSubmitted) confirmPasswordErrorFor(confirmPassword, newPassword) else null

    val canSave: Boolean
        get() =
            currentPasswordErrorFor(currentPassword) == null &&
                newPasswordErrorFor(newPassword) == null &&
                confirmPasswordErrorFor(confirmPassword, newPassword) == null

    companion object {
        const val PASSWORD_MIN = 6

        private fun currentPasswordErrorFor(password: String): Int? = if (password.isEmpty()) R.string.change_password_error_current_required else null

        private fun newPasswordErrorFor(password: String): Int? =
            when {
                password.isEmpty() -> R.string.change_password_error_new_required
                password.length < PASSWORD_MIN -> R.string.validation_password_too_short
                else -> null
            }

        private fun confirmPasswordErrorFor(
            confirmation: String,
            password: String,
        ): Int? =
            when {
                confirmation.isEmpty() -> R.string.change_password_error_confirm_required
                confirmation != password -> R.string.validation_passwords_dont_match
                else -> null
            }
    }
}
