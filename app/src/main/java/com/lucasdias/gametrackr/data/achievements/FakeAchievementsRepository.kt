package com.lucasdias.gametrackr.data.achievements

import com.lucasdias.gametrackr.core.model.AchievementsSummary
import com.lucasdias.gametrackr.core.model.GameAchievements
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.core.pagination.fakePage
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeAchievementsRepository : AchievementsRepository {
    override suspend fun fetchAchievementGames(
        page: Int,
        perPage: Int,
    ): Result<Page<GameAchievements>> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(fakePage(AchievementsMockData.games, page, perPage))
    }

    override suspend fun fetchAchievementsSummary(): Result<AchievementsSummary> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(AchievementsSummary.from(AchievementsMockData.games))
    }

    override suspend fun fetchGameAchievements(id: Long): Result<GameAchievements> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        val game = AchievementsMockData.games.firstOrNull { it.id == id } ?: return Result.failure(ApiError.NotFound)
        return Result.success(game)
    }
}
