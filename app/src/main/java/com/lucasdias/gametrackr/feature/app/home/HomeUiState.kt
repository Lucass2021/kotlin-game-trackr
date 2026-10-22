package com.lucasdias.gametrackr.feature.app.home

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.core.model.Game

enum class HomeSection {
    NEW_RELEASES,
    MOST_ANTICIPATED,
}

data class HomeFeed(
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val hasLoaded: Boolean = false,
    val hasError: Boolean = false,
) {
    val isWaiting: Boolean
        get() = games.isEmpty() && (isLoading || !hasLoaded)
}

data class HomeUiState(
    val newReleases: HomeFeed = HomeFeed(),
    val mostAnticipated: HomeFeed = HomeFeed(),
    val isRefreshing: Boolean = false,
    @param:StringRes val toastMessage: Int? = null,
) {
    fun feed(section: HomeSection): HomeFeed =
        when (section) {
            HomeSection.NEW_RELEASES -> newReleases
            HomeSection.MOST_ANTICIPATED -> mostAnticipated
        }
}
