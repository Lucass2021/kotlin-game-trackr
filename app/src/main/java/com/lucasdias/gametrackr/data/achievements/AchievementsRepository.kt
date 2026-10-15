package com.lucasdias.gametrackr.data.achievements

import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.feature.app.achievements.AchievementsSummary
import com.lucasdias.gametrackr.feature.app.achievements.GameAchievements

interface AchievementsRepository {
    suspend fun fetchAchievementGames(
        page: Int,
        perPage: Int,
    ): Result<Page<GameAchievements>>

    suspend fun fetchAchievementsSummary(): Result<AchievementsSummary>

    suspend fun fetchGameAchievements(id: Long): Result<GameAchievements>
}
