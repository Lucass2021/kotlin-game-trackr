package com.lucasdias.gametrackr.feature.app.community.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.pagination.InfiniteScrollEffect
import com.lucasdias.gametrackr.core.pagination.LoadingMoreIndicator
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.Community
import com.lucasdias.gametrackr.feature.app.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.components.CommunityChipRow
import com.lucasdias.gametrackr.feature.app.community.components.CommunityEmptyState

@Composable
fun DiscoverCommunitiesContent(
    category: String,
    onCategorySelect: (String) -> Unit,
    communities: SnapshotStateList<Community>,
    isLoadingMore: Boolean = false,
    canLoadMore: Boolean = false,
    onCommunitySelect: (Community) -> Unit,
    onJoin: (Community) -> Unit,
    onLoadMore: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }

    val filtered =
        communities.filter { community ->
            val matchesCategory = category == "All" || community.category == category
            val matchesQuery = query.isBlank() || community.name.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }

    val listState = rememberLazyListState()

    InfiniteScrollEffect(
        listState = listState,
        canLoadMore = canLoadMore,
        onLoadMore = onLoadMore,
    )

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        item {
            CommunitySearchField(
                query = query,
                onQueryChange = { query = it },
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        item {
            CommunityChipRow(
                titles = CommunityMockData.categories,
                selection = category,
                onSelect = onCategorySelect,
            )
        }

        if (filtered.isEmpty()) {
            item {
                CommunityEmptyState(
                    icon = AppIcon.SEARCH,
                    title = "No communities found",
                    message = "Try a different search or category.",
                )
            }
        } else {
            item {
                Text(
                    text = "All communities",
                    color = AppTextPrimary,
                    style = AppType.headline(22.sp),
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            items(filtered, key = { it.id }) { community ->
                Column {
                    CommunityRow(
                        community = community,
                        onSelect = { onCommunitySelect(community) },
                        onJoin = { onJoin(community) },
                    )
                    if (community.id != filtered.last().id) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = AppOutline,
                            modifier = Modifier.padding(horizontal = 20.dp),
                        )
                    }
                }
            }

            if (isLoadingMore) {
                item { LoadingMoreIndicator() }
            }
        }
    }
}
