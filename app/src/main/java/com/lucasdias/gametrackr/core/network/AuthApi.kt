package com.lucasdias.gametrackr.core.network

import com.lucasdias.gametrackr.core.network.dto.AuthResponse
import com.lucasdias.gametrackr.core.network.dto.ForgotPasswordRequest
import com.lucasdias.gametrackr.core.network.dto.LoginRequest
import com.lucasdias.gametrackr.core.network.dto.MessageResponse
import com.lucasdias.gametrackr.core.network.dto.RegisterRequest
import com.lucasdias.gametrackr.core.network.dto.ResetPasswordRequest
import com.lucasdias.gametrackr.core.network.dto.ValidateResponse
import com.lucasdias.gametrackr.core.network.dto.VerifyResetCodeRequest
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

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body body: ForgotPasswordRequest): MessageResponse

    @POST("auth/verify-reset-code")
    suspend fun verifyResetCode(@Body body: VerifyResetCodeRequest): MessageResponse

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body body: ResetPasswordRequest): MessageResponse
}
