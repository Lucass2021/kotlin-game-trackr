package com.lucasdias.gametrackr.data.stats

import com.lucasdias.gametrackr.core.model.UserStats

interface StatsRepository {
    suspend fun fetchStats(): Result<UserStats>
}
