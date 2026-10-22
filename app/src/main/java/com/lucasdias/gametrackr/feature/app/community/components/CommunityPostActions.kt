package com.lucasdias.gametrackr.feature.app.community.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.format.abbreviated
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData

private val ACTION_ICON_SIZE = 20.dp
private val ACTION_VALUE_SIZE = 14.sp

@Composable
fun CommunityPostActions(
    post: CommunityPost,
    isGuest: Boolean,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit,
    onBookmark: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        if (!isGuest) {
            ActionButton(
                icon = AppIcon.LIKE,
                label = stringResource(if (post.isLiked) R.string.community_action_unlike else R.string.community_action_like),
                filled = post.isLiked,
                tint = if (post.isLiked) AppPrimary else AppTextSecondary,
                value = post.likes.abbreviated(),
                onClick = onLike,
            )
            Spacer(Modifier.size(Spacing.lg))
        }
        ActionButton(
            icon = AppIcon.COMMENT,
            label = stringResource(R.string.community_action_comment),
            value = post.comments.abbreviated(),
            onClick = onComment,
        )
        if (!isGuest) {
            Spacer(Modifier.size(Spacing.lg))
            ActionButton(
                icon = AppIcon.SHARE,
                label = stringResource(R.string.community_action_share),
                onClick = onShare,
            )
        }
        Spacer(Modifier.weight(1f))
        if (!isGuest) {
            ActionButton(
                icon = AppIcon.BOOKMARK,
                label = stringResource(if (post.isBookmarked) R.string.community_action_unbookmark else R.string.community_action_bookmark),
                filled = post.isBookmarked,
                tint = if (post.isBookmarked) AppPrimary else AppTextSecondary,
                onClick = onBookmark,
            )
        }
    }
}

@Composable
private fun ActionButton(
    icon: AppIcon,
    label: String,
    onClick: () -> Unit,
    filled: Boolean = false,
    tint: Color = AppTextSecondary,
    value: String? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = label,
                    role = Role.Button,
                    onClick = onClick,
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon.image(filled = filled),
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(ACTION_ICON_SIZE),
        )
        if (value != null) {
            Text(
                text = value,
                color = tint,
                style = AppType.body(ACTION_VALUE_SIZE),
                modifier = Modifier.padding(start = Spacing.xs),
            )
        }
    }
}

@Preview
@Composable
private fun CommunityPostActionsPreview() {
    AppPreview {
        CommunityPostActions(
            post = CommunityMockData.feed.first(),
            isGuest = false,
            onLike = {},
            onComment = {},
            onShare = {},
            onBookmark = {},
            modifier = Modifier.padding(Spacing.md),
        )
    }
}
