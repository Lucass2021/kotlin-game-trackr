package com.lucasdias.gametrackr.core.network

import com.lucasdias.gametrackr.core.network.dto.MessageResponse
import com.lucasdias.gametrackr.core.network.dto.ProfileColorsResponse
import com.lucasdias.gametrackr.core.network.dto.SetupResponse
import com.lucasdias.gametrackr.core.network.dto.SetupsResponse
import com.lucasdias.gametrackr.core.network.dto.UpdateProfileRequest
import com.lucasdias.gametrackr.core.network.dto.ValidateResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileApi {
    @GET("profile/colors")
    suspend fun getColors(): ProfileColorsResponse

    @PATCH("profile")
    suspend fun update(
        @Body body: UpdateProfileRequest,
    ): ValidateResponse

    @GET("profile/setups")
    suspend fun getSetups(): SetupsResponse

    @Multipart
    @POST("profile/setups")
    suspend fun createSetup(
        @Part parts: List<MultipartBody.Part>,
    ): SetupResponse

    @Multipart
    @POST("profile/setups/{id}")
    suspend fun updateSetup(
        @Path("id") id: Int,
        @Part parts: List<MultipartBody.Part>,
    ): SetupResponse

    @DELETE("profile/setups/{id}")
    suspend fun deleteSetup(
        @Path("id") id: Int,
    ): MessageResponse
}
