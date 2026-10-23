package com.lucasdias.gametrackr.feature.app.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.model.GamePlatform
import com.lucasdias.gametrackr.core.pagination.FeedCache
import com.lucasdias.gametrackr.core.pagination.FeedKey
import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.core.pagination.PaginationSnapshot
import com.lucasdias.gametrackr.data.game.GameRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    scope: SearchScope,
    private val repository: GameRepository,
    private val cache: FeedCache = FeedCache(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState(scope = scope))
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var pageJob: Job? = null
    private var searchJob: Job? = null
    private var appliedKey = FeedKey(scope, search = null, platform = null)

    fun loadIfNeeded() {
        if (_uiState.value.platforms.isEmpty()) loadPlatforms()
        val state = _uiState.value
        if (state.currentPage > 0 || state.isLoading) return
        load()
    }

    fun load() {
        searchJob?.cancel()
        _uiState.update { it.copy(isSearchPending = false) }
        applyFilters()
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.currentPage == 0 || state.currentPage >= state.lastPage) return
        if (state.isLoading || state.isLoadingMore || state.isSearchPending) return
        loadPage(state.currentPage + 1)
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query, isSearchPending = true) }
        searchJob?.cancel()
        searchJob =
            viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_MS)
                _uiState.update { it.copy(isSearchPending = false) }
                if (currentKey() != appliedKey || _uiState.value.hasError) applyFilters()
            }
    }

    fun onPlatformChange(platformSlug: String?) {
        if (platformSlug == _uiState.value.platformSlug) return
        _uiState.update { it.copy(platformSlug = platformSlug) }
        load()
    }

    fun onClearFilters() {
        _uiState.update { it.copy(query = "", platformSlug = null) }
        load()
    }

    private fun loadPlatforms() {
        viewModelScope.launch {
            repository.fetchPlatforms().onSuccess { platforms -> _uiState.update { it.copy(platforms = platforms) } }
        }
    }

    private fun currentKey(): FeedKey {
        val state = _uiState.value
        return FeedKey(
            scope = state.scope,
            search = state.query.trim().ifBlank { null },
            platform = state.platforms.firstOrNull { it.slug == state.platformSlug },
        )
    }

    private fun applyFilters() {
        appliedKey = currentKey()
        val cached = cache.snapshot(appliedKey)
        if (cached == null) {
            loadPage(1)
            return
        }
        pageJob?.cancel()
        _uiState.update { it.withSnapshot(cached).copy(isLoading = false, isLoadingMore = false, hasError = false, loadMoreFailed = false) }
    }

    private fun loadPage(page: Int) {
        pageJob?.cancel()
        _uiState.update {
            if (page == 1) {
                it.copy(games = emptyList(), currentPage = 0, lastPage = 1, total = 0, isLoading = true, isLoadingMore = false)
            } else {
                it.copy(isLoadingMore = true)
            }.copy(hasError = false, loadMoreFailed = false)
        }
        val key = appliedKey
        pageJob =
            viewModelScope.launch {
                fetch(key, page)
                    .onSuccess { result ->
                        _uiState.update { state ->
                            val games = if (page == 1) result.items else state.games + result.items
                            state
                                .withSnapshot(PaginationSnapshot(games, result.currentPage, result.lastPage, result.total))
                                .copy(isLoading = false, isLoadingMore = false)
                        }
                        cache.store(key, _uiState.value.snapshot())
                    }.onFailure {
                        _uiState.update {
                            it.copy(isLoading = false, isLoadingMore = false, hasError = page == 1, loadMoreFailed = page > 1)
                        }
                    }
            }
    }

    private suspend fun fetch(
        key: FeedKey,
        page: Int,
    ): Result<Page<Game>> {
        val platformSlug = key.platform?.slug
        return when (key.scope) {
            SearchScope.MOST_ANTICIPATED -> repository.fetchAllMostAnticipated(page, SearchUiState.PAGE_SIZE, key.search, platformSlug)
            SearchScope.NEW_RELEASES -> repository.fetchAllNewReleases(page, SearchUiState.PAGE_SIZE, key.search, platformSlug)
            SearchScope.ALL -> repository.searchGames(page, SearchUiState.PAGE_SIZE, key.search, platformSlug)
        }
    }

    private fun SearchUiState.withSnapshot(snapshot: PaginationSnapshot<Game>): SearchUiState =
        copy(games = snapshot.items, currentPage = snapshot.currentPage, lastPage = snapshot.lastPage, total = snapshot.total)

    private fun SearchUiState.snapshot(): PaginationSnapshot<Game> = PaginationSnapshot(games, currentPage, lastPage, total)

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 300L
    }
}
