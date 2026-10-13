package com.lucasdias.gametrackr.feature.app.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.model.GamePlatform
import com.lucasdias.gametrackr.core.pagination.FeedCache
import com.lucasdias.gametrackr.core.pagination.FeedKey
import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.core.pagination.PaginationState
import com.lucasdias.gametrackr.data.game.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: GameRepository,
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
            _platforms.value = repository.fetchPlatforms().getOrDefault(emptyList())
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
        val platformSlug = platform?.slug

        viewModelScope.launch {
            val result = fetchPage(nextPage, search, platformSlug)
            if (requestGeneration != generation) return@launch
            result
                .onSuccess { page ->
                    pagination.append(page)
                    cache.store(key, pagination.snapshot())
                }.onFailure { if (reset) _hasError.value = true }
            if (reset) {
                _isLoading.value = false
                _hasLoaded.value = true
            }
            pagination.setLoading(false)
        }
    }

    private suspend fun fetchPage(
        page: Int,
        search: String?,
        platformSlug: String?,
    ): Result<Page<Game>> =
        when (scope) {
            SearchScope.MOST_ANTICIPATED -> repository.fetchAllMostAnticipated(page, PER_PAGE, search, platformSlug)
            SearchScope.NEW_RELEASES -> repository.fetchAllNewReleases(page, PER_PAGE, search, platformSlug)
            SearchScope.ALL -> repository.searchGames(page, PER_PAGE, search, platformSlug)
        }

    private companion object {
        const val PER_PAGE = 20
    }
}
