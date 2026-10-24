package com.lucasdias.gametrackr.feature.auth.login

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

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val googleAuth: GoogleAuth,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> =
        combine(_uiState, googleAuth.state, ::withGoogleAuth)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), LoginUiState())

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    fun onToastShown() {
        googleAuth.onErrorShown()
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun onSignInWithGoogle() {
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
            val result = authRepository.login(state.email.trim(), state.password)
            _uiState.update { it.copy(isLoading = false, toastMessage = result.exceptionOrNull()?.toUiText()) }
        }
    }

    private val isBusy: Boolean
        get() =
            _uiState.value.isLoading ||
                googleAuth.state.value == GoogleAuthState.IN_PROGRESS ||
                googleAuth.state.value == GoogleAuthState.COMPLETING

    private fun withGoogleAuth(
        state: LoginUiState,
        googleState: GoogleAuthState,
    ): LoginUiState =
        when (googleState) {
            GoogleAuthState.IDLE, GoogleAuthState.IN_PROGRESS -> state
            GoogleAuthState.COMPLETING -> state.copy(isLoading = true, toastMessage = null)
            GoogleAuthState.FAILED -> state.copy(isLoading = false, toastMessage = UiText.Resource(R.string.error_generic))
        }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
