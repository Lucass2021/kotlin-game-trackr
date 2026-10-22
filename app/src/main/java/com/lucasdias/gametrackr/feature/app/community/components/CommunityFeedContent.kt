package com.lucasdias.gametrackr.feature.app.community.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.CommunityFeedFilter
import com.lucasdias.gametrackr.core.pagination.InfiniteScrollEffect
import com.lucasdias.gametrackr.core.pagination.LoadMoreErrorRow
import com.lucasdias.gametrackr.core.pagination.LoadingMoreIndicator
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.components.rememberRemainingHeight
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.CommunityFeedActions
import com.lucasdias.gametrackr.feature.app.community.CommunityFeedUiState

private val BOTTOM_PADDING = 96.dp
private const val STATE_KEY = "feed_state"
private const val SUGGESTION_INDEX = 0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityFeedContent(
    state: CommunityFeedUiState,
    isGuest: Boolean,
    actions: CommunityFeedActions,
    onDiscover: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val filterLabels = CommunityFeedFilter.entries.map { stringResource(it.labelRes) }

    InfiniteScrollEffect(listState = listState, canLoadMore = state.canLoadMore, onLoadMore = actions.onLoadMore)

    PullToRefreshBox(isRefreshing = state.isRefreshing, onRefresh = actions.onRefresh, modifier = modifier) {
        LazyColumn(state = listState, contentPadding = PaddingValues(bottom = BOTTOM_PADDING)) {
            item {
                CommunityChipRow(
                    titles = filterLabels,
                    selection = stringResource(state.filter.labelRes),
                    onSelect = { title -> actions.onFilterChange(CommunityFeedFilter.entries[filterLabels.indexOf(title)]) },
                )
                Spacer(Modifier.height(Spacing.md))
            }

            when {
                state.hasError -> feedState(listState, isError = true, onAction = actions.onRetry)
                state.isEmpty -> feedState(listState, isError = false, onAction = onDiscover)
                state.currentPage == 0 -> item { LoadingMoreIndicator() }
                else -> feedPosts(state = state, isGuest = isGuest, actions = actions)
            }
        }
    }
}

private fun LazyListScope.feedState(
    listState: LazyListState,
    isError: Boolean,
    onAction: () -> Unit,
) {
    item(key = STATE_KEY) {
        EmptyState(
            icon = if (isError) AppIcon.INFO else AppIcon.COMMUNITY,
            title = stringResource(if (isError) R.string.community_feed_error_title else R.string.community_feed_empty_title),
            message = stringResource(if (isError) R.string.community_feed_error_message else R.string.community_feed_empty_message),
            actionTitle = stringResource(if (isError) R.string.community_feed_retry else R.string.community_feed_empty_action),
            onAction = onAction,
            modifier = Modifier.heightIn(min = rememberRemainingHeight(listState, STATE_KEY)),
        )
    }
}

private fun LazyListScope.feedPosts(
    state: CommunityFeedUiState,
    isGuest: Boolean,
    actions: CommunityFeedActions,
) {
    itemsIndexed(state.posts, key = { _, post -> post.id }) { index, post ->
        CommunityPostCard(
            post = post,
            isGuest = isGuest,
            onSelect = { actions.onPostClick(post) },
            onLike = { actions.onLike(post.id) },
            onComment = { actions.onPostClick(post) },
            onShare = { actions.onShare(post) },
            onBookmark = { actions.onBookmark(post.id) },
            modifier = Modifier.padding(horizontal = Spacing.lg),
        )
        Spacer(Modifier.height(Spacing.md))

        val suggestion = state.suggestedCommunity
        if (index == SUGGESTION_INDEX && suggestion != null && !isGuest) {
            SuggestedCommunityCard(
                community = suggestion,
                onSelect = { actions.onCommunityClick(suggestion) },
                onJoin = actions.onJoinSuggested,
                modifier = Modifier.padding(horizontal = Spacing.lg),
            )
            Spacer(Modifier.height(Spacing.md))
        }
    }

    if (state.isLoadingMore) {
        item { LoadingMoreIndicator() }
    }

    if (state.loadMoreFailed) {
        item { LoadMoreErrorRow(onRetry = actions.onLoadMore) }
    }
}

@Preview
@Composable
private fun CommunityFeedContentPreview() {
    AppPreview {
        CommunityFeedContent(
            state =
                CommunityFeedUiState(
                    posts = CommunityMockData.feed,
                    currentPage = 1,
                    suggestedCommunity = CommunityMockData.suggested,
                    loadMoreFailed = true,
                ),
            isGuest = false,
            actions = CommunityFeedActions(),
            onDiscover = {},
        )
    }
}

@Preview
@Composable
private fun CommunityFeedContentErrorPreview() {
    AppPreview {
        CommunityFeedContent(
            state = CommunityFeedUiState(hasError = true),
            isGuest = false,
            actions = CommunityFeedActions(),
            onDiscover = {},
        )
    }
}
