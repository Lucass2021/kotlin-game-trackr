package com.lucasdias.gametrackr.core.auth

import com.lucasdias.gametrackr.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface AuthStatus {
    data object Loading : AuthStatus

    data class Authenticated(
        val user: User?,
    ) : AuthStatus

    data object Guest : AuthStatus

    data object Unauthenticated : AuthStatus
}

class SessionManager {
    private val _status = MutableStateFlow<AuthStatus>(AuthStatus.Loading)
    val status: StateFlow<AuthStatus> = _status.asStateFlow()

    fun setAuthenticated(user: User?) {
        _status.value = AuthStatus.Authenticated(user)
    }

    fun setGuest() {
        _status.value = AuthStatus.Guest
    }

    fun setUnauthenticated() {
        _status.value = AuthStatus.Unauthenticated
    }
}
