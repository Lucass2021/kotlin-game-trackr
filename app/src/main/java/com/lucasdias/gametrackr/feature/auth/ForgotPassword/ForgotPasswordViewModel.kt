package com.lucasdias.gametrackr.feature.auth.forgotpassword

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

class ForgotPasswordViewModel(
    private val authRepository: AuthRepository,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    private var submitted = false

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value) }
        revalidate()
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onSubmit() {
        submitted = true
        revalidate()
        val email = _uiState.value.email.trim()
        if (_uiState.value.emailError != null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.forgotPassword(email)
            _uiState.update { it.copy(isLoading = false) }
            result.onSuccess {
                _uiState.update { it.copy(sentToEmail = email) }
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
        _uiState.update { it.copy(emailError = emailErrorFor(it.email)) }
    }

    private fun emailErrorFor(email: String): Int? {
        val trimmed = email.trim()
        return when {
            trimmed.isEmpty() -> R.string.validation_email_required
            !trimmed.contains("@") || !trimmed.contains(".") -> R.string.validation_email_invalid
            else -> null
        }
    }
}
