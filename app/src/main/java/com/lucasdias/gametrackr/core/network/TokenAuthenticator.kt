package com.lucasdias.gametrackr.core.network

import com.lucasdias.gametrackr.core.auth.SessionManager
import com.lucasdias.gametrackr.core.auth.TokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException

class TokenAuthenticator(
    private val tokenStore: TokenStore,
    private val refreshApi: RefreshApi,
    private val sessionManager: SessionManager
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= MAX_ATTEMPTS) return null

        val failedToken = response.request.header(HEADER)?.removePrefix(BEARER_PREFIX)

        synchronized(this) {
            val current = runBlocking { tokenStore.get() } ?: return null

            if (failedToken != null && failedToken != current) {
                return response.request.retryWith(current)
            }

            val newToken = try {
                val refreshResponse = refreshApi.refresh("$BEARER_PREFIX$current").execute()
                if (refreshResponse.isSuccessful) refreshResponse.body()?.token else null
            } catch (_: IOException) {
                return null
            }

            if (newToken == null) {
                runBlocking { tokenStore.clear() }
                sessionManager.setUnauthenticated()
                return null
            }

            runBlocking { tokenStore.save(newToken) }
            return response.request.retryWith(newToken)
        }
    }

    private fun Request.retryWith(token: String): Request =
        newBuilder().header(HEADER, "$BEARER_PREFIX$token").build()

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }

    private companion object {
        const val HEADER = "Authorization"
        const val BEARER_PREFIX = "Bearer "
        const val MAX_ATTEMPTS = 2
    }
}
