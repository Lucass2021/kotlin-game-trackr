package com.lucasdias.gametrackr.data.auth

import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeAccountRepository(
    var failure: Throwable? = null,
) : AccountRepository {
    var deleteCount = 0
        private set

    override suspend fun deleteAccount(): Result<Unit> {
        deleteCount++
        val error = failure
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return if (error != null) Result.failure(error) else Result.success(Unit)
    }
}
