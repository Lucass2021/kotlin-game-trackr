package com.lucasdias.gametrackr.data.achievements

import com.lucasdias.gametrackr.core.model.AchievementFilter
import com.lucasdias.gametrackr.core.model.AchievementsSummary
import com.lucasdias.gametrackr.core.model.GameAchievements
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.core.pagination.fakePage
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeAchievementsRepository(
    private val games: List<GameAchievements> = AchievementsMockData.games,
    var failure: Throwable? = null,
) : AchievementsRepository {
    var fetchCount = 0
        private set
    var gameFetchCount = 0
        private set

    override suspend fun fetchAchievementGames(
        filter: AchievementFilter,
        page: Int,
        perPage: Int,
    ): Result<Page<GameAchievements>> {
        fetchCount++
        return respond { fakePage(games.filter { it.matches(filter) }, page, perPage) }
    }

    override suspend fun fetchAchievementsSummary(): Result<AchievementsSummary> = respond { summaryOf(games) }

    override suspend fun fetchGameAchievements(id: Long): Result<GameAchievements> {
        gameFetchCount++
        return respond { games.firstOrNull { it.id == id } ?: throw ApiError.NotFound }
    }

    private fun GameAchievements.matches(filter: AchievementFilter): Boolean =
        when (filter) {
            AchievementFilter.ALL -> true
            AchievementFilter.IN_PROGRESS -> !isComplete && unlockedCount > 0
            AchievementFilter.COMPLETED -> isComplete
        }

    private fun summaryOf(games: List<GameAchievements>) =
        AchievementsSummary(
            unlockedCount = games.sumOf { it.unlockedCount },
            total = games.sumOf { it.total },
            platinumCount = games.count { it.hasPlatinum },
            completedCount = games.count { it.isComplete },
            gameCount = games.size,
        )

    private suspend fun <T> respond(block: () -> T): Result<T> {
        val error = failure
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return error?.let { Result.failure(it) } ?: runCatching(block)
    }
}
