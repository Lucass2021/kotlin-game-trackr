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

@Composable
fun CommunityScreen(
    feedViewModel: CommunityFeedViewModel,
    viewModel: CommunityViewModel,
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
    LaunchedEffect(viewModel) { viewModel.loadCommunities() }

    CommunityScreen(
        feedState = feedState,
        communities = viewModel.communities,
        isLoadingMoreCommunities = viewModel.communitiesPagination.isLoadingMore,
        canLoadMoreCommunities = viewModel.communitiesPagination.canLoadMore,
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
        onJoinCommunity = viewModel::joinCommunity,
        onLeaveCommunity = viewModel::leaveCommunity,
        onLoadMoreCommunities = { viewModel.loadMoreCommunities() },
        onCreatePost = onCreatePost,
        onCreateCommunity = onCreateCommunity,
        onCreateAccount = onCreateAccount,
    )
}

@Composable
private fun CommunityScreen(
    feedState: CommunityFeedUiState,
    communities: List<Community>,
    isLoadingMoreCommunities: Boolean,
    canLoadMoreCommunities: Boolean,
    isGuest: Boolean,
    currentUserId: Int?,
    feedActions: CommunityFeedActions,
    onJoinCommunity: (Community) -> Unit,
    onLeaveCommunity: (Community) -> Unit,
    onLoadMoreCommunities: () -> Unit,
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
                        communities = communities,
                        isLoadingMore = isLoadingMoreCommunities,
                        canLoadMore = canLoadMoreCommunities,
                        isGuest = isGuest,
                        currentUserId = currentUserId,
                        onCommunityClick = feedActions.onCommunityClick,
                        onJoin = onJoinCommunity,
                        onLeave = onLeaveCommunity,
                        onLoadMore = onLoadMoreCommunities,
                        onCreateAccount = onCreateAccount,
                    )
                }
            }
        }

        if (!isGuest) {
            val isFeed = segment == CommunitySegment.MY_FEED
            CreatePostButton(
                onClick = if (isFeed) onCreatePost else onCreateCommunity,
                modifier = Modifier.align(Alignment.BottomEnd).padding(Spacing.lg),
                label = stringResource(if (isFeed) R.string.community_action_create_post else R.string.community_action_create_community),
            )
        }

        Toast(
            message = feedState.toastMessage?.let { stringResource(it) },
            onDismiss = feedActions.onToastShown,
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
            communities = CommunityMockData.all,
            isLoadingMoreCommunities = false,
            canLoadMoreCommunities = false,
            isGuest = false,
            currentUserId = null,
            feedActions = CommunityFeedActions(),
            onJoinCommunity = {},
            onLeaveCommunity = {},
            onLoadMoreCommunities = {},
            onCreatePost = {},
            onCreateCommunity = {},
            onCreateAccount = {},
        )
    }
}
