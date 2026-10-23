package com.lucasdias.gametrackr.feature.app.gamedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.data.game.GameRepository
import com.lucasdias.gametrackr.data.library.LibraryRepository
import com.lucasdias.gametrackr.data.library.LibraryUpdates
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameDetailViewModel(
    private val repository: GameRepository,
    private val libraryRepository: LibraryRepository,
    libraryUpdates: LibraryUpdates,
    private val slug: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GameDetailUiState())
    val uiState: StateFlow<GameDetailUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private var libraryJob: Job? = null

    init {
        viewModelScope.launch {
            libraryUpdates.addedSlugs.collect { addedSlug ->
                if (addedSlug == slug) _uiState.update { it.copy(isInLibrary = true) }
            }
        }
    }

    fun loadIfNeeded() {
        val state = _uiState.value
        if (state.game != null || state.isLoading) return
        load()
    }

    fun load() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadLibraryStatus()
        loadJob =
            viewModelScope.launch {
                repository
                    .fetchGame(slug)
                    .onSuccess { game -> _uiState.update { it.copy(game = game, isLoading = false) } }
                    .onFailure { _uiState.update { it.copy(isLoading = false, hasError = true) } }
            }
    }

    private fun loadLibraryStatus() {
        libraryJob?.cancel()
        libraryJob =
            viewModelScope.launch {
                libraryRepository.isInLibrary(slug).onSuccess { isInLibrary ->
                    _uiState.update { it.copy(isInLibrary = isInLibrary) }
                }
            }
    }

    fun onAddedToLibrary() {
        _uiState.update { it.copy(toastMessage = R.string.add_library_saved) }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}
