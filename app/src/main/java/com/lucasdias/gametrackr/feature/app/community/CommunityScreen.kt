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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.feature.app.community.components.CommunityChipRow
import com.lucasdias.gametrackr.feature.app.community.components.CommunityEmptyState
import com.lucasdias.gametrackr.feature.app.community.components.CommunityPostCard
import com.lucasdias.gametrackr.feature.app.community.components.CommunitySegmentControl
import com.lucasdias.gametrackr.feature.app.community.components.CreatePostButton
import com.lucasdias.gametrackr.feature.app.community.components.SuggestedCommunityCard
import com.lucasdias.gametrackr.feature.app.community.discover.DiscoverCommunitiesContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    viewModel: CommunityViewModel,
    onPostClick: (CommunityPost) -> Unit,
    onCommunityClick: (Community) -> Unit,
    onCreatePost: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var segment by remember { mutableStateOf(CommunitySegment.MY_FEED) }
    var feedFilter by remember { mutableStateOf(CommunityMockData.feedFilters.first()) }
    var category by remember { mutableStateOf("All") }
    val feedError by viewModel.feedError.collectAsStateWithLifecycle()
    val isLoadingFeed by viewModel.isLoadingFeed.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadFeed()
        viewModel.loadCommunities()
    }

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
                        feed = viewModel.feed,
                        feedFilter = feedFilter,
                        feedError = feedError,
                        isRefreshing = isLoadingFeed,
                        onFilterSelect = { feedFilter = it },
                        onPostSelect = onPostClick,
                        onLike = { viewModel.toggleLike(it) },
                        onBookmark = { viewModel.toggleBookmark(it) },
                        suggestedCommunity = viewModel.communities.firstOrNull { !it.isJoined },
                        onCommunitySelect = onCommunityClick,
                        onJoinSuggested = { viewModel.toggleJoin(it) },
                        onDiscover = { segment = CommunitySegment.DISCOVER },
                        onRetry = {
                            viewModel.loadFeed()
                            viewModel.loadCommunities()
                        },
                    )
                }

                CommunitySegment.DISCOVER -> {
                    DiscoverCommunitiesContent(
                        category = category,
                        onCategorySelect = { category = it },
                        communities = viewModel.communities,
                        onCommunitySelect = onCommunityClick,
                        onJoin = { viewModel.toggleJoin(it) },
                    )
                }
            }
        }

        if (segment == CommunitySegment.MY_FEED) {
            CreatePostButton(
                onClick = onCreatePost,
                modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeedContent(
    feed: List<CommunityPost>,
    feedFilter: String,
    feedError: Boolean,
    isRefreshing: Boolean,
    onFilterSelect: (String) -> Unit,
    onPostSelect: (CommunityPost) -> Unit,
    onLike: (CommunityPost) -> Unit,
    onBookmark: (CommunityPost) -> Unit,
    suggestedCommunity: Community?,
    onCommunitySelect: (Community) -> Unit,
    onJoinSuggested: (Community) -> Unit,
    onDiscover: () -> Unit,
    onRetry: () -> Unit,
) {
    if (feed.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (feedError) {
                CommunityEmptyState(
                    icon = AppIcon.INFO,
                    title = "Couldn't load feed",
                    message = "Check your connection\nand try again.",
                    actionTitle = "Try again",
                    onAction = onRetry,
                )
            } else {
                CommunityEmptyState(
                    icon = AppIcon.COMMUNITY,
                    title = "Your feed is quiet",
                    message = "Join a community to see posts\nfrom other players here.",
                    actionTitle = "Discover communities",
                    onAction = onDiscover,
                )
            }
        }
        return
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRetry,
    ) {
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
                    onSelect = { onPostSelect(post) },
                    onLike = { onLike(post) },
                    onComment = { onPostSelect(post) },
                    onBookmark = { onBookmark(post) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )

                if (index == 0 && suggestedCommunity != null) {
                    Spacer(Modifier.height(16.dp))
                    SuggestedCommunityCard(
                        community = suggestedCommunity,
                        onSelect = { onCommunitySelect(suggestedCommunity) },
                        onJoin = { onJoinSuggested(suggestedCommunity) },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
