package com.lucasdias.gametrackr.data.game

import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.model.GamePlatform
import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.feature.app.gamedetail.GameDetail

interface GameRepository {
    suspend fun fetchNewReleases(limit: Int): Result<List<Game>>

    suspend fun fetchMostAnticipated(limit: Int): Result<List<Game>>

    suspend fun fetchGame(slug: String): Result<GameDetail>

    suspend fun fetchAllNewReleases(
        page: Int,
        perPage: Int,
        search: String?,
        platformSlug: String?,
    ): Result<Page<Game>>

    suspend fun fetchAllMostAnticipated(
        page: Int,
        perPage: Int,
        search: String?,
        platformSlug: String?,
    ): Result<Page<Game>>

    suspend fun searchGames(
        page: Int,
        perPage: Int,
        search: String?,
        platformSlug: String?,
    ): Result<Page<Game>>

    suspend fun fetchPlatforms(): Result<List<GamePlatform>>
}
