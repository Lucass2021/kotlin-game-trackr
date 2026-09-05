package com.lucasdias.gametrackr.core.network

import com.lucasdias.gametrackr.core.network.dto.GameDetailResponse
import com.lucasdias.gametrackr.core.network.dto.GamesResponse
import com.lucasdias.gametrackr.core.network.dto.PaginatedGamesResponse
import com.lucasdias.gametrackr.core.network.dto.PlatformsResponse
import retrofit2.http.GET
import retrofit2.http.Path
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
        @Query("search") search: String? = null,
        @Query("platform[]") platforms: List<String>? = null,
    ): PaginatedGamesResponse

    @GET("home/most-anticipated")
    suspend fun getMostAnticipated(
        @Query("limit") limit: Int? = null,
    ): GamesResponse

    @GET("home/most-anticipated/all")
    suspend fun getAllMostAnticipated(
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("search") search: String? = null,
        @Query("platform[]") platforms: List<String>? = null,
    ): PaginatedGamesResponse

    @GET("platforms")
    suspend fun getPlatforms(
        @Query("limit") limit: Int? = null,
    ): PlatformsResponse

    @GET("games/{slug}")
    suspend fun getGame(
        @Path("slug") slug: String,
    ): GameDetailResponse
}
