package com.lucasdias.gametrackr.feature.app.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.model.GamePlatform
import com.lucasdias.gametrackr.core.network.GameApi
import com.lucasdias.gametrackr.core.network.dto.toDomain
import com.lucasdias.gametrackr.core.pagination.FeedCache
import com.lucasdias.gametrackr.core.pagination.FeedKey
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

    private val _appliedSearch = MutableStateFlow("")
    val appliedSearch: StateFlow<String> = _appliedSearch.asStateFlow()

    private val _platforms = MutableStateFlow<List<GamePlatform>>(emptyList())
    val platforms: StateFlow<List<GamePlatform>> = _platforms.asStateFlow()

    private val cache = FeedCache()
    private var scope: SearchScope = SearchScope.ALL
    private var platform: GamePlatform? = null
    private var generation = 0

    private val key get() = FeedKey(scope, _appliedSearch.value.ifEmpty { null }, platform)

    init {
        viewModelScope.launch {
            _platforms.value =
                runCatching { api.getPlatforms().data.mapNotNull { it.toDomain() } }
                    .getOrDefault(emptyList())
        }
    }

    fun applyFilters(
        scope: SearchScope,
        search: String,
        platform: GamePlatform?,
    ) {
        this.scope = scope
        this.platform = platform
        _appliedSearch.value = search
        generation += 1

        val cached = cache.snapshot(key)
        if (cached != null) {
            pagination.restore(cached)
            pagination.setLoading(false)
            _isLoading.value = false
            _hasError.value = false
            _hasLoaded.value = true
            return
        }

        load(reset = true)
    }

    fun loadMore() = load(reset = false)

    private fun load(reset: Boolean) {
        if (reset) {
            _isLoading.value = true
            _hasError.value = false
            pagination.reset()
        } else {
            if (!pagination.canLoadMore) return
        }
        pagination.setLoading(true)

        val requestGeneration = generation
        val nextPage = pagination.currentPage + 1
        val search = _appliedSearch.value.ifEmpty { null }
        val platformSlugs = platform?.let { listOf(it.slug) }

        viewModelScope.launch {
            try {
                val response =
                    when (scope) {
                        SearchScope.MOST_ANTICIPATED -> {
                            api.getAllMostAnticipated(nextPage, PER_PAGE, search, platformSlugs)
                        }

                        SearchScope.NEW_RELEASES -> {
                            api.getAllNewReleases(nextPage, PER_PAGE, search, platformSlugs)
                        }

                        SearchScope.ALL -> {
                            api.searchGames(nextPage, PER_PAGE, search, platformSlugs)
                        }
                    }
                if (requestGeneration != generation) return@launch
                pagination.append(response.toPaginated(), response.data.map { it.toDomain() })
                cache.store(key, pagination.snapshot())
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
