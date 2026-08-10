package com.lucasdias.gametrackr.feature.app.community.postdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.network.CommunityApi
import com.lucasdias.gametrackr.core.network.dto.CommentBody
import com.lucasdias.gametrackr.core.network.dto.toDomain
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.PostComment
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCommentsSheet(
    postId: Long,
    comments: List<PostComment>,
    isGuest: Boolean = false,
    currentUserId: Int? = null,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    api: CommunityApi = koinInject(),
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val state = remember { comments.toMutableStateList() }
    var draft by remember { mutableStateOf("") }
    var replyingTo by remember { mutableStateOf<PostComment?>(null) }
    val scope = rememberCoroutineScope()

    val total = state.sumOf { 1 + it.replies.size + it.hiddenReplies }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppSurfaceCard,
        modifier = modifier,
    ) {
        Column(modifier = Modifier.fillMaxHeight(0.85f)) {
            Header(total = total, onClose = onDismiss)

            if (state.isEmpty()) {
                EmptyState(modifier = Modifier.weight(1f))
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(22.dp),
                ) {
                    state.forEachIndexed { index, comment ->
                        item(key = comment.id) {
                            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                                CommentRow(
                                    comment = comment,
                                    onLike = {
                                        toggleLikeWithApi(state, index, postId, comment.id, api, scope)
                                    },
                                    isGuest = isGuest,
                                    currentUserId = currentUserId,
                                    onReply = { replyingTo = comment },
                                    onDelete = { state.removeAt(index) },
                                )

                                comment.replies.forEachIndexed { replyIndex, reply ->
                                    CommentRow(
                                        comment = reply,
                                        isReply = true,
                                        onLike = { toggleReplyLike(state, index, replyIndex) },
                                        isGuest = isGuest,
                                        currentUserId = currentUserId,
                                        onReply = { replyingTo = reply },
                                        onDelete = {
                                            val parent = state[index]
                                            val updatedReplies = parent.replies.toMutableList()
                                            updatedReplies.removeAt(replyIndex)
                                            state[index] = parent.copy(replies = updatedReplies)
                                        },
                                    )
                                }

                                if (comment.hiddenReplies > 0) {
                                    Text(
                                        text = "View ${comment.hiddenReplies} more replies",
                                        color = AppTextSecondary,
                                        style = AppType.label(13.sp),
                                        modifier = Modifier.padding(start = 62.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (!isGuest) {
                if (replyingTo != null) {
                    ReplyIndicator(
                        authorName = replyingTo!!.author,
                        onClose = { replyingTo = null },
                    )
                }
                CommentComposer(
                    draft = draft,
                    onDraftChange = { draft = it },
                    onSend = {
                        val text = draft.trim()
                        if (text.isBlank()) return@CommentComposer
                        draft = ""
                        val target = replyingTo
                        replyingTo = null
                        scope.launch {
                            try {
                                if (target != null) {
                                    val response = api.replyToComment(postId, target.id, CommentBody(comment = text))
                                    val newReply = response.reply.toDomain()
                                    val parentIndex = state.indexOfFirst { it.id == target.id || it.replies.any { r -> r.id == target.id } }
                                    if (parentIndex >= 0) {
                                        val parent = state[parentIndex]
                                        state[parentIndex] = parent.copy(replies = parent.replies + newReply)
                                    }
                                } else {
                                    val response = api.addComment(postId, CommentBody(comment = text))
                                    state.add(response.comment.toDomain())
                                }
                            } catch (_: Exception) {
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun Header(
    total: Int,
    onClose: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Comments",
                color = AppTextPrimary,
                style = AppType.headline(20.sp),
            )
            Text(
                text = total.toString(),
                color = AppTextSecondary,
                style = AppType.body(15.sp),
                modifier = Modifier.padding(start = 8.dp).weight(1f),
            )
            Box(
                modifier =
                    Modifier
                        .pressScale(interactionSource)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(AppBackground)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClickLabel = stringResource(R.string.community_action_close),
                            role = Role.Button,
                            onClick = onClose,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = AppIcon.CLOSE.image(),
                    contentDescription = stringResource(R.string.community_action_close),
                    tint = AppTextSecondary,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = AppOutline)
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = AppIcon.COMMENT.image(),
            contentDescription = null,
            tint = AppTextSecondary,
            modifier = Modifier.size(44.dp),
        )
        Text(
            text = "No comments yet",
            color = AppTextPrimary,
            style = AppType.headline(20.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp),
        )
        Text(
            text = "Be the first to comment.",
            color = AppTextSecondary,
            style = AppType.body(15.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

private fun toggleLikeWithApi(
    state: SnapshotStateList<PostComment>,
    index: Int,
    postId: Long,
    commentId: Long,
    api: CommunityApi,
    scope: kotlinx.coroutines.CoroutineScope,
) {
    val c = state[index]
    state[index] = c.copy(isLiked = !c.isLiked, likes = c.likes + if (c.isLiked) -1 else 1)

    scope.launch {
        try {
            val response = api.toggleCommentLike(postId, commentId)
            val i = state.indexOfFirst { it.id == commentId }
            if (i >= 0) {
                state[i] = state[i].copy(isLiked = response.isLiked, likes = response.likes)
            }
        } catch (_: Exception) {
            val i = state.indexOfFirst { it.id == commentId }
            if (i >= 0) {
                state[i] = c
            }
        }
    }
}

private fun toggleReplyLike(
    state: SnapshotStateList<PostComment>,
    index: Int,
    replyIndex: Int,
) {
    val parent = state[index]
    val replies = parent.replies.toMutableList()
    val r = replies[replyIndex]
    replies[replyIndex] = r.copy(isLiked = !r.isLiked, likes = r.likes + if (r.isLiked) -1 else 1)
    state[index] = parent.copy(replies = replies)
}

@Composable
private fun ReplyIndicator(
    authorName: String,
    onClose: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier.fillMaxWidth().background(AppSurfaceCard).padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Replying to $authorName",
            color = AppTextSecondary,
            style = AppType.body(13.sp),
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = AppIcon.CLOSE.image(),
            contentDescription = stringResource(R.string.community_action_close),
            tint = AppTextSecondary,
            modifier =
                Modifier
                    .pressScale(interactionSource)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        role = Role.Button,
                        onClick = onClose,
                    ).size(16.dp),
        )
    }
}
