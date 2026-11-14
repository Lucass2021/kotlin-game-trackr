package com.lucasdias.gametrackr.core.network

import com.lucasdias.gametrackr.core.auth.SessionManager
import com.lucasdias.gametrackr.core.auth.TokenStore
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
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

        val response =
            try {
                refreshApi.refresh("Bearer $current").execute()
            } catch (error: IOException) {
                throw RefreshFailedException(cause = error)
            } catch (error: SerializationException) {
                throw RefreshFailedException(cause = error)
            }

        if (response.code() == HTTP_UNAUTHORIZED) {
            runBlocking { tokenStore.clear() }
            sessionManager.setUnauthenticated()
            return null
        }

        val newToken = response.body()?.token
        if (!response.isSuccessful || newToken == null) throw RefreshFailedException(response.code())

        runBlocking { tokenStore.save(newToken) }
        return newToken
    }

    private companion object {
        const val HTTP_UNAUTHORIZED = 401
    }
}
