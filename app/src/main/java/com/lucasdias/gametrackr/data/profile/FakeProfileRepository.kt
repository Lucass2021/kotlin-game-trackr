package com.lucasdias.gametrackr.data.profile

import com.lucasdias.gametrackr.core.model.ProfileColor
import com.lucasdias.gametrackr.core.model.ProfileVisibility
import com.lucasdias.gametrackr.core.model.User
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeProfileRepository(
    private val colors: List<ProfileColor> = ProfileMockData.colors,
    user: User = ProfileMockData.user,
    var failure: Throwable? = null,
) : ProfileRepository {
    var user = user
        private set

    var fetchColorsCount = 0
        private set

    var updateCount = 0
        private set

    override suspend fun fetchColors(): Result<List<ProfileColor>> {
        fetchColorsCount++
        return respond { colors }
    }

    override suspend fun updateProfile(
        name: String,
        username: String,
        profileColor: String,
        bio: String,
        visibility: ProfileVisibility,
    ): Result<User> {
        updateCount++
        return respond {
            user =
                user.copy(
                    name = name,
                    username = username,
                    profileColor = profileColor,
                    bio = bio,
                    visibility = visibility,
                )
            user
        }
    }

    private suspend fun <T> respond(block: () -> T): Result<T> {
        val error = failure
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return error?.let { Result.failure(it) } ?: runCatching(block)
    }
}
