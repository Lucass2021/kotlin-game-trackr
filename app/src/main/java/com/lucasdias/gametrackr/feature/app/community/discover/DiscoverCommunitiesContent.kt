package com.lucasdias.gametrackr.feature.app.community.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.pagination.InfiniteScrollEffect
import com.lucasdias.gametrackr.core.pagination.LoadMoreErrorRow
import com.lucasdias.gametrackr.core.pagination.LoadingMoreIndicator
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.components.rememberRemainingHeight
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.CommunityOptions
import com.lucasdias.gametrackr.feature.app.community.components.CommunityChipRow
import com.lucasdias.gametrackr.feature.app.community.discover.components.CommunityRow
import com.lucasdias.gametrackr.feature.app.community.discover.components.CommunitySearchField

private const val STATE_KEY = "discover_state"
private val DIVIDER_THICKNESS = 1.dp
private val TITLE_SIZE = 22.sp

@Composable
fun DiscoverCommunitiesContent(
    state: DiscoverCommunitiesUiState,
    actions: DiscoverCommunitiesActions,
    modifier: Modifier = Modifier,
    currentUserId: Int? = null,
) {
    val listState = rememberLazyListState()

    InfiniteScrollEffect(listState = listState, canLoadMore = state.canLoadMore, onLoadMore = actions.onLoadMore)

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(bottom = Spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
    ) {
        item {
            CommunitySearchField(
                query = state.query,
                onQueryChange = actions.onQueryChange,
                modifier = Modifier.padding(horizontal = Spacing.lg),
            )
        }

        item {
            CommunityChipRow(
                titles = CommunityOptions.categories,
                selection = state.category,
                onSelect = actions.onCategoryChange,
            )
        }

        when {
            state.hasError -> discoverState(listState, isError = true, onRetry = actions.onRetry)
            state.isEmpty -> discoverState(listState, isError = false, onRetry = actions.onRetry)
            state.currentPage == 0 -> item { LoadingMoreIndicator() }
            else -> communities(state, actions, currentUserId)
        }
    }
}

private fun LazyListScope.discoverState(
    listState: LazyListState,
    isError: Boolean,
    onRetry: () -> Unit,
) {
    item(key = STATE_KEY) {
        EmptyState(
            icon = if (isError) AppIcon.INFO else AppIcon.SEARCH,
            title = stringResource(if (isError) R.string.discover_error_title else R.string.discover_empty_title),
            message = stringResource(if (isError) R.string.discover_error_message else R.string.discover_empty_message),
            actionTitle = if (isError) stringResource(R.string.discover_retry) else null,
            onAction = if (isError) onRetry else null,
            modifier = Modifier.heightIn(min = rememberRemainingHeight(listState, STATE_KEY)),
        )
    }
}

private fun LazyListScope.communities(
    state: DiscoverCommunitiesUiState,
    actions: DiscoverCommunitiesActions,
    currentUserId: Int?,
) {
    item {
        Text(
            text = stringResource(R.string.discover_all_communities),
            color = AppTextPrimary,
            style = AppType.headline(TITLE_SIZE),
            modifier = Modifier.padding(horizontal = Spacing.lg),
        )
    }

    itemsIndexed(state.communities, key = { _, community -> community.id }) { index, community ->
        Column {
            CommunityRow(
                community = community,
                onSelect = { actions.onCommunityClick(community) },
                onJoin = { actions.onJoinTap(community) },
                isOwner = currentUserId != null && community.authorId == currentUserId,
            )
            if (index < state.communities.lastIndex) {
                HorizontalDivider(
                    thickness = DIVIDER_THICKNESS,
                    color = AppOutline,
                    modifier = Modifier.padding(horizontal = Spacing.lg),
                )
            }
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
private fun DiscoverCommunitiesContentPreview() {
    AppPreview {
        DiscoverCommunitiesContent(
            state = DiscoverCommunitiesUiState(communities = CommunityMockData.all, currentPage = 1),
            actions = DiscoverCommunitiesActions(),
        )
    }
}

@Preview
@Composable
private fun DiscoverCommunitiesContentErrorPreview() {
    AppPreview {
        DiscoverCommunitiesContent(
            state = DiscoverCommunitiesUiState(hasError = true),
            actions = DiscoverCommunitiesActions(),
        )
    }
}
