package com.lucasdias.gametrackr.data.stats

import com.lucasdias.gametrackr.core.model.UserStats
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeStatsRepository(
    private val stats: UserStats = StatsMockData.stats,
    var failure: Throwable? = null,
) : StatsRepository {
    var fetchCount = 0
        private set

    override suspend fun fetchStats(): Result<UserStats> {
        fetchCount++
        val error = failure
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return error?.let { Result.failure(it) } ?: Result.success(stats)
    }
}
