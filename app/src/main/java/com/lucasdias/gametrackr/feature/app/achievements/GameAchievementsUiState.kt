package com.lucasdias.gametrackr.feature.app.achievements

import com.lucasdias.gametrackr.core.model.Achievement
import com.lucasdias.gametrackr.core.model.GameAchievements

data class GameAchievementsUiState(
    val game: GameAchievements? = null,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
) {
    val unlockedAchievements: List<Achievement>
        get() = game?.achievements.orEmpty().filter { it.isUnlocked }

    val lockedAchievements: List<Achievement>
        get() = game?.achievements.orEmpty().filterNot { it.isUnlocked }
}
