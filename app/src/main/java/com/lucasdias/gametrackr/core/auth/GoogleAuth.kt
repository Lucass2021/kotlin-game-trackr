package com.lucasdias.gametrackr.core.auth

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.lucasdias.gametrackr.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class GoogleAuthState {
    IDLE,
    IN_PROGRESS,
    COMPLETING,
    FAILED,
}

class GoogleAuth(
    private val authRepository: AuthRepository,
) {
    private val _state = MutableStateFlow(GoogleAuthState.IDLE)
    val state: StateFlow<GoogleAuthState> = _state.asStateFlow()

    fun launch(context: Context) {
        _state.value = GoogleAuthState.IN_PROGRESS
        val url = "${BuildConfig.API_BASE_URL.trimEnd('/')}/auth/google/redirect?platform=mobile"
        CustomTabsIntent
            .Builder()
            .setShowTitle(true)
            .build()
            .apply { intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
            .launchUrl(context, url.toUri())
    }

    suspend fun handleCallback(intent: Intent?) {
        val uri = intent?.data ?: return
        if (uri.scheme != CALLBACK_SCHEME) return

        val token = uri.getQueryParameter(TOKEN_PARAM)?.takeIf { it.isNotBlank() }
        if (token == null) {
            _state.value = GoogleAuthState.FAILED
            return
        }

        _state.value = GoogleAuthState.COMPLETING
        _state.value =
            authRepository
                .signInWithGoogle(token)
                .fold({ GoogleAuthState.IDLE }, { GoogleAuthState.FAILED })
    }

    fun onResumed() {
        if (_state.value == GoogleAuthState.IN_PROGRESS) _state.value = GoogleAuthState.IDLE
    }

    fun onErrorShown() {
        if (_state.value == GoogleAuthState.FAILED) _state.value = GoogleAuthState.IDLE
    }

    private companion object {
        const val CALLBACK_SCHEME = "gametrackr"
        const val TOKEN_PARAM = "token"
    }
}
