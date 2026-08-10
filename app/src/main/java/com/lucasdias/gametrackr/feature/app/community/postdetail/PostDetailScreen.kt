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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.lucasdias.gametrackr.core.network.CommunityApi
import com.lucasdias.gametrackr.core.network.dto.toDomain
import com.lucasdias.gametrackr.core.ui.components.glow
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.CommunityPost
import com.lucasdias.gametrackr.feature.app.community.PostComment
import com.lucasdias.gametrackr.feature.app.community.components.CommunityAvatar
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun PostDetailScreen(
    post: CommunityPost,
    isGuest: Boolean = false,
    currentUserId: Int? = null,
    onBack: () -> Unit,
    onCommunityClick: () -> Unit,
    onAuthorClick: () -> Unit,
    onCreateAccount: () -> Unit = {},
    onDelete: () -> Unit = {},
    modifier: Modifier = Modifier,
    api: CommunityApi = koinInject(),
) {
    var isLiked by remember { mutableStateOf(post.isLiked) }
    var likes by remember { mutableIntStateOf(post.likes) }
    var isBookmarked by remember { mutableStateOf(post.isBookmarked) }
    var isFollowing by remember { mutableStateOf(false) }
    var showComments by remember { mutableStateOf(false) }
    val comments = remember { mutableListOf<PostComment>() }
    var commentsLoaded by remember { mutableStateOf(false) }
    var commentsError by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val isOwnPost = currentUserId != null && post.authorId == currentUserId

    LaunchedEffect(post.id) {
        commentsError = false
        try {
            val detail = api.getPost(post.id)
            val apiComments = detail.comments?.map { it.toDomain() }.orEmpty()
            comments.clear()
            comments.addAll(apiComments)
        } catch (_: Exception) {
            commentsError = true
        }
        commentsLoaded = true
    }

    Column(modifier = modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
        TopBar(
            isOwnPost = isOwnPost,
            onBack = onBack,
            onDeleteRequest = { showDeleteDialog = true },
        )

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            AuthorRow(
                post = post,
                isFollowing = isFollowing,
                showFollow = !isGuest,
                onFollow = { isFollowing = !isFollowing },
                onAuthorClick = onAuthorClick,
            )
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
            if (post.hasMedia) {
                Media(post = post)
            }
            EngagementBar(
                isLiked = isLiked,
                likes = likes,
                comments = post.comments,
                isBookmarked = isBookmarked,
                isGuest = isGuest,
                onLike = {
                    val wasLiked = isLiked
                    val oldLikes = likes
                    isLiked = !wasLiked
                    likes += if (wasLiked) -1 else 1
                    scope.launch {
                        try {
                            val response = api.toggleLike(post.id)
                            isLiked = response.isLiked
                            likes = response.likes
                        } catch (_: Exception) {
                            isLiked = wasLiked
                            likes = oldLikes
                        }
                    }
                },
                onComment = { showComments = true },
                onBookmark = { isBookmarked = !isBookmarked },
            )

            if (isGuest) {
                GuestCard(onCreateAccount = onCreateAccount)
            }
        }
    }

    if (showComments && commentsLoaded) {
        PostCommentsSheet(
            postId = post.id,
            comments = comments,
            isGuest = isGuest,
            currentUserId = currentUserId,
            onDismiss = { showComments = false },
        )
    }

    if (showDeleteDialog) {
        DeletePostDialog(
            onConfirm = {
                showDeleteDialog = false
                scope.launch {
                    try {
                        api.deletePost(post.id)
                        onDelete()
                        onBack()
                    } catch (_: Exception) {
                    }
                }
            },
            onDismiss = { showDeleteDialog = false },
        )
    }
}

@Composable
private fun TopBar(
    isOwnPost: Boolean = false,
    onBack: () -> Unit,
    onDeleteRequest: () -> Unit = {},
) {
    var showMenu by remember { mutableStateOf(false) }
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
            if (isOwnPost) {
                Box {
                    IconButtonGhost(
                        icon = AppIcon.OVERFLOW,
                        label = stringResource(R.string.community_action_more),
                        onClick = { showMenu = true },
                    )
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        containerColor = AppSurfaceCard,
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Delete post",
                                    color = AppTertiary,
                                    style = AppType.label(15.sp),
                                )
                            },
                            onClick = {
                                showMenu = false
                                onDeleteRequest()
                            },
                        )
                    }
                }
            } else {
                Spacer(Modifier.size(40.dp))
            }
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
    showFollow: Boolean = true,
    onFollow: () -> Unit,
    onAuthorClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val authorInteraction = remember { MutableInteractionSource() }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier =
                Modifier
                    .pressScale(authorInteraction)
                    .weight(1f)
                    .clickable(
                        interactionSource = authorInteraction,
                        indication = null,
                        onClickLabel = post.author,
                        role = Role.Button,
                        onClick = onAuthorClick,
                    ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CommunityAvatar(start = post.avatarStart, end = post.avatarEnd, size = 44.dp)
            Column(modifier = Modifier.padding(start = 12.dp)) {
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
        }
        if (showFollow) {
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
    isGuest: Boolean = false,
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
            if (!isGuest) {
                Action(
                    icon = AppIcon.LIKE,
                    label = stringResource(if (isLiked) R.string.community_action_unlike else R.string.community_action_like),
                    filled = isLiked,
                    tint = if (isLiked) AppPrimary else AppTextSecondary,
                    value = likes.abbreviated(),
                    onClick = onLike,
                )
                Spacer(Modifier.size(22.dp))
            }
            Action(
                icon = AppIcon.COMMENT,
                label = stringResource(R.string.community_action_comment),
                value = comments.abbreviated(),
                onClick = onComment,
            )
            if (!isGuest) {
                Spacer(Modifier.size(22.dp))
                Action(
                    icon = AppIcon.SHARE,
                    label = stringResource(R.string.community_action_share),
                    onClick = {},
                )
            }
            Spacer(Modifier.weight(1f))
            if (!isGuest) {
                Action(
                    icon = AppIcon.BOOKMARK,
                    label = stringResource(if (isBookmarked) R.string.community_action_unbookmark else R.string.community_action_bookmark),
                    filled = isBookmarked,
                    tint = if (isBookmarked) AppPrimary else AppTextSecondary,
                    onClick = onBookmark,
                )
            }
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

@Composable
private fun DeletePostDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppSurfaceCard,
        title = {
            Text(
                text = "Delete post?",
                color = AppTextPrimary,
                style = AppType.headline(20.sp),
            )
        },
        text = {
            Text(
                text = "This action cannot be undone. The post and all its comments will be permanently deleted.",
                color = AppTextSecondary,
                style = AppType.body(15.sp),
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "Delete",
                    color = AppTertiary,
                    style = AppType.label(15.sp),
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    color = AppTextSecondary,
                    style = AppType.label(15.sp),
                )
            }
        },
    )
}

@Composable
private fun GuestCard(onCreateAccount: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.post_detail_guest_title),
            color = AppTextPrimary,
            style = AppType.headline(18.sp),
        )
        Text(
            text = stringResource(R.string.post_detail_guest_message),
            color = AppTextSecondary,
            style = AppType.body(14.sp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp),
        )
        Text(
            text = stringResource(R.string.post_detail_guest_action),
            color = AppOnPrimary,
            style = AppType.label(14.sp),
            modifier =
                Modifier
                    .padding(top = 16.dp)
                    .pressScale(interactionSource)
                    .clip(CircleShape)
                    .background(AppPrimary)
                    .clickable(interactionSource = interactionSource, indication = null, onClick = onCreateAccount)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
        )
    }
}
