package com.lucasdias.gametrackr.data.stats

import com.lucasdias.gametrackr.feature.app.stats.UserStats

interface StatsRepository {
    suspend fun fetchStats(): Result<UserStats>
}
