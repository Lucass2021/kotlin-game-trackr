package com.lucasdias.gametrackr.feature.app.achievements

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppNeutral
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import kotlin.math.roundToInt

enum class AchievementTier(
    val tint: Color,
    @param:StringRes val titleRes: Int,
) {
    BRONZE(AppNeutral, R.string.achievements_tier_bronze),
    SILVER(AppTextSecondary, R.string.achievements_tier_silver),
    GOLD(AppPrimary, R.string.achievements_tier_gold),
    PLATINUM(AppSecondary, R.string.achievements_tier_platinum),
    ;

    val icon: AppIcon get() = if (this == PLATINUM) AppIcon.TROPHY else AppIcon.MEDAL
}

enum class AchievementFilter(
    @param:StringRes val titleRes: Int,
) {
    ALL(R.string.achievements_filter_all),
    IN_PROGRESS(R.string.achievements_filter_in_progress),
    COMPLETED(R.string.achievements_filter_completed),
    ;

    fun matches(game: GameAchievements): Boolean =
        when (this) {
            ALL -> true
            IN_PROGRESS -> !game.isComplete && game.unlockedCount > 0
            COMPLETED -> game.isComplete
        }
}

data class Achievement(
    val id: Long,
    val title: String,
    val detail: String,
    val tier: AchievementTier,
    val unlockedAt: String? = null,
    val isSecret: Boolean = false,
) {
    val isUnlocked: Boolean get() = unlockedAt != null

    val isHidden: Boolean get() = isSecret && !isUnlocked
}

data class GameAchievements(
    val id: Long,
    val title: String,
    val platform: String,
    val coverStart: Color,
    val coverEnd: Color,
    val achievements: List<Achievement>,
) {
    val total: Int get() = achievements.size

    val unlockedCount: Int get() = achievements.count { it.isUnlocked }

    val fraction: Float get() = if (total == 0) 0f else unlockedCount.toFloat() / total

    val percent: Int get() = (fraction * 100).roundToInt()

    val isComplete: Boolean get() = total > 0 && unlockedCount == total

    val hasPlatinum: Boolean
        get() = achievements.any { it.tier == AchievementTier.PLATINUM && it.isUnlocked }
}

val List<GameAchievements>.unlockedTotal: Int
    get() = sumOf { it.unlockedCount }

val List<GameAchievements>.achievementTotal: Int
    get() = sumOf { it.total }

val List<GameAchievements>.platinumCount: Int
    get() = count { it.hasPlatinum }

val List<GameAchievements>.completedCount: Int
    get() = count { it.isComplete }

val List<GameAchievements>.unlockedFraction: Float
    get() = if (achievementTotal == 0) 0f else unlockedTotal.toFloat() / achievementTotal

val List<GameAchievements>.unlockedPercent: Int
    get() = (unlockedFraction * 100).roundToInt()
