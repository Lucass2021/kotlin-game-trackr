package com.lucasdias.gametrackr.core.network.dto

import com.lucasdias.gametrackr.core.model.ProfileVisibility
import com.lucasdias.gametrackr.core.model.User
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    @SerialName("password_confirmation") val passwordConfirmation: String,
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ForgotPasswordRequest(
    val email: String,
    @EncodeDefault val client: String = "mobile",
)

@Serializable
data class VerifyResetCodeRequest(
    val email: String,
    val code: String,
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ResetPasswordRequest(
    val email: String,
    val code: String,
    val password: String,
    @SerialName("password_confirmation") val passwordConfirmation: String,
    @EncodeDefault val client: String = "mobile",
)

@Serializable
data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
    val username: String? = null,
    @SerialName("profile_color") val profileColor: String? = null,
    val bio: String? = null,
    val visibility: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
)

fun UserDto.toDomain() =
    User(
        id = id,
        name = name,
        email = email,
        username = username,
        profileColor = profileColor,
        bio = bio,
        visibility = ProfileVisibility.fromApi(visibility),
        createdAt = createdAt?.let(::parseDate),
    )

private fun parseDate(value: String): LocalDate? = runCatching { Instant.parse(value).atZone(ZoneOffset.UTC).toLocalDate() }.getOrNull()

@Serializable
data class AuthResponse(
    val token: String,
    val user: UserDto,
    val message: String? = null,
)

@Serializable
data class RefreshResponse(
    val token: String,
)

@Serializable
data class ValidateResponse(
    val user: UserDto,
)

@Serializable
data class MessageResponse(
    val error: String? = null,
    val message: String? = null,
)

@Serializable
data class ApiErrorBody(
    val message: String? = null,
    val error: String? = null,
    val errors: Map<String, List<String>>? = null,
)
