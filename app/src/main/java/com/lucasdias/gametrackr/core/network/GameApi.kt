package com.lucasdias.gametrackr.core.network

import com.lucasdias.gametrackr.core.network.dto.GamesResponse
import com.lucasdias.gametrackr.core.network.dto.PaginatedGamesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GameApi {
    @GET("home/new-releases")
    suspend fun getNewReleases(
        @Query("limit") limit: Int? = null,
    ): GamesResponse

    @GET("home/new-releases/all")
    suspend fun getAllNewReleases(
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null,
    ): PaginatedGamesResponse
}
