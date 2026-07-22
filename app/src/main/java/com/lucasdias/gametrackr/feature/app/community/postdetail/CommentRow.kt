package com.lucasdias.gametrackr.feature.app.community.postdetail

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.PostComment
import com.lucasdias.gametrackr.feature.app.community.components.CommunityAvatar

@Composable
fun CommentRow(
    comment: PostComment,
    onLike: () -> Unit,
    modifier: Modifier = Modifier,
    isReply: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val avatarSize = if (isReply) 28.dp else 36.dp
    Row(
        modifier = modifier.padding(start = if (isReply) 40.dp else 0.dp),
        verticalAlignment = Alignment.Top,
    ) {
        CommunityAvatar(start = comment.avatarStart, end = comment.avatarEnd, size = avatarSize)
        Column(
            modifier = Modifier.padding(start = 12.dp).weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = comment.author,
                    color = AppPrimary,
                    style = AppType.label(14.sp),
                )
                Text(
                    text = comment.timeAgo,
                    color = AppTextSecondary,
                    style = AppType.body(12.sp),
                )
            }
            Text(
                text = comment.content,
                color = AppTextPrimary,
                style = AppType.body(14.sp),
            )
            Text(
                text = "${comment.likes} likes · Reply",
                color = AppTextSecondary,
                style = AppType.label(12.sp),
            )
        }
        val likeLabel =
            stringResource(
                if (comment.isLiked) R.string.community_action_unlike_comment else R.string.community_action_like_comment,
            )
        Icon(
            imageVector = AppIcon.LIKE.image(filled = comment.isLiked),
            contentDescription = likeLabel,
            tint = if (comment.isLiked) AppPrimary else AppTextSecondary,
            modifier =
                Modifier
                    .padding(start = 8.dp)
                    .pressScale(interactionSource)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClickLabel = likeLabel,
                        role = Role.Button,
                        onClick = onLike,
                    ).size(16.dp),
        )
    }
}
