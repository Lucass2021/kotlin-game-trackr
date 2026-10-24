package com.lucasdias.gametrackr.feature.auth.verifyresetcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.data.auth.AuthRepository
import com.lucasdias.gametrackr.feature.auth.toUiText
import com.lucasdias.gametrackr.feature.auth.verifyresetcode.VerifyResetCodeUiState.Companion.CODE_LENGTH
import com.lucasdias.gametrackr.feature.auth.verifyresetcode.VerifyResetCodeUiState.Companion.RESEND_SECONDS
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VerifyResetCodeViewModel(
    private val authRepository: AuthRepository,
    private val email: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(VerifyResetCodeUiState())
    val uiState: StateFlow<VerifyResetCodeUiState> = _uiState.asStateFlow()

    private var countdownJob: Job? = null

    init {
        startCountdown()
    }

    fun onCodeChange(value: String) {
        val digits = value.filter { it in '0'..'9' }.take(CODE_LENGTH)
        if (digits == _uiState.value.code) return
        _uiState.update { it.copy(code = digits) }
        if (digits.length == CODE_LENGTH) onSubmit()
    }

    fun onSubmit() {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isSubmitted = true) }
        val state = _uiState.value
        if (!state.canSubmit) return

        _uiState.update { it.copy(isLoading = true, toastMessage = null) }
        viewModelScope.launch {
            val result = authRepository.verifyResetCode(email, state.code)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    verifiedCode = state.code.takeIf { result.isSuccess },
                    toastMessage = result.exceptionOrNull()?.toUiText(),
                )
            }
        }
    }

    fun onResend() {
        if (!_uiState.value.canResend) return
        startCountdown()
        viewModelScope.launch {
            authRepository.forgotPassword(email).onFailure { error ->
                countdownJob?.cancel()
                _uiState.update { it.copy(resendSeconds = 0, toastMessage = error.toUiText()) }
            }
        }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun onVerifiedHandled() {
        _uiState.update { it.copy(verifiedCode = null) }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        _uiState.update { it.copy(resendSeconds = RESEND_SECONDS) }
        countdownJob =
            viewModelScope.launch {
                while (_uiState.value.resendSeconds > 0) {
                    delay(TICK_MILLIS)
                    _uiState.update { it.copy(resendSeconds = it.resendSeconds - 1) }
                }
            }
    }

    private companion object {
        const val TICK_MILLIS = 1_000L
    }
}
