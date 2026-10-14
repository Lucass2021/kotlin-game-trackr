package com.lucasdias.gametrackr.data.stats

import com.lucasdias.gametrackr.feature.app.stats.StatsMockData
import com.lucasdias.gametrackr.feature.app.stats.UserStats
import kotlinx.coroutines.delay

private const val RESPONSE_DELAY_MS = 600L

class FakeStatsRepository : StatsRepository {
    override suspend fun fetchStats(): Result<UserStats> {
        delay(RESPONSE_DELAY_MS)
        return Result.success(StatsMockData.stats)
    }
}
