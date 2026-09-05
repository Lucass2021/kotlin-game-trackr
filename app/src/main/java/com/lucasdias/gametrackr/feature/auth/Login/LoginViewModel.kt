package com.lucasdias.gametrackr.feature.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.auth.AuthRepository
import com.lucasdias.gametrackr.core.auth.GoogleAuth
import com.lucasdias.gametrackr.core.auth.GoogleAuthState
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.feature.auth.toMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val googleAuth: GoogleAuth,
    private val context: Context,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> =
        combine(_uiState, googleAuth.state, ::withGoogleAuth)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), LoginUiState())

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
        googleAuth.onErrorShown()
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onGoogleSignIn() {
        googleAuth.launch(context)
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

    private fun withGoogleAuth(
        state: LoginUiState,
        googleState: GoogleAuthState,
    ): LoginUiState =
        when (googleState) {
            GoogleAuthState.IDLE, GoogleAuthState.IN_PROGRESS -> state
            GoogleAuthState.COMPLETING -> state.copy(isLoading = true, errorMessage = null)
            GoogleAuthState.FAILED -> state.copy(isLoading = false, errorMessage = context.getString(R.string.error_generic))
        }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
