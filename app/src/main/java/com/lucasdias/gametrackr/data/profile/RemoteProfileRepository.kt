package com.lucasdias.gametrackr.data.profile

import com.lucasdias.gametrackr.core.model.ProfileColor
import com.lucasdias.gametrackr.core.model.User
import com.lucasdias.gametrackr.core.network.ProfileApi
import com.lucasdias.gametrackr.core.network.apiCall
import com.lucasdias.gametrackr.core.network.dto.UpdateProfileRequest
import com.lucasdias.gametrackr.core.network.dto.toDomain
import kotlinx.serialization.json.Json

class RemoteProfileRepository(
    private val api: ProfileApi,
    private val json: Json,
) : ProfileRepository {
    override suspend fun fetchColors(): Result<List<ProfileColor>> = apiCall(json) { api.getColors().data.map { it.toDomain() } }

    override suspend fun updateProfile(
        name: String,
        username: String,
        profileColor: String,
    ): Result<User> =
        apiCall(json) {
            api.update(UpdateProfileRequest(name = name, username = username, profileColor = profileColor)).user.toDomain()
        }
}
