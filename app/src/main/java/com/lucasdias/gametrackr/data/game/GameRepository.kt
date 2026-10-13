package com.lucasdias.gametrackr.data.game

import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.feature.app.gamedetail.GameDetail

interface GameRepository {
    suspend fun fetchNewReleases(limit: Int): Result<List<Game>>

    suspend fun fetchMostAnticipated(limit: Int): Result<List<Game>>

    suspend fun fetchGame(slug: String): Result<GameDetail>
}
