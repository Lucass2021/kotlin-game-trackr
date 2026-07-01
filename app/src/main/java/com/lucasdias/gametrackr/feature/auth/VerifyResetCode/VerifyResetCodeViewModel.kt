package com.lucasdias.gametrackr.feature.auth.verifyresetcode

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.auth.AuthRepository
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.feature.auth.toMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VerifyResetCodeViewModel(
    private val authRepository: AuthRepository,
    private val context: Context,
    private val email: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(VerifyResetCodeUiState())
    val uiState: StateFlow<VerifyResetCodeUiState> = _uiState.asStateFlow()

    private var submitted = false
    private var countdownJob: Job? = null

    init {
        startCountdown()
    }

    fun onCodeChange(value: String) {
        val digits = value.filter { it.isDigit() }.take(CODE_LENGTH)
        _uiState.update { it.copy(code = digits, codeError = codeErrorFor(digits)) }
        if (digits.length == CODE_LENGTH) verify()
    }

    fun onConfirm() = verify()

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onVerifiedHandled() {
        _uiState.update { it.copy(verified = false) }
    }

    fun onResend() {
        if (_uiState.value.resendSeconds > 0) return
        viewModelScope.launch { authRepository.forgotPassword(email) }
        startCountdown()
    }

    private fun verify() {
        if (_uiState.value.isLoading) return
        submitted = true
        val code = _uiState.value.code
        val codeError = codeErrorFor(code)
        _uiState.update { it.copy(codeError = codeError) }
        if (codeError != null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.verifyResetCode(email, code)
            _uiState.update { it.copy(isLoading = false) }
            result.onSuccess {
                _uiState.update { it.copy(verified = true) }
            }
            result.onFailure { error ->
                val message =
                    (error as? ApiError)?.toMessage(context)
                        ?: context.getString(R.string.error_generic)
                _uiState.update { it.copy(errorMessage = message) }
            }
        }
    }

    private fun codeErrorFor(code: String): Int? {
        if (!submitted) return null
        return when {
            code.isBlank() -> R.string.validation_code_required
            code.length != CODE_LENGTH -> R.string.validation_code_invalid
            else -> null
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob =
            viewModelScope.launch {
                _uiState.update { it.copy(resendSeconds = RESEND_SECONDS) }
                while (_uiState.value.resendSeconds > 0) {
                    delay(1000)
                    _uiState.update { it.copy(resendSeconds = it.resendSeconds - 1) }
                }
            }
    }
}
