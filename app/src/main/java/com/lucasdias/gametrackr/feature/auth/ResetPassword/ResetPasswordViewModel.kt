package com.lucasdias.gametrackr.feature.auth.resetpassword

import android.content.Context
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

class ResetPasswordViewModel(
    private val authRepository: AuthRepository,
    private val context: Context,
    private val email: String,
    private val code: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    private var submitted = false

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value) }
        revalidate()
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value) }
        revalidate()
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onSubmit() {
        submitted = true
        revalidate()
        val state = _uiState.value
        if (state.passwordError != null || state.confirmPasswordError != null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.resetPassword(email, code, state.password)
            _uiState.update { it.copy(isLoading = false) }
            result.onSuccess {
                _uiState.update { it.copy(done = true) }
            }
            result.onFailure { error ->
                val message = (error as? ApiError)?.toMessage(context)
                    ?: context.getString(R.string.error_generic)
                _uiState.update { it.copy(errorMessage = message) }
            }
        }
    }

    private fun revalidate() {
        if (!submitted) return
        _uiState.update {
            it.copy(
                passwordError = passwordErrorFor(it.password),
                confirmPasswordError = confirmPasswordErrorFor(it.password, it.confirmPassword)
            )
        }
    }

    private fun passwordErrorFor(password: String): Int? = when {
        password.isEmpty() -> R.string.validation_password_required
        password.length < 6 -> R.string.validation_password_too_short
        else -> null
    }

    private fun confirmPasswordErrorFor(password: String, confirm: String): Int? =
        if (confirm != password) R.string.validation_passwords_dont_match else null
}
