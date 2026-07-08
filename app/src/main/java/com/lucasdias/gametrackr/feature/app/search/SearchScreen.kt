package com.lucasdias.gametrackr.feature.app.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.Sora
import com.lucasdias.gametrackr.feature.app.search.components.ExploreCommunityCard
import com.lucasdias.gametrackr.feature.app.search.components.SearchFilterChips
import com.lucasdias.gametrackr.feature.app.search.components.SearchResultCard
import com.lucasdias.gametrackr.feature.app.search.components.SearchResultsEmptyState
import com.lucasdias.gametrackr.feature.app.search.components.SearchTopBar

@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onExploreCommunity: () -> Unit,
) {
    BackHandler(onBack = onBack)

    var query by rememberSaveable { mutableStateOf("") }
    var platform by rememberSaveable { mutableStateOf<GamePlatform?>(null) }

    val games =
        remember(query, platform) {
            SearchMockData.games.filter { game ->
                val matchesPlatform = platform == null || game.platforms.contains(platform)
                val matchesQuery = query.isBlank() || game.title.contains(query.trim(), ignoreCase = true)
                matchesPlatform && matchesQuery
            }
        }

    Column(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        SearchTopBar(query = query, onQueryChange = { query = it }, onBack = onBack)

        SearchFilterChips(
            selection = platform,
            onSelect = { platform = it },
            modifier = Modifier.padding(top = 2.dp, bottom = 14.dp),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 6.dp, bottom = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            if (games.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SearchResultsEmptyState(
                        query = query,
                        onClear = {
                            query = ""
                            platform = null
                        },
                    )
                }
            } else {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionHeader(isSearching = query.isNotBlank(), count = games.size)
                }
                items(games.size) { index ->
                    SearchResultCard(game = games[index])
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                ExploreCommunityCard(
                    onExplore = onExploreCommunity,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
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
                if (isSearching) {
                    stringResource(R.string.search_results)
                } else {
                    stringResource(R.string.search_recent_releases)
                },
            color = AppTextPrimary,
            fontFamily = Sora,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = pluralStringResource(R.plurals.search_result_count, count, count),
            color = AppTextSecondary,
            fontSize = 14.sp,
        )
    }
}
