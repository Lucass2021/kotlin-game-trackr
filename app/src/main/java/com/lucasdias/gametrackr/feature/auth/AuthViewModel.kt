package com.lucasdias.gametrackr.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.core.auth.AuthStatus
import com.lucasdias.gametrackr.core.auth.SessionManager
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.data.auth.AuthRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {
    val status: StateFlow<AuthStatus> = sessionManager.status

    private var completeResetJob: Job? = null

    init {
        resolveSession()
    }

    private fun resolveSession() {
        viewModelScope.launch {
            if (!authRepository.hasToken()) {
                sessionManager.setUnauthenticated()
                return@launch
            }
            authRepository.validate().onFailure { error ->
                if (error is ApiError.Unauthorized || error is ApiError.Forbidden) {
                    sessionManager.setUnauthenticated()
                } else {
                    sessionManager.setAuthenticated(null)
                }
            }
        }
    }

    fun completeRegistration() {
        authRepository.completeRegistration()
    }

    fun continueAsGuest() {
        sessionManager.setGuest()
    }

    fun completePasswordReset(onFailure: () -> Unit) {
        if (completeResetJob?.isActive == true) return
        completeResetJob =
            viewModelScope.launch {
                authRepository.completePasswordReset().onFailure { onFailure() }
            }
    }

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
    }
}
