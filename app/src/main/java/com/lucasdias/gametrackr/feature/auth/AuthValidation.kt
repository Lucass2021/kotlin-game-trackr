package com.lucasdias.gametrackr.feature.auth

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.text.UiText

object AuthValidation {
    const val PASSWORD_MIN_LENGTH = 6

    val passwordTooShort: UiText = UiText.Resource(R.string.validation_password_too_short, listOf(PASSWORD_MIN_LENGTH))

    fun emailError(email: String): UiText? {
        val trimmed = email.trim()
        return when {
            trimmed.isEmpty() -> UiText.Resource(R.string.validation_email_required)
            !trimmed.contains("@") || !trimmed.contains(".") -> UiText.Resource(R.string.validation_email_invalid)
            else -> null
        }
    }

    fun passwordError(password: String): UiText? =
        when {
            password.isEmpty() -> UiText.Resource(R.string.validation_password_required)
            password.length < PASSWORD_MIN_LENGTH -> passwordTooShort
            else -> null
        }

    fun confirmPasswordError(
        password: String,
        confirmation: String,
    ): UiText? = if (confirmation != password) UiText.Resource(R.string.validation_passwords_dont_match) else null
}
