package com.lucasdias.gametrackr.feature.app.community.postdetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
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
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData

private val ICON_SIZE = 22.dp
private val VALUE_SIZE = 15.sp
private val DIVIDER_THICKNESS = 1.dp

@Composable
fun PostDetailEngagementBar(
    post: CommunityPost,
    commentCount: Int,
    isGuest: Boolean,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit,
    onBookmark: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        HorizontalDivider(thickness = DIVIDER_THICKNESS, color = AppOutline)
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.lg),
        ) {
            if (!isGuest) {
                EngagementAction(
                    icon = AppIcon.LIKE,
                    label = stringResource(if (post.isLiked) R.string.community_action_unlike else R.string.community_action_like),
                    filled = post.isLiked,
                    tint = if (post.isLiked) AppPrimary else AppTextSecondary,
                    value = post.likes.abbreviated(),
                    onClick = onLike,
                )
            }
            EngagementAction(
                icon = AppIcon.COMMENT,
                label = stringResource(R.string.community_action_comment),
                value = commentCount.abbreviated(),
                onClick = onComment,
            )
            if (!isGuest) {
                EngagementAction(icon = AppIcon.SHARE, label = stringResource(R.string.community_action_share), onClick = onShare)
                Spacer(Modifier.weight(1f))
                EngagementAction(
                    icon = AppIcon.BOOKMARK,
                    label =
                        stringResource(
                            if (post.isBookmarked) R.string.community_action_unbookmark else R.string.community_action_bookmark,
                        ),
                    filled = post.isBookmarked,
                    tint = if (post.isBookmarked) AppPrimary else AppTextSecondary,
                    onClick = onBookmark,
                )
            }
        }
        HorizontalDivider(thickness = DIVIDER_THICKNESS, color = AppOutline)
    }
}

@Composable
private fun EngagementAction(
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
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        Icon(
            imageVector = icon.image(filled = filled),
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(ICON_SIZE),
        )
        if (value != null) {
            Text(text = value, color = tint, style = AppType.body(VALUE_SIZE))
        }
    }
}

@Preview
@Composable
private fun PostDetailEngagementBarPreview() {
    AppPreview {
        PostDetailEngagementBar(
            post = CommunityMockData.detailPost,
            commentCount = CommunityMockData.detailPost.comments,
            isGuest = false,
            onLike = {},
            onComment = {},
            onShare = {},
            onBookmark = {},
        )
    }
}

@Preview
@Composable
private fun PostDetailEngagementBarGuestPreview() {
    AppPreview {
        PostDetailEngagementBar(
            post = CommunityMockData.detailPost,
            commentCount = CommunityMockData.detailPost.comments,
            isGuest = true,
            onLike = {},
            onComment = {},
            onShare = {},
            onBookmark = {},
        )
    }
}
