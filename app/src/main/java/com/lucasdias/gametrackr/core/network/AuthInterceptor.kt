package com.lucasdias.gametrackr.core.network

import com.lucasdias.gametrackr.core.auth.TokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenStore: TokenStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.header(HEADER) != null) return chain.proceed(request)

        val token = runBlocking { tokenStore.get() } ?: return chain.proceed(request)

        val authenticated = request.newBuilder()
            .header(HEADER, "Bearer $token")
            .build()
        return chain.proceed(authenticated)
    }

    private companion object {
        const val HEADER = "Authorization"
    }
}
