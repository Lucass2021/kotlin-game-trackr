package com.lucasdias.gametrackr.feature.app.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.feature.app.community.components.CommunityChipRow
import com.lucasdias.gametrackr.feature.app.community.components.CommunityEmptyState
import com.lucasdias.gametrackr.feature.app.community.components.CommunityPostCard
import com.lucasdias.gametrackr.feature.app.community.components.CommunitySegmentControl
import com.lucasdias.gametrackr.feature.app.community.components.CreatePostButton
import com.lucasdias.gametrackr.feature.app.community.components.SuggestedCommunityCard
import com.lucasdias.gametrackr.feature.app.community.discover.DiscoverCommunitiesContent

@Composable
fun CommunityScreen(
    onPostClick: () -> Unit,
    onCommunityClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val communities = remember { CommunityMockData.all.toMutableStateList() }
    val feed = remember { CommunityMockData.feed.toMutableStateList() }

    var segment by remember { mutableStateOf(CommunitySegment.MY_FEED) }
    var feedFilter by remember { mutableStateOf(CommunityMockData.feedFilters.first()) }
    var category by remember { mutableStateOf("All") }

    Box(modifier = modifier.fillMaxSize().background(AppBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            CommunitySegmentControl(
                selection = segment,
                onSelect = { segment = it },
                modifier = Modifier.padding(top = 4.dp, bottom = 14.dp),
            )

            when (segment) {
                CommunitySegment.MY_FEED -> {
                    FeedContent(
                        feed = feed,
                        feedFilter = feedFilter,
                        onFilterSelect = { feedFilter = it },
                        onPostSelect = onPostClick,
                        onCommunitySelect = onCommunityClick,
                    )
                }

                CommunitySegment.DISCOVER -> {
                    DiscoverCommunitiesContent(
                        category = category,
                        onCategorySelect = { category = it },
                        communities = communities,
                        onCommunitySelect = onCommunityClick,
                    )
                }
            }
        }

        if (segment == CommunitySegment.MY_FEED && feed.isNotEmpty()) {
            CreatePostButton(
                onClick = {},
                modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
            )
        }
    }
}

@Composable
private fun FeedContent(
    feed: SnapshotStateList<CommunityPost>,
    feedFilter: String,
    onFilterSelect: (String) -> Unit,
    onPostSelect: () -> Unit,
    onCommunitySelect: () -> Unit,
) {
    if (feed.isEmpty()) {
        CommunityEmptyState(
            icon = AppIcon.COMMUNITY,
            title = "Your feed is quiet",
            message = "Join a community to see posts from other players here.",
            actionTitle = "Discover communities",
            onAction = {},
        )
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 96.dp),
    ) {
        item {
            CommunityChipRow(
                titles = CommunityMockData.feedFilters,
                selection = feedFilter,
                onSelect = onFilterSelect,
            )
            Spacer(Modifier.height(16.dp))
        }

        itemsIndexed(feed, key = { _, post -> post.id }) { index, post ->
            CommunityPostCard(
                post = post,
                onSelect = onPostSelect,
                onLike = {
                    feed[index] = post.copy(isLiked = !post.isLiked, likes = post.likes + if (post.isLiked) -1 else 1)
                },
                onComment = onPostSelect,
                onBookmark = { feed[index] = post.copy(isBookmarked = !post.isBookmarked) },
                modifier = Modifier.padding(horizontal = 20.dp),
            )

            if (index == 0) {
                Spacer(Modifier.height(16.dp))
                SuggestedCommunityCard(
                    community = CommunityMockData.suggested,
                    onSelect = onCommunitySelect,
                    onJoin = {},
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
