package com.lucasdias.gametrackr.feature.app.community.postdetail

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.format.abbreviated
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.CommunityPost
import com.lucasdias.gametrackr.feature.app.community.components.CommunityAvatar

@Composable
fun PostDetailScreen(
    post: CommunityPost,
    onBack: () -> Unit,
    onCommunityClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isLiked by remember { mutableStateOf(post.isLiked) }
    var likes by remember { mutableIntStateOf(post.likes) }
    var isBookmarked by remember { mutableStateOf(post.isBookmarked) }
    var isFollowing by remember { mutableStateOf(false) }
    var showComments by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
        TopBar(onBack = onBack)

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            AuthorRow(post = post, isFollowing = isFollowing, onFollow = { isFollowing = !isFollowing })
            CommunityChip(name = post.communityName, onClick = onCommunityClick)
            Text(
                text = post.title,
                color = AppTextPrimary,
                style = AppType.headline(28.sp, FontWeight.ExtraBold),
            )
            Text(
                text = post.preview,
                color = AppTextPrimary.copy(alpha = 0.9f),
                style = AppType.body(16.sp),
                lineHeight = 24.sp,
            )
            Highlights()
            if (post.hasMedia) {
                Media(post = post)
            }
            EngagementBar(
                isLiked = isLiked,
                likes = likes,
                comments = post.comments,
                isBookmarked = isBookmarked,
                onLike = {
                    isLiked = !isLiked
                    likes += if (isLiked) 1 else -1
                },
                onComment = { showComments = true },
                onBookmark = { isBookmarked = !isBookmarked },
            )
        }
    }

    if (showComments) {
        PostCommentsSheet(
            comments = CommunityMockData.comments,
            onDismiss = { showComments = false },
        )
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
    Column {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButtonGhost(
                icon = AppIcon.BACK,
                label = stringResource(R.string.community_action_back),
                onClick = onBack,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "GameTrackr",
                color = AppPrimary,
                style = AppType.headline(20.sp, FontWeight.ExtraBold),
            )
            Spacer(Modifier.weight(1f))
            IconButtonGhost(
                icon = AppIcon.OVERFLOW,
                label = stringResource(R.string.community_action_more),
                onClick = {},
            )
        }
        HorizontalDivider(thickness = 1.dp, color = AppOutline)
    }
}

@Composable
private fun IconButtonGhost(
    icon: AppIcon,
    label: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .size(40.dp)
                .clip(CircleShape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = label,
                    role = Role.Button,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon.image(),
            contentDescription = label,
            tint = AppTextPrimary,
            modifier = Modifier.size(22.dp),
        )
    }
}

@Composable
private fun AuthorRow(
    post: CommunityPost,
    isFollowing: Boolean,
    onFollow: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(verticalAlignment = Alignment.CenterVertically) {
        CommunityAvatar(start = post.avatarStart, end = post.avatarEnd, size = 44.dp)
        Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
            Text(
                text = post.author,
                color = AppPrimary,
                style = AppType.label(15.sp),
            )
            Text(
                text = post.timeAgo,
                color = AppTextSecondary,
                style = AppType.body(13.sp),
            )
        }
        val shape = CircleShape
        Text(
            text = if (isFollowing) "Following" else "Follow",
            color = if (isFollowing) AppOnPrimary else AppTextPrimary,
            style = AppType.label(14.sp),
            modifier =
                Modifier
                    .pressScale(interactionSource)
                    .clip(shape)
                    .background(if (isFollowing) AppPrimary else Color.Transparent)
                    .then(if (isFollowing) Modifier else Modifier.border(1.dp, AppOutline, shape))
                    .clickable(interactionSource = interactionSource, indication = null, onClick = onFollow)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
        )
    }
}

@Composable
private fun CommunityChip(
    name: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = CircleShape
    Row(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
                .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = AppIcon.COMMUNITY.image(),
            contentDescription = null,
            tint = AppTextSecondary,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = name,
            color = AppTextSecondary,
            style = AppType.label(13.sp),
        )
    }
}

@Composable
private fun Highlights() {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        CommunityMockData.detailPostHighlights.forEach { highlight ->
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier =
                        Modifier
                            .padding(top = 8.dp, end = 12.dp)
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(AppPrimary),
                )
                Text(
                    text = highlight,
                    color = AppTextSecondary,
                    style = AppType.body(15.sp),
                    lineHeight = 22.sp,
                )
            }
        }
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
            modifier = Modifier.size(44.dp),
        )
    }
}

@Composable
private fun EngagementBar(
    isLiked: Boolean,
    likes: Int,
    comments: Int,
    isBookmarked: Boolean,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onBookmark: () -> Unit,
) {
    Column {
        HorizontalDivider(thickness = 1.dp, color = AppOutline)
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Action(
                icon = AppIcon.LIKE,
                label = stringResource(if (isLiked) R.string.community_action_unlike else R.string.community_action_like),
                filled = isLiked,
                tint = if (isLiked) AppPrimary else AppTextSecondary,
                value = likes.abbreviated(),
                onClick = onLike,
            )
            Spacer(Modifier.size(22.dp))
            Action(
                icon = AppIcon.COMMENT,
                label = stringResource(R.string.community_action_comment),
                value = comments.abbreviated(),
                onClick = onComment,
            )
            Spacer(Modifier.size(22.dp))
            Action(
                icon = AppIcon.SHARE,
                label = stringResource(R.string.community_action_share),
                onClick = {},
            )
            Spacer(Modifier.weight(1f))
            Action(
                icon = AppIcon.BOOKMARK,
                label = stringResource(if (isBookmarked) R.string.community_action_unbookmark else R.string.community_action_bookmark),
                filled = isBookmarked,
                tint = if (isBookmarked) AppPrimary else AppTextSecondary,
                onClick = onBookmark,
            )
        }
        HorizontalDivider(thickness = 1.dp, color = AppOutline)
    }
}

@Composable
private fun Action(
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
            modifier = Modifier.size(22.dp),
        )
        if (value != null) {
            Text(
                text = value,
                color = tint,
                style = AppType.body(15.sp),
                modifier = Modifier.padding(start = 7.dp),
            )
        }
    }
}
