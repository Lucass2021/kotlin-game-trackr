package com.lucasdias.gametrackr.feature.app.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.data.achievements.AchievementsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameAchievementsViewModel(
    private val repository: AchievementsRepository,
    private val gameId: Long,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GameAchievementsUiState())
    val uiState: StateFlow<GameAchievementsUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    fun loadIfNeeded() {
        val state = _uiState.value
        if (state.game != null || state.isLoading) return
        load()
    }

    fun load() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob =
            viewModelScope.launch {
                repository
                    .fetchGameAchievements(gameId)
                    .onSuccess { game -> _uiState.update { it.copy(game = game, isLoading = false) } }
                    .onFailure { _uiState.update { it.copy(isLoading = false, hasError = true) } }
            }
    }
}
