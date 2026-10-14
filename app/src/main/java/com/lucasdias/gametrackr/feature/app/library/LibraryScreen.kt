package com.lucasdias.gametrackr.feature.app.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.pagination.InfiniteScrollEffect
import com.lucasdias.gametrackr.core.pagination.LoadingMoreIndicator
import com.lucasdias.gametrackr.core.pagination.PaginationState
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.data.library.LibraryRepository
import com.lucasdias.gametrackr.feature.app.community.components.CommunityEmptyState
import com.lucasdias.gametrackr.feature.app.library.components.LibraryEmptyState
import com.lucasdias.gametrackr.feature.app.library.components.LibraryEntryRow
import com.lucasdias.gametrackr.feature.app.library.components.LibraryFilterChips
import com.lucasdias.gametrackr.feature.app.library.components.LibraryStatsBar
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

private const val ENTRIES_PAGE_SIZE = 3

@Composable
fun LibraryScreen(
    filter: LibraryStatus?,
    onFilterChange: (LibraryStatus?) -> Unit,
    isGuest: Boolean = false,
    onCreateAccount: () -> Unit = {},
    modifier: Modifier = Modifier,
    onBrowseGames: () -> Unit = {},
    onGameClick: () -> Unit = {},
    repository: LibraryRepository = koinInject(),
) {
    if (isGuest) {
        LibraryGuestState(onCreateAccount = onCreateAccount)
        return
    }

    val pagination = remember { PaginationState<LibraryEntry>() }
    var stats by remember { mutableStateOf<LibraryStats?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun loadNextPage() {
        if (!pagination.canLoadMore) return
        pagination.setLoading(true)
        coroutineScope.launch {
            repository
                .fetchLibrary(page = pagination.currentPage + 1, perPage = ENTRIES_PAGE_SIZE)
                .onSuccess { pagination.append(it) }
            pagination.setLoading(false)
        }
    }

    LaunchedEffect(repository) {
        loadNextPage()
        stats = repository.fetchLibraryStats().getOrNull()
    }

    val filteredEntries = if (filter == null) pagination.items.toList() else pagination.items.filter { it.status == filter }

    Column(modifier = modifier.fillMaxSize().background(AppBackground)) {
        val loadedStats = stats
        if (pagination.items.isNotEmpty() && loadedStats != null) {
            LibraryStatsBar(
                stats = loadedStats,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 16.dp),
            )
        }

        LibraryFilterChips(
            selection = filter,
            onSelect = onFilterChange,
            modifier = Modifier.padding(top = if (pagination.items.isEmpty()) 12.dp else 0.dp, bottom = 14.dp),
        )

        when {
            pagination.currentPage == 0 -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppPrimary)
                }
            }

            pagination.items.isEmpty() -> {
                LibraryEmptyState(onBrowse = onBrowseGames)
            }

            filteredEntries.isEmpty() -> {
                FilterEmptyState(status = filter)
            }

            else -> {
                val listState = rememberLazyListState()

                InfiniteScrollEffect(
                    listState = listState,
                    canLoadMore = pagination.canLoadMore,
                    onLoadMore = ::loadNextPage,
                )

                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(filteredEntries) { entry ->
                        LibraryEntryRow(
                            entry = entry,
                            onFavorite = {
                                val index = pagination.items.indexOfFirst { it.title == entry.title }
                                if (index >= 0) {
                                    pagination.items[index] = pagination.items[index].copy(isFavorite = !entry.isFavorite)
                                }
                            },
                            modifier = Modifier.clickable(onClick = onGameClick),
                        )
                    }

                    if (pagination.isLoadingMore) {
                        item { LoadingMoreIndicator() }
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryGuestState(onCreateAccount: () -> Unit) {
    CommunityEmptyState(
        icon = AppIcon.BRAND,
        title = stringResource(R.string.library_guest_title),
        message = stringResource(R.string.library_guest_message),
        actionTitle = stringResource(R.string.library_guest_action),
        onAction = onCreateAccount,
    )
}

@Composable
private fun FilterEmptyState(status: LibraryStatus?) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.library_filter_empty_title),
            color = AppTextPrimary,
            style = AppType.headline(20.sp, FontWeight.ExtraBold),
        )
        Text(
            text =
                stringResource(
                    R.string.library_filter_empty_subtitle,
                    status?.let { stringResource(it.labelRes) } ?: "",
                ),
            color = AppTextSecondary,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}
