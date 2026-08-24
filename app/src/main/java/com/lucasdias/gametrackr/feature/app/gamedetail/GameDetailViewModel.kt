package com.lucasdias.gametrackr.feature.app.gamedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.core.network.GameApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameDetailViewModel(
    private val api: GameApi,
    private val slug: String,
) : ViewModel() {
    private val _game = MutableStateFlow<GameDetail?>(null)
    val game: StateFlow<GameDetail?> = _game.asStateFlow()

    private val _hasError = MutableStateFlow(false)
    val hasError: StateFlow<Boolean> = _hasError.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            try {
                _game.value = api.getGame(slug).data.toDomain()
            } catch (_: Exception) {
                _hasError.value = true
            }
        }
    }
}
