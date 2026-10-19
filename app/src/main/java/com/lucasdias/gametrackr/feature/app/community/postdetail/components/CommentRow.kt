package com.lucasdias.gametrackr.feature.app.community.postdetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.PostComment
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.components.CommunityAvatar

private val AVATAR_SIZE = 36.dp
private val REPLY_AVATAR_SIZE = 28.dp
private val LIKE_ICON_SIZE = 16.dp
private val AUTHOR_SIZE = 14.sp
private val TIME_SIZE = 12.sp
private val CONTENT_SIZE = 14.sp
private val META_SIZE = 12.sp

@Composable
fun CommentRow(
    comment: PostComment,
    onLike: () -> Unit,
    modifier: Modifier = Modifier,
    isReply: Boolean = false,
    isGuest: Boolean = false,
    onReply: () -> Unit = {},
) {
    Row(
        modifier = modifier.padding(start = if (isReply) Spacing.xxxl else 0.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        CommunityAvatar(start = comment.avatarStart, end = comment.avatarEnd, size = if (isReply) REPLY_AVATAR_SIZE else AVATAR_SIZE)
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                Text(text = comment.author, color = AppPrimary, style = AppType.label(AUTHOR_SIZE))
                Text(text = comment.timeAgo, color = AppTextSecondary, style = AppType.body(TIME_SIZE))
            }
            Text(text = comment.content, color = AppTextPrimary, style = AppType.body(CONTENT_SIZE))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
                MetaText(text = stringResource(R.string.post_detail_comment_likes, comment.likes))
                if (!isGuest) {
                    val replyInteraction = remember { MutableInteractionSource() }
                    MetaText(text = "·")
                    MetaText(
                        text = stringResource(R.string.post_detail_reply),
                        modifier =
                            Modifier
                                .pressScale(replyInteraction)
                                .clickable(interactionSource = replyInteraction, indication = null, role = Role.Button, onClick = onReply),
                    )
                }
            }
        }
        if (!isGuest) {
            val likeInteraction = remember { MutableInteractionSource() }
            val likeLabel =
                stringResource(if (comment.isLiked) R.string.community_action_unlike_comment else R.string.community_action_like_comment)
            Icon(
                imageVector = AppIcon.LIKE.image(filled = comment.isLiked),
                contentDescription = likeLabel,
                tint = if (comment.isLiked) AppPrimary else AppTextSecondary,
                modifier =
                    Modifier
                        .pressScale(likeInteraction)
                        .clickable(
                            interactionSource = likeInteraction,
                            indication = null,
                            onClickLabel = likeLabel,
                            role = Role.Button,
                            onClick = onLike,
                        ).size(LIKE_ICON_SIZE),
            )
        }
    }
}

@Composable
private fun MetaText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(text = text, color = AppTextSecondary, style = AppType.label(META_SIZE), modifier = modifier)
}

@Preview
@Composable
private fun CommentRowPreview() {
    AppPreview {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
            CommentRow(comment = CommunityMockData.comments[0], onLike = {})
            CommentRow(comment = CommunityMockData.comments[0].replies[0], onLike = {}, isReply = true)
            CommentRow(comment = CommunityMockData.comments[1], onLike = {}, isGuest = true)
        }
    }
}
