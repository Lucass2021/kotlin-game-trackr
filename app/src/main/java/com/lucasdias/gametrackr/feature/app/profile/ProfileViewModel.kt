package com.lucasdias.gametrackr.feature.app.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.data.profile.ProfileOverviewRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileOverviewRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private var loadedUserId: Int? = null

    fun loadFor(userId: Int?) {
        if (userId == loadedUserId) return
        loadedUserId = userId
        loadJob?.cancel()
        _uiState.value = ProfileUiState()
        if (userId != null) load()
    }

    fun load() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob =
            viewModelScope.launch {
                repository
                    .fetchMyOverview()
                    .onSuccess { overview -> _uiState.update { it.copy(overview = overview, isLoading = false) } }
                    .onFailure { _uiState.update { it.copy(isLoading = false, hasError = true) } }
            }
    }
}
