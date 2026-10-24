package com.lucasdias.gametrackr.data.auth

import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakePasswordRepository(
    var failure: Throwable? = null,
) : PasswordRepository {
    var changeCount = 0
        private set

    var lastCurrentPassword: String? = null
        private set

    var lastNewPassword: String? = null
        private set

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
    ): Result<Unit> {
        changeCount++
        val error = failure
        delay(FakeLatency.RESPONSE_DELAY_MS)
        if (error != null) return Result.failure(error)
        lastCurrentPassword = currentPassword
        lastNewPassword = newPassword
        return Result.success(Unit)
    }
}
