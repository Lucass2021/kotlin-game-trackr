package com.lucasdias.gametrackr.feature.auth.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.data.auth.AuthRepository
import com.lucasdias.gametrackr.feature.auth.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val authRepository: AuthRepository,
    private val email: String,
    private val code: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value) }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun onSubmit() {
        if (_uiState.value.isLoading || _uiState.value.isReset) return
        _uiState.update { it.copy(isSubmitted = true) }
        val state = _uiState.value
        if (!state.canSubmit) return

        _uiState.update { it.copy(isLoading = true, toastMessage = null) }
        viewModelScope.launch {
            val result = authRepository.resetPassword(email, code, state.password)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isReset = result.isSuccess,
                    toastMessage = result.exceptionOrNull()?.toUiText(),
                )
            }
        }
    }
}
