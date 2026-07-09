package com.lucasdias.gametrackr.feature.app.library

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary

enum class LibraryStatus(
    @param:StringRes val labelRes: Int,
) {
    PLAYING(R.string.library_status_playing),
    COMPLETED(R.string.library_status_completed),
    BACKLOG(R.string.library_status_backlog),
    PLATINUM(R.string.library_status_platinum),
    ABANDONED(R.string.library_status_abandoned),
    WISHLIST(R.string.library_status_wishlist),
    ;

    val badgeBackground: Color
        get() =
            when (this) {
                PLAYING, PLATINUM -> AppPrimary
                COMPLETED -> AppSecondary
                BACKLOG -> AppOutline
                ABANDONED -> AppTertiary.copy(alpha = 0.18f)
                WISHLIST -> AppSecondary.copy(alpha = 0.18f)
            }

    val badgeForeground: Color
        get() =
            when (this) {
                PLAYING, COMPLETED, PLATINUM -> AppOnPrimary
                BACKLOG -> AppTextSecondary
                ABANDONED -> AppTertiary
                WISHLIST -> AppSecondary
            }
}

data class LibraryEntry(
    val title: String,
    val status: LibraryStatus,
    val rating: Int,
    val hours: Int,
    val coverStart: Color,
    val coverEnd: Color,
)

data class LibraryStats(
    val totalGames: Int,
    val platinum: Int,
)
