package com.lucasdias.gametrackr.feature.auth.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.data.auth.AuthRepository
import com.lucasdias.gametrackr.feature.auth.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value) }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun onCodeSentHandled() {
        _uiState.update { it.copy(sentToEmail = null) }
    }

    fun onSubmit() {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isSubmitted = true) }
        if (!_uiState.value.canSubmit) return

        val email = _uiState.value.email.trim()
        _uiState.update { it.copy(isLoading = true, toastMessage = null) }
        viewModelScope.launch {
            val result = authRepository.forgotPassword(email)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    sentToEmail = email.takeIf { result.isSuccess },
                    toastMessage = result.exceptionOrNull()?.toUiText(),
                )
            }
        }
    }
}
