package com.lucasdias.gametrackr.data.profile

import com.lucasdias.gametrackr.core.model.ProfileOverview

interface ProfileOverviewRepository {
    suspend fun fetchMyOverview(): Result<ProfileOverview>

    suspend fun fetchUserOverview(username: String): Result<ProfileOverview>
}
