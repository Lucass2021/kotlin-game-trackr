package com.lucasdias.gametrackr.data.profile

import com.lucasdias.gametrackr.core.model.ProfileOverview
import com.lucasdias.gametrackr.core.model.UserOverview

interface ProfileOverviewRepository {
    suspend fun fetchMyOverview(): Result<ProfileOverview>

    suspend fun fetchUserOverview(username: String): Result<UserOverview>

    suspend fun setFriend(
        username: String,
        isFriend: Boolean,
    ): Result<Unit>
}
