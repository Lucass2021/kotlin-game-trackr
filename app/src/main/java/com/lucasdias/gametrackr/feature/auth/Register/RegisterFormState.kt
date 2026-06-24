package com.lucasdias.gametrackr.feature.auth.register

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lucasdias.gametrackr.R

private const val TAG = "RegisterForm"

class RegisterFormState {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var acceptedTerms by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
        private set

    private var submitted by mutableStateOf(false)

    @get:StringRes
    val nameError: Int?
        get() {
            if (!submitted) return null
            val trimmed = name.trim()
            return when {
                trimmed.isEmpty() -> R.string.validation_name_required
                trimmed.length < 3 -> R.string.validation_name_too_short
                else -> null
            }
        }

    @get:StringRes
    val emailError: Int?
        get() {
            if (!submitted) return null
            val trimmed = email.trim()
            return when {
                trimmed.isEmpty() -> R.string.validation_email_required
                !trimmed.contains("@") || !trimmed.contains(".") -> R.string.validation_email_invalid
                else -> null
            }
        }

    @get:StringRes
    val passwordError: Int?
        get() {
            if (!submitted) return null
            return when {
                password.isEmpty() -> R.string.validation_password_required
                password.length < 6 -> R.string.validation_password_too_short
                else -> null
            }
        }

    @get:StringRes
    val confirmPasswordError: Int?
        get() {
            if (!submitted) return null
            return if (confirmPassword != password) R.string.validation_passwords_dont_match else null
        }

    @get:StringRes
    val termsError: Int?
        get() {
            if (!submitted) return null
            return if (!acceptedTerms) R.string.validation_terms_required else null
        }

    fun submit(): Boolean {
        submitted = true
        val isValid = nameError == null &&
            emailError == null &&
            passwordError == null &&
            confirmPasswordError == null &&
            termsError == null
        if (isValid) {
            Log.d(TAG, "Sign up -> name: $name, email: $email, password: $password")
        }
        return isValid
    }

    fun signUpGoogle() {
        Log.d(TAG, "Sign up with Google")
    }
}
