package com.lucasdias.gametrackr.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.core.auth.AuthRepository
import com.lucasdias.gametrackr.core.auth.AuthStatus
import com.lucasdias.gametrackr.core.auth.SessionManager
import com.lucasdias.gametrackr.core.network.ApiError
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    val status: StateFlow<AuthStatus> = sessionManager.status

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
                if (error is ApiError.Network) {
                    sessionManager.setAuthenticated(null)
                } else {
                    sessionManager.setUnauthenticated()
                }
            }
        }
    }

    fun completeRegistration() {
        authRepository.completeRegistration()
    }

    fun completePasswordReset() {
        viewModelScope.launch { authRepository.completePasswordReset() }
    }

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
    }
}
