package com.lucasdias.gametrackr.feature.app.profile.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.core.model.SetupItem
import com.lucasdias.gametrackr.data.profile.SetupRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MySetupViewModel(
    private val repository: SetupRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MySetupUiState())
    val uiState: StateFlow<MySetupUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private var loadedUserId: Int? = null

    fun loadFor(userId: Int?) {
        if (userId == loadedUserId) return
        loadedUserId = userId
        clear()
        if (userId != null) load()
    }

    fun load() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob =
            viewModelScope.launch {
                repository
                    .fetchSetups()
                    .onSuccess { setups -> _uiState.update { it.copy(setups = setups, isLoading = false) } }
                    .onFailure { _uiState.update { it.copy(isLoading = false, hasError = true) } }
            }
    }

    private fun clear() {
        loadJob?.cancel()
        _uiState.value = MySetupUiState()
    }

    fun onSetupSaved(setup: SetupItem) {
        _uiState.update { state ->
            val exists = state.setups.any { it.id == setup.id }
            val setups = if (exists) state.setups.map { if (it.id == setup.id) setup else it } else state.setups + setup
            state.copy(setups = setups)
        }
    }

    fun onSetupDeleted(id: Int) {
        _uiState.update { state -> state.copy(setups = state.setups.filterNot { it.id == id }) }
    }
}
