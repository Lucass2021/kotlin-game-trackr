package com.lucasdias.gametrackr.data.profile

import com.lucasdias.gametrackr.data.FakeLatency
import com.lucasdias.gametrackr.feature.app.profile.ProfileMockData
import com.lucasdias.gametrackr.feature.app.profile.ProfileOverview
import com.lucasdias.gametrackr.feature.app.profile.UserProfileMockData
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
