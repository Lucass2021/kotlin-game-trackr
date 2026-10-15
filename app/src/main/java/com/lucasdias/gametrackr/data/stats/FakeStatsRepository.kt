package com.lucasdias.gametrackr.data.stats

import com.lucasdias.gametrackr.core.model.UserStats
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeStatsRepository : StatsRepository {
    override suspend fun fetchStats(): Result<UserStats> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(StatsMockData.stats)
    }
}
