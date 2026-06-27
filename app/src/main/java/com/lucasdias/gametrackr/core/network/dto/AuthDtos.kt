package com.lucasdias.gametrackr.core.network.dto

import com.lucasdias.gametrackr.core.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    @SerialName("password_confirmation") val passwordConfirmation: String
)

@Serializable
data class UserDto(
    val id: Int,
    val name: String,
    val email: String
)

fun UserDto.toDomain() = User(id = id, name = name, email = email)

@Serializable
data class AuthResponse(
    val token: String,
    val user: UserDto,
    val message: String? = null
)

@Serializable
data class RefreshResponse(
    val token: String
)

@Serializable
data class ValidateResponse(
    val user: UserDto
)

@Serializable
data class MessageResponse(
    val message: String? = null
)

@Serializable
data class ApiErrorBody(
    val message: String? = null,
    val error: String? = null,
    val errors: Map<String, List<String>>? = null
)
