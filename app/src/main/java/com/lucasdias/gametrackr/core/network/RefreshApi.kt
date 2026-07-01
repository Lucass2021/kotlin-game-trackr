package com.lucasdias.gametrackr.core.network

import com.lucasdias.gametrackr.core.network.dto.RefreshResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface RefreshApi {
    @POST("auth/refresh")
    fun refresh(
        @Header("Authorization") bearer: String,
    ): Call<RefreshResponse>
}
