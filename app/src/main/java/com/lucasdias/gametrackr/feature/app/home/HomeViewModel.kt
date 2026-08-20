package com.lucasdias.gametrackr.feature.app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.network.GameApi
import com.lucasdias.gametrackr.core.network.dto.toDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val api: GameApi,
) : ViewModel() {
    private val _newReleases = MutableStateFlow<List<Game>>(emptyList())
    val newReleases: StateFlow<List<Game>> = _newReleases.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasLoaded = MutableStateFlow(false)
    val hasLoaded: StateFlow<Boolean> = _hasLoaded.asStateFlow()

    private val _hasError = MutableStateFlow(false)
    val hasError: StateFlow<Boolean> = _hasError.asStateFlow()

    fun loadNewReleases() {
        if (_isLoading.value) return
        _isLoading.value = true
        _hasError.value = false

        viewModelScope.launch {
            try {
                _newReleases.value = api.getNewReleases(limit = NEW_RELEASES_LIMIT).data.map { it.toDomain() }
            } catch (_: Exception) {
                _hasError.value = true
            }
            _isLoading.value = false
            _hasLoaded.value = true
        }
    }

    private companion object {
        const val NEW_RELEASES_LIMIT = 10
    }
}
