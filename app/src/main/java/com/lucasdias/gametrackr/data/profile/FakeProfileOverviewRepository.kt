package com.lucasdias.gametrackr.data.profile

import com.lucasdias.gametrackr.core.model.ProfileOverview
import com.lucasdias.gametrackr.core.model.UserOverview
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeProfileOverviewRepository(
    private val myOverview: ProfileOverview = ProfileMockData.overview,
    private val userOverview: ProfileOverview = UserProfileMockData.overview,
    friendUsernames: Set<String> = emptySet(),
    var failure: Throwable? = null,
) : ProfileOverviewRepository {
    private val friendUsernames = friendUsernames.toMutableSet()

    var fetchCount = 0
        private set

    var setFriendCount = 0
        private set

    override suspend fun fetchMyOverview(): Result<ProfileOverview> {
        fetchCount++
        return respond { myOverview }
    }

    override suspend fun fetchUserOverview(username: String): Result<UserOverview> {
        fetchCount++
        return respond {
            UserOverview(
                bio = UserProfileMockData.BIO,
                joinedAt = UserProfileMockData.joinedAt,
                isFriend = username in friendUsernames,
                overview = userOverview,
            )
        }
    }

    override suspend fun setFriend(
        username: String,
        isFriend: Boolean,
    ): Result<Unit> {
        setFriendCount++
        return respond {
            if (isFriend) friendUsernames.add(username) else friendUsernames.remove(username)
            Unit
        }
    }

    fun isFriend(username: String): Boolean = username in friendUsernames

    private suspend fun <T> respond(block: () -> T): Result<T> {
        val error = failure
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return error?.let { Result.failure(it) } ?: runCatching(block)
    }
}
