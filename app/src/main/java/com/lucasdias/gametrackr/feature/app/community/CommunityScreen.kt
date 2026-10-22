package com.lucasdias.gametrackr.feature.app.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.shareText
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.components.CommunityDiscoverSegment
import com.lucasdias.gametrackr.feature.app.community.components.CommunityFeedContent
import com.lucasdias.gametrackr.feature.app.community.components.CommunitySegmentControl
import com.lucasdias.gametrackr.feature.app.community.components.CreatePostButton
import com.lucasdias.gametrackr.feature.app.community.discover.DiscoverCommunitiesActions
import com.lucasdias.gametrackr.feature.app.community.discover.DiscoverCommunitiesUiState
import com.lucasdias.gametrackr.feature.app.community.discover.DiscoverCommunitiesViewModel

@Composable
fun CommunityScreen(
    feedViewModel: CommunityFeedViewModel,
    discoverViewModel: DiscoverCommunitiesViewModel,
    isGuest: Boolean,
    onPostClick: (CommunityPost) -> Unit,
    onCommunityClick: (Community) -> Unit,
    onCreatePost: () -> Unit,
    onCreateCommunity: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
    currentUserId: Int? = null,
) {
    val feedState by feedViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(feedViewModel) { feedViewModel.loadIfNeeded() }
    val discoverState by discoverViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(discoverViewModel) { discoverViewModel.loadIfNeeded() }

    CommunityScreen(
        feedState = feedState,
        discoverState = discoverState,
        isGuest = isGuest,
        currentUserId = currentUserId,
        modifier = modifier,
        feedActions =
            CommunityFeedActions(
                onRefresh = feedViewModel::refresh,
                onRetry = feedViewModel::load,
                onLoadMore = feedViewModel::loadMore,
                onFilterChange = feedViewModel::onFilterChange,
                onPostClick = onPostClick,
                onLike = feedViewModel::onLike,
                onShare = { post -> context.shareText(context.getString(R.string.post_detail_share_message, post.title)) },
                onBookmark = feedViewModel::onBookmark,
                onCommunityClick = onCommunityClick,
                onJoinSuggested = feedViewModel::onJoinSuggested,
                onToastShown = feedViewModel::onToastShown,
            ),
        discoverActions =
            DiscoverCommunitiesActions(
                onRetry = discoverViewModel::load,
                onLoadMore = discoverViewModel::loadMore,
                onQueryChange = discoverViewModel::onQueryChange,
                onCategoryChange = discoverViewModel::onCategoryChange,
                onCommunityClick = onCommunityClick,
                onJoinTap = discoverViewModel::onJoinTap,
                onLeaveConfirmed = discoverViewModel::onLeaveConfirmed,
                onLeaveDismissed = discoverViewModel::onLeaveDismissed,
            ),
        onDiscoverToastShown = discoverViewModel::onToastShown,
        onCreatePost = onCreatePost,
        onCreateCommunity = onCreateCommunity,
        onCreateAccount = onCreateAccount,
    )
}

@Composable
private fun CommunityScreen(
    feedState: CommunityFeedUiState,
    discoverState: DiscoverCommunitiesUiState,
    isGuest: Boolean,
    currentUserId: Int?,
    feedActions: CommunityFeedActions,
    discoverActions: DiscoverCommunitiesActions,
    onDiscoverToastShown: () -> Unit,
    onCreatePost: () -> Unit,
    onCreateCommunity: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var segment by rememberSaveable { mutableStateOf(CommunitySegment.MY_FEED) }

    Box(modifier = modifier.fillMaxSize().background(AppBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            CommunitySegmentControl(
                selection = segment,
                onSelect = { segment = it },
                modifier = Modifier.padding(top = Spacing.xxs, bottom = Spacing.md),
            )

            when (segment) {
                CommunitySegment.MY_FEED -> {
                    CommunityFeedContent(
                        state = feedState,
                        isGuest = isGuest,
                        actions = feedActions,
                        onDiscover = { segment = CommunitySegment.DISCOVER },
                    )
                }

                CommunitySegment.DISCOVER -> {
                    CommunityDiscoverSegment(
                        state = discoverState,
                        actions = discoverActions,
                        isGuest = isGuest,
                        currentUserId = currentUserId,
                        onCreateAccount = onCreateAccount,
                    )
                }
            }
        }

        val isFeed = segment == CommunitySegment.MY_FEED
        if (!isGuest) {
            CreatePostButton(
                onClick = if (isFeed) onCreatePost else onCreateCommunity,
                modifier = Modifier.align(Alignment.BottomEnd).padding(Spacing.lg),
                label = stringResource(if (isFeed) R.string.community_action_create_post else R.string.community_action_create_community),
            )
        }

        Toast(
            message = (if (isFeed) feedState.toastMessage else discoverState.toastMessage)?.let { stringResource(it) },
            onDismiss = if (isFeed) feedActions.onToastShown else onDiscoverToastShown,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Preview
@Composable
private fun CommunityScreenPreview() {
    AppPreview {
        CommunityScreen(
            feedState =
                CommunityFeedUiState(
                    posts = CommunityMockData.feed,
                    currentPage = 1,
                    suggestedCommunity = CommunityMockData.suggested,
                ),
            discoverState = DiscoverCommunitiesUiState(communities = CommunityMockData.all, currentPage = 1),
            isGuest = false,
            currentUserId = null,
            feedActions = CommunityFeedActions(),
            discoverActions = DiscoverCommunitiesActions(),
            onDiscoverToastShown = {},
            onCreatePost = {},
            onCreateCommunity = {},
            onCreateAccount = {},
        )
    }
}
