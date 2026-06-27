package com.lucasdias.gametrackr.core.auth

import com.lucasdias.gametrackr.core.model.User
import com.lucasdias.gametrackr.core.network.AuthApi
import com.lucasdias.gametrackr.core.network.dto.LoginRequest
import com.lucasdias.gametrackr.core.network.dto.RegisterRequest
import com.lucasdias.gametrackr.core.network.dto.toDomain
import com.lucasdias.gametrackr.core.network.toApiError
import kotlinx.serialization.json.Json

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenStore: TokenStore,
    private val sessionManager: SessionManager,
    private val json: Json
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> = apiCall {
        val response = api.login(LoginRequest(email = email, password = password))
        tokenStore.save(response.token)
        response.user.toDomain().also(sessionManager::setAuthenticated)
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): Result<User> = apiCall {
        val response = api.register(
            RegisterRequest(
                name = name,
                email = email,
                password = password,
                passwordConfirmation = passwordConfirmation
            )
        )
        tokenStore.save(response.token)
        response.user.toDomain().also(sessionManager::setAuthenticated)
    }

    override suspend fun validate(): Result<User> = apiCall {
        api.validate().user.toDomain().also(sessionManager::setAuthenticated)
    }

    override suspend fun logout() {
        runCatching { api.logout() }
        tokenStore.clear()
        sessionManager.setUnauthenticated()
    }

    override suspend fun hasToken(): Boolean = tokenStore.get() != null

    private inline fun <T> apiCall(block: () -> T): Result<T> =
        try {
            Result.success(block())
        } catch (throwable: Throwable) {
            Result.failure(throwable.toApiError(json))
        }
}
