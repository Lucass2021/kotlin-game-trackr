package com.lucasdias.gametrackr.core.ui.theme

import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.core.model.AchievementTier
import com.lucasdias.gametrackr.core.ui.icon.AppIcon

val AchievementTier.tint: Color
    get() =
        when (this) {
            AchievementTier.BRONZE -> AppNeutral
            AchievementTier.SILVER -> AppTextSecondary
            AchievementTier.GOLD -> AppPrimary
            AchievementTier.PLATINUM -> AppSecondary
        }

val AchievementTier.icon: AppIcon get() = if (this == AchievementTier.PLATINUM) AppIcon.TROPHY else AppIcon.MEDAL
