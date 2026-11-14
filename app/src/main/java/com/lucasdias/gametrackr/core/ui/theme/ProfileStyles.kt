package com.lucasdias.gametrackr.core.ui.theme

import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.core.model.ActivityKind
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.model.Profile
import com.lucasdias.gametrackr.core.model.ProfileColor
import com.lucasdias.gametrackr.core.model.ProfileVisibility
import com.lucasdias.gametrackr.core.model.SetupPalette
import com.lucasdias.gametrackr.core.ui.icon.AppIcon

private const val AVATAR_GRADIENT_DEPTH = 0.28f
private const val WISHLIST_TINT_ALPHA = 0.55f

val Profile.avatarStart: Color get() = avatarHex.toAppColor()

val Profile.avatarEnd: Color get() = avatarHex.toAppColor().darkened(AVATAR_GRADIENT_DEPTH)

val ProfileColor.color: Color get() = hex.toAppColor()

val ProfileVisibility.icon: AppIcon
    get() =
        when (this) {
            ProfileVisibility.PUBLIC -> AppIcon.EYE
            ProfileVisibility.PRIVATE -> AppIcon.EYE_SLASH
        }

val LibraryStatus.barTint: Color
    get() =
        when (this) {
            LibraryStatus.PLAYING -> AppPrimary
            LibraryStatus.COMPLETED -> AppSecondary
            LibraryStatus.BACKLOG -> AppTextSecondary
            LibraryStatus.PLATINUM -> AppTextPrimary
            LibraryStatus.ABANDONED -> AppTertiary
            LibraryStatus.WISHLIST -> AppSecondary.copy(alpha = WISHLIST_TINT_ALPHA)
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

val SetupPalette.start: Color
    get() =
        when (this) {
            SetupPalette.VIOLET -> CoverVioletStart
            SetupPalette.EMERALD -> CoverEmeraldStart
            SetupPalette.CRIMSON -> CoverCrimsonStart
            SetupPalette.INDIGO -> CoverIndigoStart
            SetupPalette.AZURE -> CoverAzureStart
            SetupPalette.CYAN -> CoverCyanStart
            SetupPalette.PINE -> CoverPineStart
        }

val SetupPalette.end: Color
    get() =
        when (this) {
            SetupPalette.VIOLET -> CoverVioletEnd
            SetupPalette.EMERALD -> CoverEmeraldEnd
            SetupPalette.CRIMSON -> CoverCrimsonEnd
            SetupPalette.INDIGO -> CoverIndigoEnd
            SetupPalette.AZURE -> CoverAzureEnd
            SetupPalette.CYAN -> CoverCyanEnd
            SetupPalette.PINE -> CoverPineEnd
        }
