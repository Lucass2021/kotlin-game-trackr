package com.lucasdias.gametrackr.feature.app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.data.game.GameRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: GameRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val loads = mutableMapOf<HomeSection, Deferred<Boolean>>()

    fun loadIfNeeded() {
        HomeSection.entries
            .filter { !_uiState.value.feed(it).hasLoaded && loads[it] == null }
            .forEach(::startLoad)
    }

    fun refresh() {
        if (_uiState.value.isRefreshing) return
        _uiState.update { it.copy(isRefreshing = true) }
        viewModelScope.launch {
            val failed =
                HomeSection.entries
                    .map { loads[it] ?: startLoad(it) }
                    .awaitAll()
                    .any { !it }
            val hasGames =
                HomeSection.entries.any {
                    _uiState.value
                        .feed(it)
                        .games
                        .isNotEmpty()
                }
            _uiState.update {
                it.copy(isRefreshing = false, toastMessage = if (failed && hasGames) R.string.home_refresh_error else it.toastMessage)
            }
        }
    }

    fun onRetry(section: HomeSection) {
        if (loads[section] == null) startLoad(section)
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    private fun startLoad(section: HomeSection): Deferred<Boolean> {
        updateFeed(section) { it.copy(isLoading = true, hasError = false) }
        val load = viewModelScope.async { load(section) }
        loads[section] = load
        load.invokeOnCompletion { loads.remove(section) }
        return load
    }

    private suspend fun load(section: HomeSection): Boolean {
        val result = fetch(section)
        updateFeed(section) { feed ->
            result
                .fold(
                    onSuccess = { games -> feed.copy(games = games) },
                    onFailure = { feed.copy(hasError = true) },
                ).copy(isLoading = false, hasLoaded = true)
        }
        return result.isSuccess
    }

    private suspend fun fetch(section: HomeSection): Result<List<Game>> =
        when (section) {
            HomeSection.NEW_RELEASES -> repository.fetchNewReleases(SLIDER_LIMIT)
            HomeSection.MOST_ANTICIPATED -> repository.fetchMostAnticipated(SLIDER_LIMIT)
        }

    private fun updateFeed(
        section: HomeSection,
        transform: (HomeFeed) -> HomeFeed,
    ) {
        _uiState.update { state ->
            when (section) {
                HomeSection.NEW_RELEASES -> state.copy(newReleases = transform(state.newReleases))
                HomeSection.MOST_ANTICIPATED -> state.copy(mostAnticipated = transform(state.mostAnticipated))
            }
        }
    }

    private companion object {
        const val SLIDER_LIMIT = 10
    }
}
