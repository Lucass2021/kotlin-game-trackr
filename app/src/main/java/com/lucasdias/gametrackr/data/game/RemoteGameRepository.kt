package com.lucasdias.gametrackr.data.game

import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.model.GamePlatform
import com.lucasdias.gametrackr.core.network.GameApi
import com.lucasdias.gametrackr.core.network.apiCall
import com.lucasdias.gametrackr.core.network.dto.PaginatedGamesResponse
import com.lucasdias.gametrackr.core.network.dto.toDomain
import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.feature.app.gamedetail.GameDetail
import com.lucasdias.gametrackr.feature.app.gamedetail.toDomain
import kotlinx.serialization.json.Json

class RemoteGameRepository(
    private val api: GameApi,
    private val json: Json,
) : GameRepository {
    override suspend fun fetchNewReleases(limit: Int): Result<List<Game>> = apiCall(json) { api.getNewReleases(limit = limit).data.map { it.toDomain() } }

    override suspend fun fetchMostAnticipated(limit: Int): Result<List<Game>> =
        apiCall(json) { api.getMostAnticipated(limit = limit).data.map { it.toDomain() } }

    override suspend fun fetchGame(slug: String): Result<GameDetail> = apiCall(json) { api.getGame(slug).data.toDomain() }

    override suspend fun fetchAllNewReleases(
        page: Int,
        perPage: Int,
        search: String?,
        platformSlug: String?,
    ): Result<Page<Game>> = apiCall(json) { api.getAllNewReleases(page, perPage, search, platformSlug?.let(::listOf)).toPage() }

    override suspend fun fetchAllMostAnticipated(
        page: Int,
        perPage: Int,
        search: String?,
        platformSlug: String?,
    ): Result<Page<Game>> = apiCall(json) { api.getAllMostAnticipated(page, perPage, search, platformSlug?.let(::listOf)).toPage() }

    override suspend fun searchGames(
        page: Int,
        perPage: Int,
        search: String?,
        platformSlug: String?,
    ): Result<Page<Game>> = apiCall(json) { api.searchGames(page, perPage, search, platformSlug?.let(::listOf)).toPage() }

    override suspend fun fetchPlatforms(): Result<List<GamePlatform>> = apiCall(json) { api.getPlatforms().data.mapNotNull { it.toDomain() } }

    private fun PaginatedGamesResponse.toPage(): Page<Game> =
        Page(
            items = data.map { it.toDomain() },
            currentPage = meta.page,
            lastPage = meta.lastPage,
            total = meta.total,
        )
}
