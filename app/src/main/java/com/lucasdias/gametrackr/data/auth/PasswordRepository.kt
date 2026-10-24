package com.lucasdias.gametrackr.data.auth

interface PasswordRepository {
    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
    ): Result<Unit>
}
