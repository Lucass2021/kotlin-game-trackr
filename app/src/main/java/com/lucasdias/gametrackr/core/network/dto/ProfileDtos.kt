package com.lucasdias.gametrackr.core.network.dto

import com.lucasdias.gametrackr.core.model.ProfileColor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileColorDto(
    val key: String,
    val name: String,
    val hex: String,
)

@Serializable
data class ProfileColorsResponse(
    val message: String? = null,
    val data: List<ProfileColorDto>,
)

@Serializable
data class UpdateProfileRequest(
    val name: String,
    val username: String,
    @SerialName("profile_color") val profileColor: String,
)

fun ProfileColorDto.toDomain() = ProfileColor(key = key, name = name, hex = hex)
