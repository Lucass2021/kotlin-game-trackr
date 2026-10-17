package com.lucasdias.gametrackr.feature.app.community.communitydetail.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.pagination.LoadMoreErrorRow
import com.lucasdias.gametrackr.core.pagination.LoadingMoreIndicator
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.components.rememberRemainingHeight
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.communitydetail.CommunityDetailUiState
import com.lucasdias.gametrackr.feature.app.community.components.CommunityPostCard

private const val EMPTY_STATE_KEY = "empty_state"

fun LazyListScope.communityDetailPosts(
    state: CommunityDetailUiState,
    listState: LazyListState,
    isGuest: Boolean,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    onCreatePost: () -> Unit,
    onPostClick: (CommunityPost) -> Unit,
    onLike: (Long) -> Unit,
    onBookmark: (Long) -> Unit,
) {
    when {
        state.hasError -> {
            item(key = EMPTY_STATE_KEY) {
                EmptyState(
                    icon = AppIcon.INFO,
                    title = stringResource(R.string.community_detail_posts_error_title),
                    message = stringResource(R.string.community_detail_posts_error_message),
                    actionTitle = stringResource(R.string.community_detail_retry),
                    onAction = onRetry,
                    modifier = Modifier.heightIn(min = rememberRemainingHeight(listState, EMPTY_STATE_KEY)),
                )
            }
        }

        state.isEmpty -> {
            item(key = EMPTY_STATE_KEY) {
                EmptyState(
                    icon = AppIcon.COMMUNITY,
                    title = stringResource(R.string.community_detail_empty_title),
                    message =
                        stringResource(
                            if (state.isJoined) R.string.community_detail_empty_joined else R.string.community_detail_empty_not_joined,
                        ),
                    actionTitle = if (state.isJoined) stringResource(R.string.community_detail_create_post) else null,
                    onAction = if (state.isJoined) onCreatePost else null,
                    modifier = Modifier.heightIn(min = rememberRemainingHeight(listState, EMPTY_STATE_KEY)),
                )
            }
        }

        state.currentPage == 0 -> {
            item { LoadingMoreIndicator() }
        }

        else -> {
            items(state.posts, key = { it.id }) { post ->
                CommunityPostCard(
                    post = post,
                    isGuest = isGuest,
                    showsCommunityName = false,
                    onSelect = { onPostClick(post) },
                    onLike = { onLike(post.id) },
                    onComment = { onPostClick(post) },
                    onBookmark = { onBookmark(post.id) },
                    modifier = Modifier.padding(horizontal = Spacing.lg),
                )
                Spacer(Modifier.height(Spacing.md))
            }

            if (state.isLoadingMore) {
                item { LoadingMoreIndicator() }
            }

            if (state.loadMoreFailed) {
                item { LoadMoreErrorRow(onRetry = onLoadMore) }
            }
        }
    }
}

@Preview
@Composable
private fun CommunityDetailPostsPreview() {
    AppPreview {
        val listState = rememberLazyListState()
        LazyColumn(state = listState) {
            communityDetailPosts(
                state =
                    CommunityDetailUiState(
                        posts = CommunityMockData.communityPosts,
                        currentPage = 1,
                        loadMoreFailed = true,
                    ),
                listState = listState,
                isGuest = false,
                onRetry = {},
                onLoadMore = {},
                onCreatePost = {},
                onPostClick = {},
                onLike = {},
                onBookmark = {},
            )
        }
    }
}
