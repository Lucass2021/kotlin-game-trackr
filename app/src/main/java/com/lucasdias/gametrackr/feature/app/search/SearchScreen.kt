package com.lucasdias.gametrackr.feature.app.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.GamePlatform
import com.lucasdias.gametrackr.core.pagination.InfiniteGridScrollEffect
import com.lucasdias.gametrackr.core.pagination.LoadingMoreIndicator
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.search.components.SearchFilterChips
import com.lucasdias.gametrackr.feature.app.search.components.SearchResultCard
import com.lucasdias.gametrackr.feature.app.search.components.SearchResultsEmptyState
import com.lucasdias.gametrackr.feature.app.search.components.SearchTopBar
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onGameClick: (String?) -> Unit,
    scope: SearchScope = SearchScope.ALL,
    viewModel: SearchViewModel = koinViewModel(),
) {
    var query by rememberSaveable { mutableStateOf("") }
    var platformSlug by rememberSaveable { mutableStateOf<String?>(null) }

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val hasLoaded by viewModel.hasLoaded.collectAsStateWithLifecycle()
    val appliedSearch by viewModel.appliedSearch.collectAsStateWithLifecycle()
    val platforms by viewModel.platforms.collectAsStateWithLifecycle()
    val platform = platforms.firstOrNull { it.slug == platformSlug }

    val trimmedQuery = query.trim()
    var hasPendingFilter by remember { mutableStateOf(false) }

    LaunchedEffect(platform, trimmedQuery) {
        if (hasLoaded && trimmedQuery != appliedSearch) {
            hasPendingFilter = true
            delay(SEARCH_DEBOUNCE_MILLIS)
        }
        viewModel.applyFilters(scope, trimmedQuery, platform)
        hasPendingFilter = false
    }

    val games = viewModel.games
    val isStillSearching = !hasLoaded || isLoading || hasPendingFilter

    Column(modifier = Modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
        SearchTopBar(query = query, onQueryChange = { query = it }, onBack = onBack)

        SearchFilterChips(
            platforms = platforms,
            selection = platform,
            onSelect = { platformSlug = it?.slug },
            modifier = Modifier.padding(top = 2.dp, bottom = 14.dp),
        )

        val gridState = rememberLazyGridState()

        InfiniteGridScrollEffect(
            gridState = gridState,
            canLoadMore = viewModel.pagination.canLoadMore,
            onLoadMore = { viewModel.loadMore() },
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (games.isEmpty() && isStillSearching) {
                CircularProgressIndicator(
                    color = AppPrimary,
                    modifier = Modifier.align(Alignment.Center),
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = gridState,
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 6.dp, bottom = 28.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    when {
                        games.isEmpty() -> {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                SearchResultsEmptyState(
                                    query = query,
                                    onClear = {
                                        query = ""
                                        platformSlug = null
                                    },
                                )
                            }
                        }

                        else -> {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                SectionHeader(
                                    scope = scope,
                                    isSearching = trimmedQuery.isNotEmpty(),
                                    count = viewModel.pagination.total,
                                )
                            }
                            items(games.size) { index ->
                                SearchResultCard(
                                    game = games[index],
                                    modifier = Modifier.clickable { onGameClick(games[index].slug) },
                                )
                            }
                            if (viewModel.pagination.isLoadingMore) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    LoadingMoreIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    scope: SearchScope,
    isSearching: Boolean,
    count: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text =
                when {
                    isSearching -> stringResource(R.string.search_results)
                    scope.isFiltered -> stringResource(scope.titleRes)
                    else -> stringResource(R.string.search_popular)
                },
            color = AppTextPrimary,
            style = AppType.headline(22.sp),
        )
        Text(
            text = pluralStringResource(R.plurals.search_result_count, count, count),
            color = AppTextSecondary,
            style = AppType.body(14.sp),
        )
    }
}

private const val SEARCH_DEBOUNCE_MILLIS = 300L
