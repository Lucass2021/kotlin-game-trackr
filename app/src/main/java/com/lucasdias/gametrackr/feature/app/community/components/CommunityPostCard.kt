package com.lucasdias.gametrackr.feature.app.community.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData

private val BORDER_WIDTH = 1.dp
private val AVATAR_SIZE = 40.dp
private val MEDIA_ICON_SIZE = 40.dp
private val TITLE_SIZE = 19.sp
private val PREVIEW_SIZE = 15.sp
private val AUTHOR_SIZE = 15.sp
private val METADATA_SIZE = 13.sp
private const val MEDIA_ASPECT_RATIO = 16f / 9f
private const val MEDIA_ICON_ALPHA = 0.15f
private const val PREVIEW_MAX_LINES = 2

@Composable
fun CommunityPostCard(
    post: CommunityPost,
    modifier: Modifier = Modifier,
    isGuest: Boolean = false,
    showsCommunityName: Boolean = true,
    onSelect: () -> Unit = {},
    onLike: () -> Unit = {},
    onComment: () -> Unit = {},
    onShare: () -> Unit = {},
    onBookmark: () -> Unit = {},
) {
    val shape = RoundedCornerShape(Radius.lg)
    val cardInteraction = remember { MutableInteractionSource() }
    val metadata = if (showsCommunityName) "${post.timeAgo} · ${post.communityName}" else post.timeAgo

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppOutline, shape)
                .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        Column(
            modifier =
                Modifier
                    .pressScale(cardInteraction)
                    .clickable(interactionSource = cardInteraction, indication = null, onClick = onSelect),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Header(post = post, metadata = metadata)

            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                Text(
                    text = post.title,
                    color = AppTextPrimary,
                    style = AppType.headline(TITLE_SIZE),
                )
                Text(
                    text = post.preview,
                    color = AppTextSecondary,
                    style = AppType.body(PREVIEW_SIZE),
                    maxLines = PREVIEW_MAX_LINES,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (post.hasMedia) {
                Media(post = post)
            }
        }

        CommunityPostActions(
            post = post,
            isGuest = isGuest,
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
        CommunityAvatar(start = post.avatarStart, end = post.avatarEnd, size = AVATAR_SIZE)
        Column(
            modifier = Modifier.padding(start = Spacing.sm).weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xxxs),
        ) {
            Text(
                text = post.author,
                color = AppPrimary,
                style = AppType.label(AUTHOR_SIZE),
            )
            Text(
                text = metadata,
                color = AppTextSecondary,
                style = AppType.body(METADATA_SIZE),
            )
        }
    }
}

@Composable
private fun Media(post: CommunityPost) {
    val shape = RoundedCornerShape(Radius.md)
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(MEDIA_ASPECT_RATIO)
                .clip(shape)
                .background(Brush.linearGradient(listOf(post.mediaStart, post.mediaEnd)))
                .border(BORDER_WIDTH, AppOutline, shape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = AppIcon.BRAND.image(),
            contentDescription = null,
            tint = Color.White.copy(alpha = MEDIA_ICON_ALPHA),
            modifier = Modifier.size(MEDIA_ICON_SIZE),
        )
    }
}

@Preview
@Composable
private fun CommunityPostCardPreview() {
    AppPreview {
        Column(
            modifier = Modifier.padding(Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            CommunityPostCard(post = CommunityMockData.feed.first())
            CommunityPostCard(post = CommunityMockData.feed.last().copy(isBookmarked = true), isGuest = true)
        }
    }
}
