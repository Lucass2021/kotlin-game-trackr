package com.lucasdias.gametrackr.core.network

import com.lucasdias.gametrackr.core.auth.SessionManager
import com.lucasdias.gametrackr.core.auth.TokenStore
import kotlinx.coroutines.runBlocking
import java.io.IOException

class TokenRefresher(
    private val tokenStore: TokenStore,
    private val refreshApi: RefreshApi,
    private val sessionManager: SessionManager,
) {
    @Synchronized
    fun refresh(spentToken: String): String? {
        val current = runBlocking { tokenStore.get() } ?: return null
        if (current != spentToken) return current

        val newToken =
            try {
                val response = refreshApi.refresh("Bearer $current").execute()
                if (response.isSuccessful) response.body()?.token else null
            } catch (_: IOException) {
                return null
            }

        if (newToken == null) {
            runBlocking { tokenStore.clear() }
            sessionManager.setUnauthenticated()
            return null
        }

        runBlocking { tokenStore.save(newToken) }
        return newToken
    }
}
