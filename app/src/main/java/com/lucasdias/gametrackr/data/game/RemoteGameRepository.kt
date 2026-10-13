package com.lucasdias.gametrackr.data.game

import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.network.GameApi
import com.lucasdias.gametrackr.core.network.apiCall
import com.lucasdias.gametrackr.core.network.dto.toDomain
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
}
