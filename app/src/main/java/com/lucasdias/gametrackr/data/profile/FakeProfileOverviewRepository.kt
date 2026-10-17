package com.lucasdias.gametrackr.data.profile

import com.lucasdias.gametrackr.core.model.ProfileOverview
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeProfileOverviewRepository(
    private val myOverview: ProfileOverview = ProfileMockData.overview,
    private val userOverview: ProfileOverview = UserProfileMockData.overview,
    var failure: Throwable? = null,
) : ProfileOverviewRepository {
    var fetchCount = 0
        private set

    override suspend fun fetchMyOverview(): Result<ProfileOverview> = respond { myOverview }

    override suspend fun fetchUserOverview(username: String): Result<ProfileOverview> = respond { userOverview }

    private suspend fun <T> respond(block: () -> T): Result<T> {
        fetchCount++
        val error = failure
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return error?.let { Result.failure(it) } ?: runCatching(block)
    }
}
