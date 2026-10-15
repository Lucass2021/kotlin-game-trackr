package com.lucasdias.gametrackr.data.profile

import com.lucasdias.gametrackr.core.model.ProfileOverview
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeProfileOverviewRepository : ProfileOverviewRepository {
    override suspend fun fetchMyOverview(): Result<ProfileOverview> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(
            ProfileOverview(
                breakdown = ProfileMockData.breakdown,
                favorites = ProfileMockData.favorites,
                currentlyPlaying = ProfileMockData.currentlyPlaying,
                activity = ProfileMockData.activity,
            ),
        )
    }

    override suspend fun fetchUserOverview(username: String): Result<ProfileOverview> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(
            ProfileOverview(
                breakdown = UserProfileMockData.breakdown,
                favorites = UserProfileMockData.favorites,
                currentlyPlaying = UserProfileMockData.currentlyPlaying,
                activity = UserProfileMockData.activity,
            ),
        )
    }
}
