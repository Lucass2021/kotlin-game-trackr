package com.lucasdias.gametrackr.feature.app.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.network.GameApi
import com.lucasdias.gametrackr.core.network.dto.toDomain
import com.lucasdias.gametrackr.core.pagination.PaginationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val api: GameApi,
) : ViewModel() {
    val pagination = PaginationState<Game>()

    val games get() = pagination.items

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasLoaded = MutableStateFlow(false)
    val hasLoaded: StateFlow<Boolean> = _hasLoaded.asStateFlow()

    private val _hasError = MutableStateFlow(false)
    val hasError: StateFlow<Boolean> = _hasError.asStateFlow()

    private var filterFetches by mutableIntStateOf(0)

    val canFetchMoreForFilter: Boolean
        get() = pagination.canLoadMore && filterFetches < FILTER_FETCH_BUDGET

    fun resetFilterBudget() {
        filterFetches = 0
    }

    fun loadNewReleases(reset: Boolean = true) {
        if (reset) {
            if (_isLoading.value) return
            _isLoading.value = true
            _hasError.value = false
            pagination.reset()
            pagination.setLoading(true)
        } else {
            if (!pagination.canLoadMore) return
            pagination.setLoading(true)
        }

        val nextPage = pagination.currentPage + 1

        viewModelScope.launch {
            try {
                val response = api.getAllNewReleases(page = nextPage, perPage = PER_PAGE)
                pagination.append(response.toPaginated(), response.data.map { it.toDomain() })
            } catch (_: Exception) {
                if (reset) _hasError.value = true
            }
            if (reset) {
                _isLoading.value = false
                _hasLoaded.value = true
            }
            pagination.setLoading(false)
        }
    }

    fun loadMoreNewReleases() = loadNewReleases(reset = false)

    fun loadMoreForFilter() {
        filterFetches += 1
        loadMoreNewReleases()
    }

    private companion object {
        const val PER_PAGE = 20
        const val FILTER_FETCH_BUDGET = 5
    }
}
