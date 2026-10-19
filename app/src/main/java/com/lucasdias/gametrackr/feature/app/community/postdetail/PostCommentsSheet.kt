package com.lucasdias.gametrackr.feature.app.community.postdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.PostComment
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.postdetail.components.CommentComposer
import com.lucasdias.gametrackr.feature.app.community.postdetail.components.CommentRow
import com.lucasdias.gametrackr.feature.app.community.postdetail.components.CommentsHeader
import com.lucasdias.gametrackr.feature.app.community.postdetail.components.ReplyIndicator

private const val SHEET_HEIGHT_FRACTION = 0.85f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCommentsSheet(
    state: PostDetailUiState,
    isGuest: Boolean,
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
    onCommentLike: (Long) -> Unit,
    onReplyTap: (PostComment) -> Unit,
    onReplyCancel: () -> Unit,
    onDraftChange: (String) -> Unit,
    onSend: () -> Unit,
    onToastShown: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AppSurfaceCard,
        modifier = modifier,
    ) {
        Box(modifier = Modifier.fillMaxHeight(SHEET_HEIGHT_FRACTION)) {
            Column {
                CommentsHeader(total = state.commentCount, onClose = onDismiss)
                Box(modifier = Modifier.weight(1f)) {
                    CommentsBody(state = state, isGuest = isGuest, onRetry = onRetry, onCommentLike = onCommentLike, onReplyTap = onReplyTap)
                }
                if (!isGuest && state.hasLoadedComments) {
                    state.replyingTo?.let { ReplyIndicator(author = it.author, onCancel = onReplyCancel) }
                    CommentComposer(draft = state.draft, onDraftChange = onDraftChange, onSend = onSend)
                }
            }
            Toast(
                message = state.toastMessage?.let { stringResource(it) },
                onDismiss = onToastShown,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}

@Composable
private fun CommentsBody(
    state: PostDetailUiState,
    isGuest: Boolean,
    onRetry: () -> Unit,
    onCommentLike: (Long) -> Unit,
    onReplyTap: (PostComment) -> Unit,
) {
    when {
        state.commentsFailed -> {
            EmptyState(
                icon = AppIcon.INFO,
                title = stringResource(R.string.post_detail_comments_error_title),
                message = stringResource(R.string.post_detail_comments_error_message),
                actionTitle = stringResource(R.string.post_detail_retry),
                onAction = onRetry,
                modifier = Modifier.fillMaxSize(),
            )
        }

        !state.hasLoadedComments -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppPrimary)
            }
        }

        state.comments.isEmpty() -> {
            EmptyState(
                icon = AppIcon.COMMENT,
                title = stringResource(R.string.post_detail_comments_empty_title),
                message = stringResource(R.string.post_detail_comments_empty_message),
                modifier = Modifier.fillMaxSize(),
            )
        }

        else -> {
            LazyColumn(
                contentPadding = PaddingValues(Spacing.lg),
                verticalArrangement = Arrangement.spacedBy(Spacing.lg),
            ) {
                items(state.comments, key = { it.id }) { thread ->
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
                        CommentRow(
                            comment = thread,
                            onLike = { onCommentLike(thread.id) },
                            isGuest = isGuest,
                            onReply = { onReplyTap(thread) },
                        )
                        thread.replies.forEach { reply ->
                            CommentRow(
                                comment = reply,
                                onLike = { onCommentLike(reply.id) },
                                isReply = true,
                                isGuest = isGuest,
                                onReply = { onReplyTap(reply) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommentsBodyPreview() {
    AppPreview {
        CommentsBody(
            state = PostDetailUiState(post = CommunityMockData.detailPost, comments = CommunityMockData.comments, hasLoadedComments = true),
            isGuest = false,
            onRetry = {},
            onCommentLike = {},
            onReplyTap = {},
        )
    }
}

@Preview
@Composable
private fun CommentsBodyErrorPreview() {
    AppPreview {
        CommentsBody(
            state = PostDetailUiState(post = CommunityMockData.detailPost, commentsFailed = true),
            isGuest = false,
            onRetry = {},
            onCommentLike = {},
            onReplyTap = {},
        )
    }
}
