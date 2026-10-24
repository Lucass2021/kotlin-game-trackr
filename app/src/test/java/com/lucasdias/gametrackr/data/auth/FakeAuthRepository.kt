package com.lucasdias.gametrackr.data.auth

import com.lucasdias.gametrackr.core.model.User
import kotlinx.coroutines.CompletableDeferred

class FakeAuthRepository(
    var failure: Throwable? = null,
) : AuthRepository {
    val user = User(id = 1, name = "Player", email = "player@gametrackr.com")

    var loginCount = 0
        private set
    var lastEmail: String? = null
        private set
    var lastPassword: String? = null
        private set
    var registerCount = 0
        private set
    var lastName: String? = null
        private set
    var forgotPasswordCount = 0
        private set
    var verifyCount = 0
        private set
    var lastCode: String? = null
        private set
    var gate: CompletableDeferred<Unit>? = null

    override suspend fun login(
        email: String,
        password: String,
    ): Result<User> {
        loginCount += 1
        lastEmail = email
        lastPassword = password
        gate?.await()
        return failure?.let { Result.failure(it) } ?: Result.success(user)
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String,
    ): Result<User> {
        registerCount += 1
        lastName = name
        lastEmail = email
        lastPassword = password
        gate?.await()
        return failure?.let { Result.failure(it) } ?: Result.success(user)
    }

    override suspend fun validate(): Result<User> = Result.success(user)

    override suspend fun signInWithGoogle(token: String): Result<User> = Result.success(user)

    override suspend fun logout() = Unit

    override suspend fun hasToken(): Boolean = false

    override fun completeRegistration() = Unit

    override suspend fun forgotPassword(email: String): Result<Unit> {
        forgotPasswordCount += 1
        lastEmail = email
        gate?.await()
        return failure?.let { Result.failure(it) } ?: Result.success(Unit)
    }

    override suspend fun verifyResetCode(
        email: String,
        code: String,
    ): Result<Unit> {
        verifyCount += 1
        lastEmail = email
        lastCode = code
        gate?.await()
        return failure?.let { Result.failure(it) } ?: Result.success(Unit)
    }

    override suspend fun resetPassword(
        email: String,
        code: String,
        newPassword: String,
    ): Result<Unit> = Result.success(Unit)

    override suspend fun completePasswordReset(): Result<User> = Result.success(user)
}
