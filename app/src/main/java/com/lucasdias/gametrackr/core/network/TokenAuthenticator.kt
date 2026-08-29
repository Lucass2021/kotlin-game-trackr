package com.lucasdias.gametrackr.core.network

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenRefresher: TokenRefresher,
) : Authenticator {
    override fun authenticate(
        route: Route?,
        response: Response,
    ): Request? {
        if (response.isPermissionDenied()) return null
        if (responseCount(response) >= MAX_ATTEMPTS) return null

        val spent = response.request.header(HEADER)?.removePrefix(BEARER_PREFIX) ?: return null
        val token = tokenRefresher.refresh(spent) ?: return null
        return response.request
            .newBuilder()
            .header(HEADER, "$BEARER_PREFIX$token")
            .build()
    }

    private fun Response.isPermissionDenied(): Boolean = runCatching { peekBody(PEEK_LIMIT).string().contains("\"error\"") }.getOrDefault(false)

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
        const val PEEK_LIMIT = 4096L
    }
}
