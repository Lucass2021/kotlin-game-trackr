package com.lucasdias.gametrackr.feature.app.community.postdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.model.PostComment
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.shareText
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.components.ConfirmDialog
import com.lucasdias.gametrackr.feature.app.community.postdetail.components.PostDetailContent
import com.lucasdias.gametrackr.feature.app.community.postdetail.components.PostDetailTopBar
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PostDetailScreen(
    post: CommunityPost,
    onBack: () -> Unit,
    onCommunityClick: (Community) -> Unit,
    onAuthorClick: () -> Unit,
    modifier: Modifier = Modifier,
    isGuest: Boolean = false,
    currentUserId: Int? = null,
    onCreateAccount: () -> Unit = {},
    onDelete: () -> Unit = {},
    viewModel: PostDetailViewModel = koinViewModel { parametersOf(post) },
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) { viewModel.loadIfNeeded() }
    LaunchedEffect(state.communityToOpen) {
        state.communityToOpen?.let { community ->
            onCommunityClick(community)
            viewModel.onCommunityOpened()
        }
    }
    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) {
            onDelete()
            onBack()
        }
    }

    PostDetailScreen(
        state = state,
        isGuest = isGuest,
        isOwnPost = currentUserId != null && post.authorId == currentUserId,
        modifier = modifier,
        onBack = onBack,
        onCommunityClick = viewModel::onCommunityTap,
        onAuthorClick = onAuthorClick,
        onCreateAccount = onCreateAccount,
        onLike = viewModel::onLike,
        onBookmark = viewModel::onBookmark,
        onCommentsOpen = viewModel::onCommentsOpen,
        onCommentsDismiss = viewModel::onCommentsDismiss,
        onRetryComments = viewModel::loadComments,
        onCommentLike = viewModel::onCommentLike,
        onReplyTap = viewModel::onReplyTap,
        onReplyCancel = viewModel::onReplyCancel,
        onDraftChange = viewModel::onDraftChange,
        onSend = viewModel::onSend,
        onDeleteRequest = viewModel::onDeleteRequest,
        onDeleteConfirmed = viewModel::onDeleteConfirmed,
        onDeleteDismissed = viewModel::onDeleteDismissed,
        onToastShown = viewModel::onToastShown,
    )
}

@Composable
private fun PostDetailScreen(
    state: PostDetailUiState,
    isGuest: Boolean,
    isOwnPost: Boolean,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onCommunityClick: () -> Unit = {},
    onAuthorClick: () -> Unit = {},
    onCreateAccount: () -> Unit = {},
    onLike: () -> Unit = {},
    onBookmark: () -> Unit = {},
    onCommentsOpen: () -> Unit = {},
    onCommentsDismiss: () -> Unit = {},
    onRetryComments: () -> Unit = {},
    onCommentLike: (Long) -> Unit = {},
    onReplyTap: (PostComment) -> Unit = {},
    onReplyCancel: () -> Unit = {},
    onDraftChange: (String) -> Unit = {},
    onSend: () -> Unit = {},
    onDeleteRequest: () -> Unit = {},
    onDeleteConfirmed: () -> Unit = {},
    onDeleteDismissed: () -> Unit = {},
    onToastShown: () -> Unit = {},
) {
    val context = LocalContext.current
    val shareMessage = stringResource(R.string.post_detail_share_message, state.post.title)

    Box(modifier = modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
        Column {
            PostDetailTopBar(isOwnPost = isOwnPost, onBack = onBack, onDeleteRequest = onDeleteRequest)
            PostDetailContent(
                post = state.post,
                commentCount = state.commentCount,
                isGuest = isGuest,
                onAuthorClick = onAuthorClick,
                onCommunityClick = onCommunityClick,
                onLike = onLike,
                onComment = onCommentsOpen,
                onShare = { context.shareText(shareMessage) },
                onBookmark = onBookmark,
                onCreateAccount = onCreateAccount,
            )
        }

        Toast(
            message = if (state.showComments) null else state.toastMessage?.let { stringResource(it) },
            onDismiss = onToastShown,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }

    if (state.showComments) {
        PostCommentsSheet(
            state = state,
            isGuest = isGuest,
            onDismiss = onCommentsDismiss,
            onRetry = onRetryComments,
            onCommentLike = onCommentLike,
            onReplyTap = onReplyTap,
            onReplyCancel = onReplyCancel,
            onDraftChange = onDraftChange,
            onSend = onSend,
            onToastShown = onToastShown,
        )
    }

    if (state.showDeleteConfirmation) {
        ConfirmDialog(
            title = stringResource(R.string.post_detail_delete_title),
            message = stringResource(R.string.post_detail_delete_message),
            confirmLabel = stringResource(R.string.post_detail_delete_confirm),
            dismissLabel = stringResource(R.string.post_detail_delete_cancel),
            onConfirm = onDeleteConfirmed,
            onDismiss = onDeleteDismissed,
        )
    }
}

@Preview
@Composable
private fun PostDetailScreenPreview() {
    AppPreview {
        PostDetailScreen(state = PostDetailUiState(post = CommunityMockData.detailPost), isGuest = false, isOwnPost = true)
    }
}

@Preview
@Composable
private fun PostDetailScreenGuestPreview() {
    AppPreview {
        PostDetailScreen(state = PostDetailUiState(post = CommunityMockData.detailPost), isGuest = true, isOwnPost = false)
    }
}
