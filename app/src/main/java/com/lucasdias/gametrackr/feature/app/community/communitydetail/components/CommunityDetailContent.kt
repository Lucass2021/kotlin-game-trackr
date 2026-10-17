package com.lucasdias.gametrackr.feature.app.community.communitydetail.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityMember
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.pagination.InfiniteScrollEffect
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.CommunityDetailTab
import com.lucasdias.gametrackr.feature.app.community.communitydetail.CommunityDetailUiState

private val BOTTOM_PADDING = 96.dp
private val DESCRIPTION_SIZE = 15.sp
private const val DESCRIPTION_MAX_LINES = 3

@Composable
fun CommunityDetailContent(
    community: Community,
    state: CommunityDetailUiState,
    isGuest: Boolean,
    isOwner: Boolean,
    onShare: () -> Unit,
    onJoin: () -> Unit,
    onTabSelect: (CommunityDetailTab) -> Unit,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    onRetryMembers: () -> Unit,
    onCreatePost: () -> Unit,
    onPostClick: (CommunityPost) -> Unit,
    onLike: (Long) -> Unit,
    onBookmark: (Long) -> Unit,
    onMemberClick: (CommunityMember) -> Unit,
    onDeleteRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    InfiniteScrollEffect(
        listState = listState,
        canLoadMore = state.tab == CommunityDetailTab.POSTS && state.canLoadMore,
        onLoadMore = onLoadMore,
    )

    LazyColumn(state = listState, modifier = modifier, contentPadding = PaddingValues(bottom = BOTTOM_PADDING)) {
        item { CommunityDetailHeader(community = community) }

        item {
            Text(
                text = community.description,
                color = AppTextSecondary,
                style = AppType.body(DESCRIPTION_SIZE),
                maxLines = DESCRIPTION_MAX_LINES,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = Spacing.lg),
            )
            Spacer(Modifier.height(Spacing.lg))
            if (!isGuest) {
                CommunityDetailActions(
                    isJoined = state.isJoined,
                    isOwner = isOwner,
                    onJoin = onJoin,
                    onShare = onShare,
                    modifier = Modifier.padding(horizontal = Spacing.lg),
                )
                Spacer(Modifier.height(Spacing.lg))
            }
            CommunityStatsBar(community = community, modifier = Modifier.padding(horizontal = Spacing.lg))
            Spacer(Modifier.height(Spacing.lg))
            CommunityDetailTabs(selection = state.tab, onSelect = onTabSelect)
            Spacer(Modifier.height(Spacing.lg))
        }

        when (state.tab) {
            CommunityDetailTab.POSTS -> {
                communityDetailPosts(
                    state = state,
                    listState = listState,
                    isGuest = isGuest,
                    onRetry = onRetry,
                    onLoadMore = onLoadMore,
                    onCreatePost = onCreatePost,
                    onPostClick = onPostClick,
                    onLike = onLike,
                    onBookmark = onBookmark,
                )
            }

            CommunityDetailTab.ABOUT -> {
                item { CommunityAboutSection(community = community, isOwner = isOwner, onDelete = onDeleteRequest) }
            }

            CommunityDetailTab.MEMBERS -> {
                item {
                    if (state.membersFailed) {
                        EmptyState(
                            icon = AppIcon.INFO,
                            title = stringResource(R.string.community_detail_members_error_title),
                            message = stringResource(R.string.community_detail_members_error_message),
                            actionTitle = stringResource(R.string.community_detail_retry),
                            onAction = onRetryMembers,
                        )
                    } else {
                        CommunityMembersSection(members = state.members, onMemberClick = onMemberClick)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommunityDetailContentPreview() {
    AppPreview {
        CommunityDetailContent(
            community = CommunityMockData.detailCommunity,
            state = CommunityDetailUiState(posts = CommunityMockData.communityPosts, currentPage = 1),
            isGuest = false,
            isOwner = false,
            onShare = {},
            onJoin = {},
            onTabSelect = {},
            onRetry = {},
            onLoadMore = {},
            onRetryMembers = {},
            onCreatePost = {},
            onPostClick = {},
            onLike = {},
            onBookmark = {},
            onMemberClick = {},
            onDeleteRequest = {},
        )
    }
}
