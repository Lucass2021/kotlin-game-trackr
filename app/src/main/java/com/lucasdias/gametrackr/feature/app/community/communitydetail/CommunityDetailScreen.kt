package com.lucasdias.gametrackr.feature.app.community.communitydetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityMember
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.shareText
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.CommunityDetailTab
import com.lucasdias.gametrackr.feature.app.community.communitydetail.components.CommunityDetailContent
import com.lucasdias.gametrackr.feature.app.community.communitydetail.components.CommunityDetailDialogs
import com.lucasdias.gametrackr.feature.app.community.communitydetail.components.CommunityDetailFloatingButton
import com.lucasdias.gametrackr.feature.app.community.communitydetail.components.CommunityDetailOwnerMenu
import com.lucasdias.gametrackr.feature.app.community.components.CreatePostButton
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CommunityDetailScreen(
    community: Community,
    onBack: () -> Unit,
    onPostClick: (CommunityPost) -> Unit,
    onCreatePost: () -> Unit,
    onMemberClick: (CommunityMember) -> Unit,
    modifier: Modifier = Modifier,
    isGuest: Boolean = false,
    currentUserId: Int? = null,
    onDeleted: () -> Unit = {},
    viewModel: CommunityDetailViewModel = koinViewModel { parametersOf(community) },
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) { viewModel.loadIfNeeded() }
    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) {
            onDeleted()
            onBack()
        }
    }

    CommunityDetailScreen(
        community = community,
        state = state,
        isGuest = isGuest,
        isOwner = currentUserId != null && community.authorId == currentUserId,
        modifier = modifier,
        onBack = onBack,
        onRefresh = viewModel::refresh,
        onRetry = viewModel::load,
        onLoadMore = viewModel::loadMore,
        onRetryMembers = viewModel::retryMembers,
        onTabSelect = viewModel::onTabSelect,
        onJoin = viewModel::onJoinTap,
        onLeaveConfirmed = viewModel::onLeaveConfirmed,
        onLeaveDismissed = viewModel::onLeaveDismissed,
        onDeleteRequest = viewModel::onDeleteRequest,
        onDeleteConfirmed = viewModel::onDeleteConfirmed,
        onDeleteDismissed = viewModel::onDeleteDismissed,
        onLike = viewModel::onLike,
        onBookmark = viewModel::onBookmark,
        onToastShown = viewModel::onToastShown,
        onCreatePost = onCreatePost,
        onPostClick = onPostClick,
        onMemberClick = onMemberClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommunityDetailScreen(
    community: Community,
    state: CommunityDetailUiState,
    isGuest: Boolean,
    isOwner: Boolean,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onRetry: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    onRetryMembers: () -> Unit = {},
    onTabSelect: (CommunityDetailTab) -> Unit = {},
    onJoin: () -> Unit = {},
    onLeaveConfirmed: () -> Unit = {},
    onLeaveDismissed: () -> Unit = {},
    onDeleteRequest: () -> Unit = {},
    onDeleteConfirmed: () -> Unit = {},
    onDeleteDismissed: () -> Unit = {},
    onLike: (Long) -> Unit = {},
    onBookmark: (Long) -> Unit = {},
    onToastShown: () -> Unit = {},
    onCreatePost: () -> Unit = {},
    onPostClick: (CommunityPost) -> Unit = {},
    onMemberClick: (CommunityMember) -> Unit = {},
) {
    val context = LocalContext.current
    val shareMessage = stringResource(R.string.community_detail_share_message, community.name)

    Box(modifier = modifier.fillMaxSize().background(AppBackground)) {
        PullToRefreshBox(isRefreshing = state.isRefreshing, onRefresh = onRefresh) {
            CommunityDetailContent(
                community = community,
                state = state,
                isGuest = isGuest,
                isOwner = isOwner,
                onShare = { context.shareText(shareMessage) },
                onJoin = onJoin,
                onTabSelect = onTabSelect,
                onRetry = onRetry,
                onLoadMore = onLoadMore,
                onRetryMembers = onRetryMembers,
                onCreatePost = onCreatePost,
                onPostClick = onPostClick,
                onLike = onLike,
                onBookmark = onBookmark,
                onMemberClick = onMemberClick,
                onDeleteRequest = onDeleteRequest,
            )
        }

        CommunityDetailFloatingButton(
            icon = AppIcon.BACK,
            label = stringResource(R.string.community_action_back),
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart),
        )

        if (isOwner) {
            CommunityDetailOwnerMenu(onDeleteRequest = onDeleteRequest, modifier = Modifier.align(Alignment.TopEnd))
        }

        if (state.tab == CommunityDetailTab.POSTS && state.isJoined && !isGuest) {
            CreatePostButton(onClick = onCreatePost, modifier = Modifier.align(Alignment.BottomEnd).padding(Spacing.lg))
        }

        Toast(
            message = state.toastMessage?.let { stringResource(it) },
            onDismiss = onToastShown,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }

    CommunityDetailDialogs(
        showLeaveConfirmation = state.showLeaveConfirmation,
        showDeleteConfirmation = state.showDeleteConfirmation,
        onLeaveConfirmed = onLeaveConfirmed,
        onLeaveDismissed = onLeaveDismissed,
        onDeleteConfirmed = onDeleteConfirmed,
        onDeleteDismissed = onDeleteDismissed,
    )
}

@Preview
@Composable
private fun CommunityDetailScreenPreview() {
    AppPreview {
        CommunityDetailScreen(
            community = CommunityMockData.detailCommunity,
            state = CommunityDetailUiState(posts = CommunityMockData.communityPosts, currentPage = 1),
            isGuest = false,
            isOwner = true,
        )
    }
}

@Preview
@Composable
private fun CommunityDetailScreenErrorPreview() {
    AppPreview {
        CommunityDetailScreen(
            community = CommunityMockData.detailCommunity,
            state = CommunityDetailUiState(hasError = true),
            isGuest = false,
            isOwner = false,
        )
    }
}
