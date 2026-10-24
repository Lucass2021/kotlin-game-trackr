package com.lucasdias.gametrackr.feature.auth

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.text.UiText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthValidationTest {
    @Test
    fun `email is trimmed before validation`() {
        assertNull(AuthValidation.emailError("  player@gametrackr.com \n"))
        assertEquals(UiText.Resource(R.string.validation_email_required), AuthValidation.emailError(" \n "))
        assertEquals(UiText.Resource(R.string.validation_email_invalid), AuthValidation.emailError("player@gametrackr"))
    }

    @Test
    fun `password needs the minimum length`() {
        assertEquals(UiText.Resource(R.string.validation_password_required), AuthValidation.passwordError(""))
        assertEquals(UiText.Resource(R.string.validation_password_too_short, listOf(6)), AuthValidation.passwordError("12345"))
        assertNull(AuthValidation.passwordError("123456"))
    }

    @Test
    fun `confirmation must match the password`() {
        assertEquals(
            UiText.Resource(R.string.validation_passwords_dont_match),
            AuthValidation.confirmPasswordError("secret123", "secret12"),
        )
        assertNull(AuthValidation.confirmPasswordError("secret123", "secret123"))
    }
}
