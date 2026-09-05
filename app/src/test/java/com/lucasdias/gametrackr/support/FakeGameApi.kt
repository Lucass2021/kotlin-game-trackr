package com.lucasdias.gametrackr.support

import com.lucasdias.gametrackr.core.network.GameApi
import com.lucasdias.gametrackr.core.network.dto.GameDetailResponse
import com.lucasdias.gametrackr.core.network.dto.GameDto
import com.lucasdias.gametrackr.core.network.dto.GamePlatformDto
import com.lucasdias.gametrackr.core.network.dto.GamesMeta
import com.lucasdias.gametrackr.core.network.dto.GamesResponse
import com.lucasdias.gametrackr.core.network.dto.PaginatedGamesResponse
import com.lucasdias.gametrackr.core.network.dto.PlatformsResponse
import kotlinx.coroutines.CompletableDeferred
import java.io.IOException

class FakeGameApi(
    private val slider: List<GameDto> = emptyList(),
    private val feed: List<GameDto> = emptyList(),
    private val platforms: List<GamePlatformDto> = emptyList(),
    private val lastPage: Int = 1,
    private val failure: Throwable? = null,
    private val gate: CompletableDeferred<Unit>? = null,
) : GameApi {
    private val calls = mutableMapOf<String, Int>()

    fun callCount(name: String): Int = calls[name] ?: 0

    override suspend fun getNewReleases(limit: Int?): GamesResponse = sliderResponse("newReleases")

    override suspend fun getMostAnticipated(limit: Int?): GamesResponse = sliderResponse("mostAnticipated")

    override suspend fun getAllNewReleases(
        page: Int?,
        perPage: Int?,
        search: String?,
        platforms: List<String>?,
    ): PaginatedGamesResponse = feedResponse("allNewReleases", page)

    override suspend fun getAllMostAnticipated(
        page: Int?,
        perPage: Int?,
        search: String?,
        platforms: List<String>?,
    ): PaginatedGamesResponse = feedResponse("allMostAnticipated", page)

    override suspend fun searchGames(
        page: Int?,
        perPage: Int?,
        search: String?,
        platforms: List<String>?,
    ): PaginatedGamesResponse = feedResponse("searchGames", page)

    override suspend fun getPlatforms(limit: Int?): PlatformsResponse {
        record("platforms")
        failure?.let { throw it }
        return PlatformsResponse(data = platforms)
    }

    override suspend fun getGame(slug: String): GameDetailResponse {
        record("game")
        throw failure ?: IOException("no detail stubbed")
    }

    private suspend fun sliderResponse(name: String): GamesResponse {
        record(name)
        gate?.await()
        failure?.let { throw it }
        return GamesResponse(data = slider)
    }

    private suspend fun feedResponse(
        name: String,
        page: Int?,
    ): PaginatedGamesResponse {
        record(name)
        gate?.await()
        failure?.let { throw it }
        return PaginatedGamesResponse(
            data = feed,
            meta =
                GamesMeta(
                    page = page ?: 1,
                    perPage = 20,
                    total = feed.size * lastPage,
                    lastPage = lastPage,
                    hasMore = (page ?: 1) < lastPage,
                ),
        )
    }

    private fun record(name: String) {
        calls[name] = callCount(name) + 1
    }
}
