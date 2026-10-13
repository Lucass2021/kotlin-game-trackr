package com.lucasdias.gametrackr.feature.app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.data.game.GameRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeFeed(
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val hasLoaded: Boolean = false,
    val hasError: Boolean = false,
)

class HomeViewModel(
    private val repository: GameRepository,
) : ViewModel() {
    private val _newReleases = MutableStateFlow(HomeFeed())
    val newReleases: StateFlow<HomeFeed> = _newReleases.asStateFlow()

    private val _mostAnticipated = MutableStateFlow(HomeFeed())
    val mostAnticipated: StateFlow<HomeFeed> = _mostAnticipated.asStateFlow()

    fun load(force: Boolean = false) {
        viewModelScope.launch {
            awaitAll(
                async { load(_newReleases, force) { repository.fetchNewReleases(SLIDER_LIMIT) } },
                async { load(_mostAnticipated, force) { repository.fetchMostAnticipated(SLIDER_LIMIT) } },
            )
        }
    }

    private suspend fun load(
        feed: MutableStateFlow<HomeFeed>,
        force: Boolean,
        fetch: suspend () -> Result<List<Game>>,
    ) {
        if (feed.value.isLoading) return
        if (!force && feed.value.hasLoaded && feed.value.games.isNotEmpty()) return
        feed.value = feed.value.copy(isLoading = true, hasError = false)

        feed.value =
            fetch()
                .fold(
                    onSuccess = { games -> feed.value.copy(games = games) },
                    onFailure = { feed.value.copy(hasError = true) },
                ).copy(isLoading = false, hasLoaded = true)
    }

    private companion object {
        const val SLIDER_LIMIT = 10
    }
}
