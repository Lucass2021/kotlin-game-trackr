package com.lucasdias.gametrackr.core.network

import com.lucasdias.gametrackr.core.network.dto.AuthResponse
import com.lucasdias.gametrackr.core.network.dto.LoginRequest
import com.lucasdias.gametrackr.core.network.dto.MessageResponse
import com.lucasdias.gametrackr.core.network.dto.RegisterRequest
import com.lucasdias.gametrackr.core.network.dto.ValidateResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    @POST("auth/validate")
    suspend fun validate(): ValidateResponse

    @POST("auth/logout")
    suspend fun logout(): MessageResponse

    @GET("profile/me")
    suspend fun me(): ValidateResponse
}
