package com.lucasdias.gametrackr.core.model

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.R

enum class LibraryStatus(
    @param:StringRes val labelRes: Int,
) {
    PLAYING(R.string.library_status_playing),
    COMPLETED(R.string.library_status_completed),
    BACKLOG(R.string.library_status_backlog),
    PLATINUM(R.string.library_status_platinum),
    ABANDONED(R.string.library_status_abandoned),
    WISHLIST(R.string.library_status_wishlist),
}

data class LibraryEntry(
    val title: String,
    val slug: String,
    val status: LibraryStatus,
    val rating: Int,
    val hours: Int,
    val cover: CoverPalette,
    val isFavorite: Boolean = false,
)

data class LibraryStats(
    val totalGames: Int,
    val platinum: Int,
)

data class NewLibraryEntry(
    val title: String,
    val slug: String,
    val status: LibraryStatus,
    val rating: Int,
    val hours: Int?,
    val platform: String?,
    val startedAt: Long?,
    val finishedAt: Long?,
    val review: String?,
    val cover: CoverPalette,
)
