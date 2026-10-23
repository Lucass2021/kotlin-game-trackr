package com.lucasdias.gametrackr.feature.app.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.data.library.LibraryRepository
import com.lucasdias.gametrackr.data.library.LibraryUpdates
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val repository: LibraryRepository,
    libraryUpdates: LibraryUpdates,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    private var listJob: Job? = null
    private var statsJob: Job? = null
    private val favoriteJobs = mutableMapOf<String, Job>()
    private var loadedUserId: Int? = null

    init {
        viewModelScope.launch {
            libraryUpdates.addedSlugs.collect {
                if (loadedUserId == null) return@collect
                loadPage(1)
                loadStats()
            }
        }
    }

    fun loadFor(userId: Int?) {
        if (userId == loadedUserId) return
        loadedUserId = userId
        clear()
        if (userId != null) load()
    }

    fun load() {
        loadPage(1)
        if (_uiState.value.stats == null) loadStats()
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.currentPage == 0 || state.currentPage >= state.lastPage || state.isLoading || state.isLoadingMore) return
        loadPage(state.currentPage + 1)
    }

    fun onFilterChange(status: LibraryStatus?) {
        if (status == _uiState.value.filter) return
        _uiState.update { it.copy(filter = status) }
        if (loadedUserId != null) loadPage(1)
    }

    fun onToggleFavorite(slug: String) {
        val entry = _uiState.value.entries.firstOrNull { it.slug == slug } ?: return
        val isFavorite = !entry.isFavorite
        setFavoriteLocally(slug, isFavorite)
        favoriteJobs[slug]?.cancel()
        favoriteJobs[slug] =
            viewModelScope.launch {
                repository.setFavorite(slug, isFavorite).onFailure {
                    setFavoriteLocally(slug, !isFavorite)
                    _uiState.update { it.copy(errorMessage = R.string.library_favorite_error) }
                }
            }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun loadPage(page: Int) {
        listJob?.cancel()
        val filter = _uiState.value.filter
        _uiState.update {
            if (page == 1) {
                it.copy(entries = emptyList(), currentPage = 0, lastPage = 1, isLoading = true, isLoadingMore = false)
            } else {
                it.copy(isLoadingMore = true)
            }.copy(hasError = false, loadMoreFailed = false)
        }
        listJob =
            viewModelScope.launch {
                repository
                    .fetchLibrary(filter, page, LibraryUiState.PAGE_SIZE)
                    .onSuccess { result ->
                        _uiState.update {
                            it.copy(
                                entries = if (page == 1) result.items else it.entries + result.items,
                                currentPage = result.currentPage,
                                lastPage = result.lastPage,
                                isLoading = false,
                                isLoadingMore = false,
                            )
                        }
                    }.onFailure {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoadingMore = false,
                                hasError = page == 1,
                                loadMoreFailed = page > 1,
                            )
                        }
                    }
            }
    }

    private fun loadStats() {
        statsJob?.cancel()
        statsJob =
            viewModelScope.launch {
                repository.fetchLibraryStats().onSuccess { stats -> _uiState.update { it.copy(stats = stats) } }
            }
    }

    private fun setFavoriteLocally(
        slug: String,
        isFavorite: Boolean,
    ) {
        _uiState.update { state ->
            state.copy(entries = state.entries.map { if (it.slug == slug) it.copy(isFavorite = isFavorite) else it })
        }
    }

    private fun clear() {
        listJob?.cancel()
        statsJob?.cancel()
        favoriteJobs.values.forEach { it.cancel() }
        favoriteJobs.clear()
        _uiState.value = LibraryUiState()
    }
}
