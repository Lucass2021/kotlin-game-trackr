package com.lucasdias.gametrackr.feature.app.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.core.model.AchievementFilter
import com.lucasdias.gametrackr.data.achievements.AchievementsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AchievementsViewModel(
    private val repository: AchievementsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AchievementsUiState())
    val uiState: StateFlow<AchievementsUiState> = _uiState.asStateFlow()

    private var listJob: Job? = null
    private var summaryJob: Job? = null

    fun loadIfNeeded() {
        val state = _uiState.value
        if (state.currentPage > 0 || state.isLoading) return
        load()
    }

    fun load() {
        loadPage(1)
        if (_uiState.value.summary == null) loadSummary()
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.currentPage == 0 || state.currentPage >= state.lastPage || state.isLoading || state.isLoadingMore) return
        loadPage(state.currentPage + 1)
    }

    fun onFilterChange(filter: AchievementFilter) {
        if (filter == _uiState.value.filter) return
        _uiState.update { it.copy(filter = filter) }
        loadPage(1)
    }

    private fun loadPage(page: Int) {
        listJob?.cancel()
        val filter = _uiState.value.filter
        _uiState.update {
            if (page == 1) {
                it.copy(games = emptyList(), currentPage = 0, lastPage = 1, isLoading = true, isLoadingMore = false)
            } else {
                it.copy(isLoadingMore = true)
            }.copy(hasError = false, loadMoreFailed = false)
        }
        listJob =
            viewModelScope.launch {
                repository
                    .fetchAchievementGames(filter, page, AchievementsUiState.PAGE_SIZE)
                    .onSuccess { result ->
                        _uiState.update {
                            it.copy(
                                games = if (page == 1) result.items else it.games + result.items,
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

    private fun loadSummary() {
        summaryJob?.cancel()
        summaryJob =
            viewModelScope.launch {
                repository.fetchAchievementsSummary().onSuccess { summary -> _uiState.update { it.copy(summary = summary) } }
            }
    }
}
