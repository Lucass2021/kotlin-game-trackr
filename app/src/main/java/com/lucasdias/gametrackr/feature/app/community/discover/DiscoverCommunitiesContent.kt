package com.lucasdias.gametrackr.feature.app.community.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
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
    onCommunitySelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }

    val filtered =
        communities.filter { community ->
            val matchesCategory = category == "All" || community.category == category
            val matchesQuery = query.isBlank() || community.name.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }

    LazyColumn(
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

        item {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                SectionHeader(title = "Featured")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    items(CommunityMockData.featured, key = { it.id }) { community ->
                        FeaturedCommunityCard(
                            community = community,
                            onSelect = onCommunitySelect,
                            onJoin = { toggleJoin(communities, community) },
                        )
                    }
                }
            }
        }

        if (filtered.isEmpty()) {
            item {
                CommunityEmptyState(
                    icon = AppIcon.SEARCH,
                    title = "No communities found",
                    message = "Try a different name or clear the filters to see everything.",
                    actionTitle = "Clear filters",
                    onAction = {
                        query = ""
                        onCategorySelect("All")
                    },
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
                        onSelect = onCommunitySelect,
                        onJoin = { toggleJoin(communities, community) },
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
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            color = AppTextPrimary,
            style = AppType.headline(22.sp),
        )
        Text(
            text = "See all",
            color = AppPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

private fun toggleJoin(
    communities: SnapshotStateList<Community>,
    community: Community,
) {
    val index = communities.indexOfFirst { it.id == community.id }
    if (index >= 0) {
        communities[index] = communities[index].copy(isJoined = !communities[index].isJoined)
    }
}
