package com.lucasdias.gametrackr.core.ui.theme

import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.core.model.ActivityKind
import com.lucasdias.gametrackr.core.model.Profile
import com.lucasdias.gametrackr.core.model.ProfileColor
import com.lucasdias.gametrackr.core.model.ProfileVisibility
import com.lucasdias.gametrackr.core.ui.icon.AppIcon

private const val AVATAR_GRADIENT_DEPTH = 0.28f

val Profile.avatarStart: Color get() = avatarHex.toAppColor()

val Profile.avatarEnd: Color get() = avatarHex.toAppColor().darkened(AVATAR_GRADIENT_DEPTH)

val ProfileColor.color: Color get() = hex.toAppColor()

val ProfileVisibility.icon: AppIcon
    get() =
        when (this) {
            ProfileVisibility.PUBLIC -> AppIcon.EYE
            ProfileVisibility.PRIVATE -> AppIcon.EYE_SLASH
        }

val ActivityKind.icon: AppIcon
    get() =
        when (this) {
            ActivityKind.PLATINUM -> AppIcon.MEDAL
            ActivityKind.COMPLETED -> AppIcon.SUCCESS
            ActivityKind.STARTED -> AppIcon.BRAND
            ActivityKind.RATED -> AppIcon.LIKE
            ActivityKind.ADDED -> AppIcon.ADD_TO_LIBRARY
        }

val ActivityKind.tint: Color
    get() =
        when (this) {
            ActivityKind.PLATINUM, ActivityKind.STARTED -> AppPrimary
            ActivityKind.COMPLETED -> AppSecondary
            ActivityKind.RATED -> AppTertiary
            ActivityKind.ADDED -> AppTextSecondary
        }
