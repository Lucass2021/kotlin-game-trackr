package com.lucasdias.gametrackr.data.stats

import com.lucasdias.gametrackr.data.FakeLatency
import com.lucasdias.gametrackr.feature.app.stats.StatsMockData
import com.lucasdias.gametrackr.feature.app.stats.UserStats
import kotlinx.coroutines.delay

class FakeStatsRepository : StatsRepository {
    override suspend fun fetchStats(): Result<UserStats> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(StatsMockData.stats)
    }
}
