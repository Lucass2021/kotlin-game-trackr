package com.lucasdias.gametrackr.core.auth

import com.lucasdias.gametrackr.core.model.User
import com.lucasdias.gametrackr.core.network.AuthApi
import com.lucasdias.gametrackr.core.network.dto.ForgotPasswordRequest
import com.lucasdias.gametrackr.core.network.dto.LoginRequest
import com.lucasdias.gametrackr.core.network.dto.RegisterRequest
import com.lucasdias.gametrackr.core.network.dto.ResetPasswordRequest
import com.lucasdias.gametrackr.core.network.dto.VerifyResetCodeRequest
import com.lucasdias.gametrackr.core.network.dto.toDomain
import com.lucasdias.gametrackr.core.network.toApiError
import kotlinx.serialization.json.Json

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenStore: TokenStore,
    private val sessionManager: SessionManager,
    private val json: Json,
) : AuthRepository {
    private var pendingUser: User? = null
    private var pendingResetEmail: String? = null
    private var pendingResetPassword: String? = null

    override suspend fun login(
        email: String,
        password: String,
    ): Result<User> =
        apiCall {
            val response = api.login(LoginRequest(email = email, password = password))
            tokenStore.save(response.token)
            response.user.toDomain().also(sessionManager::setAuthenticated)
        }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String,
    ): Result<User> =
        apiCall {
            val response =
                api.register(
                    RegisterRequest(
                        name = name,
                        email = email,
                        password = password,
                        passwordConfirmation = passwordConfirmation,
                    ),
                )
            tokenStore.save(response.token)
            response.user.toDomain().also { pendingUser = it }
        }

    override fun completeRegistration() {
        pendingUser?.let(sessionManager::setAuthenticated)
        pendingUser = null
    }

    override suspend fun validate(): Result<User> =
        apiCall {
            api
                .validate()
                .user
                .toDomain()
                .also(sessionManager::setAuthenticated)
        }

    override suspend fun logout() {
        runCatching { api.logout() }
        tokenStore.clear()
        sessionManager.setUnauthenticated()
    }

    override suspend fun hasToken(): Boolean = tokenStore.get() != null

    override suspend fun forgotPassword(email: String): Result<Unit> =
        apiCall {
            api.forgotPassword(ForgotPasswordRequest(email = email))
            Unit
        }

    override suspend fun verifyResetCode(
        email: String,
        code: String,
    ): Result<Unit> =
        apiCall {
            api.verifyResetCode(VerifyResetCodeRequest(email = email, code = code))
            Unit
        }

    override suspend fun resetPassword(
        email: String,
        code: String,
        newPassword: String,
    ): Result<Unit> =
        apiCall {
            api.resetPassword(
                ResetPasswordRequest(
                    email = email,
                    code = code,
                    password = newPassword,
                    passwordConfirmation = newPassword,
                ),
            )
            pendingResetEmail = email
            pendingResetPassword = newPassword
        }

    override suspend fun completePasswordReset(): Result<User> =
        apiCall {
            val email = requireNotNull(pendingResetEmail)
            val password = requireNotNull(pendingResetPassword)
            val response = api.login(LoginRequest(email = email, password = password))
            tokenStore.save(response.token)
            pendingResetEmail = null
            pendingResetPassword = null
            response.user.toDomain().also(sessionManager::setAuthenticated)
        }

    private inline fun <T> apiCall(block: () -> T): Result<T> =
        try {
            Result.success(block())
        } catch (throwable: Throwable) {
            Result.failure(throwable.toApiError(json))
        }
}
