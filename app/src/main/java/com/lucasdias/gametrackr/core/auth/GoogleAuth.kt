package com.lucasdias.gametrackr.core.auth

import kotlinx.coroutines.flow.StateFlow

enum class GoogleAuthState {
    IDLE,
    IN_PROGRESS,
    COMPLETING,
    FAILED,
}

interface GoogleAuth {
    val state: StateFlow<GoogleAuthState>

    fun launch()

    fun onErrorShown()
}
