package com.lucasdias.gametrackr.support

import com.lucasdias.gametrackr.core.auth.AuthRepository
import com.lucasdias.gametrackr.core.model.User

class FakeAuthRepository(
    private val result: Result<User> = Result.success(TestData.user()),
) : AuthRepository {
    var registerCount = 0
        private set
    var lastRegisteredName: String? = null
        private set

    override suspend fun login(
        email: String,
        password: String,
    ): Result<User> = result

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String,
    ): Result<User> {
        registerCount++
        lastRegisteredName = name
        return result
    }

    override suspend fun validate(): Result<User> = result

    override suspend fun signInWithGoogle(token: String): Result<User> = result

    override suspend fun logout() = Unit

    override suspend fun hasToken(): Boolean = false

    override fun completeRegistration() = Unit

    override suspend fun forgotPassword(email: String): Result<Unit> = Result.success(Unit)

    override suspend fun verifyResetCode(
        email: String,
        code: String,
    ): Result<Unit> = Result.success(Unit)

    override suspend fun resetPassword(
        email: String,
        code: String,
        newPassword: String,
    ): Result<Unit> = Result.success(Unit)

    override suspend fun completePasswordReset(): Result<User> = result
}
