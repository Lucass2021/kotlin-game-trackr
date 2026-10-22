package com.lucasdias.gametrackr.feature.app.community.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.discover.DiscoverCommunitiesContent

@Composable
fun CommunityDiscoverSegment(
    communities: List<Community>,
    isLoadingMore: Boolean,
    canLoadMore: Boolean,
    isGuest: Boolean,
    currentUserId: Int?,
    onCommunityClick: (Community) -> Unit,
    onJoin: (Community) -> Unit,
    onLeave: (Community) -> Unit,
    onLoadMore: () -> Unit,
    onCreateAccount: () -> Unit,
) {
    var category by remember { mutableStateOf("All") }
    var communityToLeave by remember { mutableStateOf<Community?>(null) }

    if (isGuest) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            EmptyState(
                icon = AppIcon.COMMUNITY,
                title = stringResource(R.string.discover_guest_title),
                message = stringResource(R.string.discover_guest_message),
                actionTitle = stringResource(R.string.discover_guest_action),
                onAction = onCreateAccount,
            )
        }
        return
    }

    DiscoverCommunitiesContent(
        category = category,
        onCategorySelect = { category = it },
        communities = communities,
        isLoadingMore = isLoadingMore,
        canLoadMore = canLoadMore,
        onCommunitySelect = onCommunityClick,
        onJoin = { community -> if (community.isJoined) communityToLeave = community else onJoin(community) },
        onLoadMore = onLoadMore,
        currentUserId = currentUserId,
    )

    communityToLeave?.let { community ->
        ConfirmDialog(
            title = stringResource(R.string.community_leave_title),
            message = stringResource(R.string.community_leave_message),
            confirmLabel = stringResource(R.string.community_leave_confirm),
            dismissLabel = stringResource(R.string.community_leave_cancel),
            onConfirm = {
                communityToLeave = null
                onLeave(community)
            },
            onDismiss = { communityToLeave = null },
        )
    }
}

@Preview
@Composable
private fun CommunityDiscoverSegmentPreview() {
    AppPreview {
        CommunityDiscoverSegment(
            communities = CommunityMockData.all,
            isLoadingMore = false,
            canLoadMore = false,
            isGuest = false,
            currentUserId = null,
            onCommunityClick = {},
            onJoin = {},
            onLeave = {},
            onLoadMore = {},
            onCreateAccount = {},
        )
    }
}
