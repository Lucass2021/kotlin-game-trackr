package com.lucasdias.gametrackr.core.model

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.R
import kotlin.math.roundToInt

enum class AchievementTier(
    @param:StringRes val titleRes: Int,
) {
    BRONZE(R.string.achievements_tier_bronze),
    SILVER(R.string.achievements_tier_silver),
    GOLD(R.string.achievements_tier_gold),
    PLATINUM(R.string.achievements_tier_platinum),
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
    val cover: CoverPalette,
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

data class AchievementsSummary(
    val unlockedCount: Int,
    val total: Int,
    val platinumCount: Int,
    val completedCount: Int,
    val gameCount: Int,
) {
    val fraction: Float get() = if (total == 0) 0f else unlockedCount.toFloat() / total

    val percent: Int get() = (fraction * 100).roundToInt()
}
