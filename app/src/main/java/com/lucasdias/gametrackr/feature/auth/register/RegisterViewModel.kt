package com.lucasdias.gametrackr.feature.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.auth.GoogleAuth
import com.lucasdias.gametrackr.core.auth.GoogleAuthState
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.data.auth.AuthRepository
import com.lucasdias.gametrackr.feature.auth.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val googleAuth: GoogleAuth,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> =
        combine(_uiState, googleAuth.state, ::withGoogleAuth)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), RegisterUiState())

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value) }
    }

    fun onToggleTerms() {
        _uiState.update { it.copy(acceptedTerms = !it.acceptedTerms) }
    }

    fun onToastShown() {
        googleAuth.onErrorShown()
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun onSignUpWithGoogle() {
        if (isBusy) return
        googleAuth.launch()
    }

    fun onSubmit() {
        if (isBusy) return
        _uiState.update { it.copy(isSubmitted = true) }
        val state = _uiState.value
        if (!state.canSubmit) return

        _uiState.update { it.copy(isLoading = true, toastMessage = null) }
        viewModelScope.launch {
            val result =
                authRepository.register(
                    name = state.name.trim(),
                    email = state.email.trim(),
                    password = state.password,
                    passwordConfirmation = state.confirmPassword,
                )
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isRegistered = result.isSuccess,
                    toastMessage = result.exceptionOrNull()?.toUiText(),
                )
            }
        }
    }

    private val isBusy: Boolean
        get() =
            _uiState.value.isLoading ||
                _uiState.value.isRegistered ||
                googleAuth.state.value == GoogleAuthState.IN_PROGRESS ||
                googleAuth.state.value == GoogleAuthState.COMPLETING

    private fun withGoogleAuth(
        state: RegisterUiState,
        googleState: GoogleAuthState,
    ): RegisterUiState =
        when (googleState) {
            GoogleAuthState.IDLE, GoogleAuthState.IN_PROGRESS -> state
            GoogleAuthState.COMPLETING -> state.copy(isLoading = true, toastMessage = null)
            GoogleAuthState.FAILED -> state.copy(isLoading = false, toastMessage = UiText.Resource(R.string.error_generic))
        }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
