package com.lucasdias.gametrackr.core.network

import com.lucasdias.gametrackr.core.network.dto.ProfileColorsResponse
import com.lucasdias.gametrackr.core.network.dto.UpdateProfileRequest
import com.lucasdias.gametrackr.core.network.dto.ValidateResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface ProfileApi {
    @GET("profile/colors")
    suspend fun getColors(): ProfileColorsResponse

    @PATCH("profile")
    suspend fun update(
        @Body body: UpdateProfileRequest,
    ): ValidateResponse
}
