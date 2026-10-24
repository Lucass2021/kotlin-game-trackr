package com.lucasdias.gametrackr.feature.app.settings.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.data.auth.PasswordRepository
import com.lucasdias.gametrackr.feature.auth.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val repository: PasswordRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun onCurrentPasswordChange(value: String) {
        _uiState.update { it.copy(currentPassword = value) }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update { it.copy(newPassword = value) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value) }
    }

    fun onSave() {
        val current = _uiState.value
        if (current.isSaving || current.isChanged) return
        _uiState.update { it.copy(isSubmitted = true) }
        if (!current.canSave) return

        _uiState.update { it.copy(isSaving = true, toastMessage = null) }
        viewModelScope.launch {
            repository
                .changePassword(currentPassword = current.currentPassword, newPassword = current.newPassword)
                .onSuccess { _uiState.update { it.copy(isSaving = false, isChanged = true) } }
                .onFailure { error -> _uiState.update { it.copy(isSaving = false, toastMessage = error.toUiText()) } }
        }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}
