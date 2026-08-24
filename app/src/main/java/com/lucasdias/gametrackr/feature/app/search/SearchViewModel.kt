package com.lucasdias.gametrackr.feature.app.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.model.GamePlatform
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

    private var search: String? = null
    private var platform: GamePlatform? = null
    private var generation = 0

    fun applyFilters(
        search: String,
        platform: GamePlatform?,
    ) {
        this.search = search.ifEmpty { null }
        this.platform = platform
        load(reset = true)
    }

    fun loadMore() = load(reset = false)

    private fun load(reset: Boolean) {
        if (reset) {
            generation += 1
            _isLoading.value = true
            _hasError.value = false
            pagination.reset()
        } else {
            if (!pagination.canLoadMore) return
        }
        pagination.setLoading(true)

        val requestGeneration = generation
        val nextPage = pagination.currentPage + 1

        viewModelScope.launch {
            try {
                val response =
                    api.getAllNewReleases(
                        page = nextPage,
                        perPage = PER_PAGE,
                        search = search,
                        platforms = platform?.igdbSlugs,
                    )
                if (requestGeneration != generation) return@launch
                pagination.append(response.toPaginated(), response.data.map { it.toDomain() })
            } catch (_: Exception) {
                if (requestGeneration != generation) return@launch
                if (reset) _hasError.value = true
            }
            if (reset) {
                _isLoading.value = false
                _hasLoaded.value = true
            }
            pagination.setLoading(false)
        }
    }

    private companion object {
        const val PER_PAGE = 20
    }
}
