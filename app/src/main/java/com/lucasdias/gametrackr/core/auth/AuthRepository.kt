package com.lucasdias.gametrackr.core.auth

import com.lucasdias.gametrackr.core.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): Result<User>

    suspend fun validate(): Result<User>
    suspend fun logout()
    suspend fun hasToken(): Boolean
}
