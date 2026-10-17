package com.lucasdias.gametrackr.feature.app.community.communitydetail.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.feature.app.community.components.ConfirmDialog

@Composable
fun CommunityDetailDialogs(
    showLeaveConfirmation: Boolean,
    showDeleteConfirmation: Boolean,
    onLeaveConfirmed: () -> Unit,
    onLeaveDismissed: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    onDeleteDismissed: () -> Unit,
) {
    if (showLeaveConfirmation) {
        ConfirmDialog(
            title = stringResource(R.string.community_leave_title),
            message = stringResource(R.string.community_leave_message),
            confirmLabel = stringResource(R.string.community_leave_confirm),
            dismissLabel = stringResource(R.string.community_leave_cancel),
            onConfirm = onLeaveConfirmed,
            onDismiss = onLeaveDismissed,
        )
    }

    if (showDeleteConfirmation) {
        ConfirmDialog(
            title = stringResource(R.string.community_delete_title),
            message = stringResource(R.string.community_delete_message),
            confirmLabel = stringResource(R.string.community_delete_confirm),
            dismissLabel = stringResource(R.string.community_delete_cancel),
            onConfirm = onDeleteConfirmed,
            onDismiss = onDeleteDismissed,
        )
    }
}

@Preview
@Composable
private fun CommunityDetailDialogsPreview() {
    AppPreview {
        CommunityDetailDialogs(
            showLeaveConfirmation = true,
            showDeleteConfirmation = false,
            onLeaveConfirmed = {},
            onLeaveDismissed = {},
            onDeleteConfirmed = {},
            onDeleteDismissed = {},
        )
    }
}
