package com.lucasdias.gametrackr.feature.auth.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.auth.AuthRepository
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.feature.auth.toMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "LoginViewModel"

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val context: Context,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private var submitted = false

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value) }
        revalidate()
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value) }
        revalidate()
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onGoogleSignIn() {
        Log.d(TAG, "Sign in with Google")
    }

    fun onSubmit() {
        submitted = true
        revalidate()
        val state = _uiState.value
        if (state.emailError != null || state.passwordError != null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.login(state.email.trim(), state.password)
            _uiState.update { it.copy(isLoading = false) }
            result.onFailure { error ->
                val message =
                    (error as? ApiError)?.toMessage(context)
                        ?: context.getString(R.string.error_generic)
                _uiState.update { it.copy(errorMessage = message) }
            }
        }
    }

    private fun revalidate() {
        if (!submitted) return
        _uiState.update {
            it.copy(
                emailError = emailErrorFor(it.email),
                passwordError = passwordErrorFor(it.password),
            )
        }
    }

    private fun emailErrorFor(email: String): Int? {
        val trimmed = email.trim()
        return when {
            trimmed.isEmpty() -> R.string.validation_email_required
            !trimmed.contains("@") || !trimmed.contains(".") -> R.string.validation_email_invalid
            else -> null
        }
    }

    private fun passwordErrorFor(password: String): Int? =
        when {
            password.isEmpty() -> R.string.validation_password_required
            password.length < 6 -> R.string.validation_password_too_short
            else -> null
        }
}
