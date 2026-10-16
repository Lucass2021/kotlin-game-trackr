package com.lucasdias.gametrackr.feature.app.achievements

import com.lucasdias.gametrackr.core.model.AchievementFilter
import com.lucasdias.gametrackr.core.model.AchievementsSummary
import com.lucasdias.gametrackr.core.model.GameAchievements

data class AchievementsUiState(
    val games: List<GameAchievements> = emptyList(),
    val summary: AchievementsSummary? = null,
    val filter: AchievementFilter = AchievementFilter.ALL,
    val currentPage: Int = 0,
    val lastPage: Int = 1,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasError: Boolean = false,
    val loadMoreFailed: Boolean = false,
) {
    val canLoadMore: Boolean
        get() = currentPage in 1 until lastPage && !isLoading && !isLoadingMore && !loadMoreFailed

    val showsSummary: Boolean
        get() = summary != null && filter == AchievementFilter.ALL

    companion object {
        const val PAGE_SIZE = 3
    }
}
