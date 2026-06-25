package com.lucasdias.gametrackr.feature.auth.login

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lucasdias.gametrackr.R

private const val TAG = "LoginForm"

class LoginFormState {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var rememberMe by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
        private set

    private var submitted by mutableStateOf(false)

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

    fun submit(): Boolean {
        submitted = true
        val isValid = emailError == null && passwordError == null 
        if (isValid) {
            Log.d(TAG, "Sign in -> email: $email, password: $password")
        }
        return isValid
    }

    fun signInGoogle() {
        Log.d(TAG, "Sign in with Google")
    }

    fun forgotPassword() {
        Log.d(TAG, "Forgot password -> email: $email")
    }
}
