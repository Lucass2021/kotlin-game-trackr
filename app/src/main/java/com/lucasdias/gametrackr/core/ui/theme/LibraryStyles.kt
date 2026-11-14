package com.lucasdias.gametrackr.core.ui.theme

import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.core.model.LibraryStatus

private const val SOFT_BADGE_ALPHA = 0.18f
private const val WISHLIST_TINT_ALPHA = 0.55f

val LibraryStatus.badgeBackground: Color
    get() =
        when (this) {
            LibraryStatus.PLAYING, LibraryStatus.PLATINUM -> AppPrimary
            LibraryStatus.COMPLETED -> AppSecondary
            LibraryStatus.BACKLOG -> AppOutline
            LibraryStatus.ABANDONED -> AppTertiary.copy(alpha = SOFT_BADGE_ALPHA)
            LibraryStatus.WISHLIST -> AppSecondary.copy(alpha = SOFT_BADGE_ALPHA)
        }

val LibraryStatus.badgeForeground: Color
    get() =
        when (this) {
            LibraryStatus.PLAYING, LibraryStatus.COMPLETED, LibraryStatus.PLATINUM -> AppOnPrimary
            LibraryStatus.BACKLOG -> AppTextSecondary
            LibraryStatus.ABANDONED -> AppTertiary
            LibraryStatus.WISHLIST -> AppSecondary
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
