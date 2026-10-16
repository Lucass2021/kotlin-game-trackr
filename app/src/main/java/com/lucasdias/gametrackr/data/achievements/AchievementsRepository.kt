package com.lucasdias.gametrackr.data.achievements

import com.lucasdias.gametrackr.core.model.AchievementFilter
import com.lucasdias.gametrackr.core.model.AchievementsSummary
import com.lucasdias.gametrackr.core.model.GameAchievements
import com.lucasdias.gametrackr.core.pagination.Page

interface AchievementsRepository {
    suspend fun fetchAchievementGames(
        filter: AchievementFilter,
        page: Int,
        perPage: Int,
    ): Result<Page<GameAchievements>>

    suspend fun fetchAchievementsSummary(): Result<AchievementsSummary>

    suspend fun fetchGameAchievements(id: Long): Result<GameAchievements>
}
