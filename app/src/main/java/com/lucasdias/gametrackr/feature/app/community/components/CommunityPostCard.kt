package com.lucasdias.gametrackr.feature.app.community.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.format.abbreviated
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.CommunityPost

@Composable
fun CommunityPostCard(
    post: CommunityPost,
    modifier: Modifier = Modifier,
    showsCommunityName: Boolean = true,
    onSelect: () -> Unit = {},
    onLike: () -> Unit = {},
    onComment: () -> Unit = {},
    onShare: () -> Unit = {},
    onBookmark: () -> Unit = {},
) {
    val shape = RoundedCornerShape(16.dp)
    val cardInteraction = remember { MutableInteractionSource() }
    val metadata = if (showsCommunityName) "${post.timeAgo} · ${post.communityName}" else post.timeAgo

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .pressScale(cardInteraction)
                    .clickable(interactionSource = cardInteraction, indication = null, onClick = onSelect),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Header(post = post, metadata = metadata)

            Text(
                text = post.title,
                color = AppTextPrimary,
                style = AppType.headline(19.sp),
            )
            Text(
                text = post.preview,
                color = AppTextSecondary,
                style = AppType.body(15.sp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            if (post.hasMedia) {
                Media(post = post)
            }
        }

        Actions(
            post = post,
            onLike = onLike,
            onComment = onComment,
            onShare = onShare,
            onBookmark = onBookmark,
        )
    }
}

@Composable
private fun Header(
    post: CommunityPost,
    metadata: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CommunityAvatar(start = post.avatarStart, end = post.avatarEnd, size = 40.dp)
        Column(modifier = Modifier.padding(start = 12.dp, end = 32.dp).weight(1f)) {
            Text(
                text = post.author,
                color = AppPrimary,
                style = AppType.label(15.sp),
            )
            Text(
                text = metadata,
                color = AppTextSecondary,
                style = AppType.body(13.sp),
            )
        }
        Icon(
            imageVector = AppIcon.OVERFLOW.image(),
            contentDescription = null,
            tint = AppTextSecondary,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun Media(post: CommunityPost) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(shape)
                .background(Brush.linearGradient(listOf(post.mediaStart, post.mediaEnd)))
                .border(1.dp, AppOutline, shape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = AppIcon.BRAND.image(),
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.15f),
            modifier = Modifier.size(40.dp),
        )
    }
}

@Composable
private fun Actions(
    post: CommunityPost,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit,
    onBookmark: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        ActionButton(
            icon = AppIcon.LIKE,
            label = stringResource(if (post.isLiked) R.string.community_action_unlike else R.string.community_action_like),
            filled = post.isLiked,
            tint = if (post.isLiked) AppPrimary else AppTextSecondary,
            value = post.likes.abbreviated(),
            onClick = onLike,
        )
        Spacer(Modifier.size(20.dp))
        ActionButton(
            icon = AppIcon.COMMENT,
            label = stringResource(R.string.community_action_comment),
            value = post.comments.abbreviated(),
            onClick = onComment,
        )
        Spacer(Modifier.size(20.dp))
        ActionButton(
            icon = AppIcon.SHARE,
            label = stringResource(R.string.community_action_share),
            onClick = onShare,
        )
        Spacer(Modifier.weight(1f))
        ActionButton(
            icon = AppIcon.BOOKMARK,
            label = stringResource(if (post.isBookmarked) R.string.community_action_unbookmark else R.string.community_action_bookmark),
            filled = post.isBookmarked,
            tint = if (post.isBookmarked) AppPrimary else AppTextSecondary,
            onClick = onBookmark,
        )
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
            modifier = Modifier.size(20.dp),
        )
        if (value != null) {
            Text(
                text = value,
                color = tint,
                style = AppType.body(14.sp),
                modifier = Modifier.padding(start = 7.dp),
            )
        }
    }
}
