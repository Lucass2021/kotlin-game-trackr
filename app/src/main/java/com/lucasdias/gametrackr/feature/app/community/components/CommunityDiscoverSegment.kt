package com.lucasdias.gametrackr.feature.app.community.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.discover.DiscoverCommunitiesActions
import com.lucasdias.gametrackr.feature.app.community.discover.DiscoverCommunitiesContent
import com.lucasdias.gametrackr.feature.app.community.discover.DiscoverCommunitiesUiState

@Composable
fun CommunityDiscoverSegment(
    state: DiscoverCommunitiesUiState,
    actions: DiscoverCommunitiesActions,
    isGuest: Boolean,
    currentUserId: Int?,
    onCreateAccount: () -> Unit,
) {
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

    DiscoverCommunitiesContent(state = state, actions = actions, currentUserId = currentUserId)

    if (state.communityToLeave != null) {
        ConfirmDialog(
            title = stringResource(R.string.community_leave_title),
            message = stringResource(R.string.community_leave_message),
            confirmLabel = stringResource(R.string.community_leave_confirm),
            dismissLabel = stringResource(R.string.community_leave_cancel),
            onConfirm = actions.onLeaveConfirmed,
            onDismiss = actions.onLeaveDismissed,
        )
    }
}

@Preview
@Composable
private fun CommunityDiscoverSegmentPreview() {
    AppPreview {
        CommunityDiscoverSegment(
            state = DiscoverCommunitiesUiState(communities = CommunityMockData.all, currentPage = 1),
            actions = DiscoverCommunitiesActions(),
            isGuest = false,
            currentUserId = null,
            onCreateAccount = {},
        )
    }
}
