package com.lucasdias.gametrackr.core.auth

import kotlinx.coroutines.flow.MutableStateFlow

class FakeGoogleAuth : GoogleAuth {
    override val state = MutableStateFlow(GoogleAuthState.IDLE)

    var launchCount = 0
        private set

    override fun launch() {
        launchCount += 1
        state.value = GoogleAuthState.IN_PROGRESS
    }

    override fun onErrorShown() {
        if (state.value == GoogleAuthState.FAILED) state.value = GoogleAuthState.IDLE
    }
}
