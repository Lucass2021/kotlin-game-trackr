package com.lucasdias.gametrackr.feature.app.notifications.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.NotificationSection
import com.lucasdias.gametrackr.core.pagination.InfiniteScrollEffect
import com.lucasdias.gametrackr.core.pagination.LoadMoreErrorRow
import com.lucasdias.gametrackr.core.pagination.LoadingMoreIndicator
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.notifications.NotificationsMockData
import com.lucasdias.gametrackr.feature.app.notifications.NotificationSectionGroup
import com.lucasdias.gametrackr.feature.app.notifications.NotificationsUiState

private val SECTION_TITLE_SIZE = 12.sp
private val SECTION_TITLE_TRACKING = 1.sp

@Composable
fun NotificationsList(
    sections: List<NotificationSectionGroup>,
    canLoadMore: Boolean,
    isLoadingMore: Boolean,
    loadMoreFailed: Boolean,
    onLoadMore: () -> Unit,
    onAcceptFriendRequest: (Int) -> Unit,
    onDeclineFriendRequest: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    InfiniteScrollEffect(listState = listState, canLoadMore = canLoadMore, onLoadMore = onLoadMore)

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = Spacing.md, end = Spacing.md, bottom = Spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        sections.forEach { group ->
            item(key = "header_${group.section.name}") { NotificationSectionHeader(group.section) }
            items(group.items, key = { it.id }) { item ->
                NotificationRow(
                    item = item,
                    onAccept = { onAcceptFriendRequest(item.id) },
                    onDecline = { onDeclineFriendRequest(item.id) },
                )
            }
        }

        if (isLoadingMore) {
            item { LoadingMoreIndicator() }
        }

        if (loadMoreFailed) {
            item { LoadMoreErrorRow(onRetry = onLoadMore) }
        }
    }
}

@Composable
private fun NotificationSectionHeader(section: NotificationSection) {
    Text(
        text = stringResource(section.title).uppercase(),
        color = AppTextSecondary,
        fontSize = SECTION_TITLE_SIZE,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = SECTION_TITLE_TRACKING,
        modifier = Modifier.padding(start = Spacing.xxs, top = Spacing.md, bottom = Spacing.xxs),
    )
}

@Preview
@Composable
private fun NotificationsListPreview() {
    AppPreview {
        NotificationsList(
            sections = NotificationsUiState(items = NotificationsMockData.items, currentPage = 1).sections,
            canLoadMore = false,
            isLoadingMore = false,
            loadMoreFailed = true,
            onLoadMore = {},
            onAcceptFriendRequest = {},
            onDeclineFriendRequest = {},
        )
    }
}
