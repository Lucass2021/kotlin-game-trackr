package com.lucasdias.gametrackr.data.profile

import com.lucasdias.gametrackr.core.model.ProfileColor
import com.lucasdias.gametrackr.core.model.ProfileVisibility
import com.lucasdias.gametrackr.core.model.User

interface ProfileRepository {
    suspend fun fetchColors(): Result<List<ProfileColor>>

    suspend fun updateProfile(
        name: String,
        username: String,
        profileColor: String,
        bio: String,
        visibility: ProfileVisibility,
    ): Result<User>
}
