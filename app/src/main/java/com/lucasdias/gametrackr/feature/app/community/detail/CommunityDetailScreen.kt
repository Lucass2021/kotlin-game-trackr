package com.lucasdias.gametrackr.feature.app.community.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.network.CommunityApi
import com.lucasdias.gametrackr.core.network.dto.toDomain
import com.lucasdias.gametrackr.core.network.toApiError
import com.lucasdias.gametrackr.core.pagination.InfiniteScrollEffect
import com.lucasdias.gametrackr.core.pagination.LoadingMoreIndicator
import com.lucasdias.gametrackr.core.pagination.PaginationState
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.shareText
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.Community
import com.lucasdias.gametrackr.feature.app.community.CommunityDetailTab
import com.lucasdias.gametrackr.feature.app.community.CommunityMember
import com.lucasdias.gametrackr.feature.app.community.CommunityPost
import com.lucasdias.gametrackr.feature.app.community.components.CommunityEmptyState
import com.lucasdias.gametrackr.feature.app.community.components.CommunityPostCard
import com.lucasdias.gametrackr.feature.app.community.components.ConfirmDialog
import com.lucasdias.gametrackr.feature.app.community.components.CreatePostButton
import com.lucasdias.gametrackr.feature.app.community.components.JoinButton
import com.lucasdias.gametrackr.feature.auth.toMessage
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject

@Composable
fun CommunityDetailScreen(
    community: Community,
    isGuest: Boolean = false,
    currentUserId: Int? = null,
    onBack: () -> Unit,
    onPostClick: (CommunityPost) -> Unit,
    onCreatePost: () -> Unit,
    onMemberClick: (CommunityMember) -> Unit,
    onDeleted: () -> Unit = {},
    modifier: Modifier = Modifier,
    api: CommunityApi = koinInject(),
    json: Json = koinInject(),
) {
    var isJoined by remember { mutableStateOf(community.isJoined) }
    val isOwner = currentUserId != null && community.authorId == currentUserId
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showLeaveDialog by remember { mutableStateOf(false) }
    var deleteError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    var tab by remember { mutableStateOf(CommunityDetailTab.POSTS) }
    val postsPagination = remember { PaginationState<CommunityPost>() }
    val posts = postsPagination.items
    val members = remember { mutableStateListOf<CommunityMember>() }
    var postsError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    suspend fun loadPosts(reset: Boolean = true) {
        if (reset) {
            postsError = false
            postsPagination.reset()
        } else {
            if (!postsPagination.canLoadMore) return
            postsPagination.setLoading(true)
        }

        val nextPage = postsPagination.currentPage + 1

        try {
            val response = api.getPosts(communityId = community.id, perPage = 30, page = nextPage)
            postsPagination.append(response, response.data.map { it.toDomain() })
        } catch (_: Exception) {
            if (reset) postsError = true
        }
        postsPagination.setLoading(false)
    }

    suspend fun loadMembers() {
        try {
            val detail = api.getCommunity(community.id)
            val apiMembers = detail.members?.map { it.toDomain() }.orEmpty()
            members.clear()
            members.addAll(apiMembers)
        } catch (_: Exception) {
        }
    }

    LaunchedEffect(community.id) {
        loadPosts()
        loadMembers()
    }

    Box(modifier = modifier.fillMaxSize().background(AppBackground)) {
        val listState = rememberLazyListState()

        InfiniteScrollEffect(
            listState = listState,
            canLoadMore = postsPagination.canLoadMore,
            onLoadMore = { scope.launch { loadPosts(reset = false) } },
        )

        LazyColumn(state = listState, contentPadding = PaddingValues(bottom = 96.dp)) {
            item { CommunityDetailHeader(community = community) }

            item {
                Text(
                    text = community.description,
                    color = AppTextSecondary,
                    style = AppType.body(15.sp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 0.dp),
                )
                Spacer(Modifier.height(20.dp))
                val context = LocalContext.current
                ActionRow(
                    isJoined = isJoined,
                    isOwner = isOwner,
                    isGuest = isGuest,
                    onJoin = {
                        if (isJoined) {
                            showLeaveDialog = true
                        } else {
                            isJoined = true
                            scope.launch {
                                try {
                                    api.joinCommunity(community.id)
                                } catch (_: Exception) {
                                    isJoined = false
                                }
                            }
                        }
                    },
                    onShare = { context.shareText("Join the ${community.name} community on GameTrackr!") },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                Spacer(Modifier.height(20.dp))
                CommunityStatsBar(community = community, modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(Modifier.height(20.dp))
                CommunityDetailTabs(selection = tab, onSelect = { tab = it })
                Spacer(Modifier.height(20.dp))
            }

            when (tab) {
                CommunityDetailTab.POSTS -> {
                    if (postsError && posts.isEmpty()) {
                        item {
                            CommunityEmptyState(
                                icon = AppIcon.INFO,
                                title = "Couldn't load posts",
                                message = "Check your connection\nand try again.",
                                actionTitle = "Try again",
                                onAction = {
                                    scope.launch {
                                        loadPosts()
                                        loadMembers()
                                    }
                                },
                            )
                        }
                    } else if (posts.isEmpty()) {
                        item {
                            CommunityEmptyState(
                                icon = AppIcon.COMMUNITY,
                                title = "No posts yet",
                                message =
                                    if (isJoined) {
                                        "Start the first discussion in this community."
                                    } else {
                                        "Join this community to start posting."
                                    },
                                actionTitle = if (isJoined) "Create post" else null,
                                onAction = if (isJoined) onCreatePost else null,
                            )
                        }
                    } else {
                        itemsIndexed(posts, key = { _, post -> post.id }) { index, post ->
                            CommunityPostCard(
                                post = post,
                                isGuest = isGuest,
                                showsCommunityName = false,
                                onSelect = { onPostClick(post) },
                                onLike = {
                                    val wasLiked = post.isLiked
                                    posts[index] =
                                        post.copy(
                                            isLiked = !wasLiked,
                                            likes = post.likes + if (wasLiked) -1 else 1,
                                        )
                                    scope.launch {
                                        try {
                                            val response = api.toggleLike(post.id)
                                            val i = posts.indexOfFirst { it.id == post.id }
                                            if (i >= 0) {
                                                posts[i] =
                                                    posts[i].copy(
                                                        isLiked = response.isLiked,
                                                        likes = response.likes,
                                                    )
                                            }
                                        } catch (_: Exception) {
                                            val i = posts.indexOfFirst { it.id == post.id }
                                            if (i >= 0) {
                                                posts[i] =
                                                    posts[i].copy(
                                                        isLiked = wasLiked,
                                                        likes = post.likes,
                                                    )
                                            }
                                        }
                                    }
                                },
                                onComment = { onPostClick(post) },
                                onBookmark = {
                                    posts[index] = post.copy(isBookmarked = !post.isBookmarked)
                                },
                                modifier = Modifier.padding(horizontal = 20.dp),
                            )
                            Spacer(Modifier.height(16.dp))
                        }

                        if (postsPagination.isLoadingMore) {
                            item { LoadingMoreIndicator() }
                        }
                    }
                }

                CommunityDetailTab.ABOUT -> {
                    item {
                        CommunityAboutSection(
                            community = community,
                            isOwner = isOwner,
                            onDelete = { showDeleteDialog = true },
                        )
                    }
                }

                CommunityDetailTab.MEMBERS -> {
                    item {
                        CommunityMembersSection(
                            members = members,
                            onMemberClick = onMemberClick,
                        )
                    }
                }
            }
        }

        BackCircle(onClick = onBack, modifier = Modifier.align(Alignment.TopStart))

        if (isOwner) {
            OwnerMenu(
                onDeleteRequest = { showDeleteDialog = true },
                modifier = Modifier.align(Alignment.TopEnd),
            )
        }

        if (tab == CommunityDetailTab.POSTS && isJoined && !isGuest) {
            CreatePostButton(onClick = onCreatePost, modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp))
        }
    }

    if (showLeaveDialog) {
        ConfirmDialog(
            title = stringResource(R.string.community_leave_title),
            message = stringResource(R.string.community_leave_message),
            confirmLabel = stringResource(R.string.community_leave_confirm),
            dismissLabel = stringResource(R.string.community_leave_cancel),
            onConfirm = {
                showLeaveDialog = false
                isJoined = false
                scope.launch {
                    try {
                        api.leaveCommunity(community.id)
                    } catch (_: Exception) {
                        isJoined = true
                    }
                }
            },
            onDismiss = { showLeaveDialog = false },
        )
    }

    if (showDeleteDialog) {
        ConfirmDialog(
            title = stringResource(R.string.community_delete_title),
            message = stringResource(R.string.community_delete_message),
            confirmLabel = stringResource(R.string.community_delete_confirm),
            dismissLabel = stringResource(R.string.community_delete_cancel),
            onConfirm = {
                showDeleteDialog = false
                scope.launch {
                    try {
                        api.deleteCommunity(community.id)
                        onDeleted()
                        onBack()
                    } catch (throwable: Exception) {
                        deleteError =
                            when (val error = throwable.toApiError(json)) {
                                is ApiError.Forbidden -> context.getString(R.string.community_delete_forbidden)
                                else -> error.toMessage(context)
                            }
                    }
                }
            },
            onDismiss = { showDeleteDialog = false },
        )
    }

    deleteError?.let { message ->
        ConfirmDialog(
            title = stringResource(R.string.community_delete_failed_title),
            message = message,
            confirmLabel = stringResource(R.string.community_delete_failed_confirm),
            onConfirm = { deleteError = null },
            onDismiss = { deleteError = null },
        )
    }
}

@Composable
private fun OwnerMenu(
    onDeleteRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        FloatingCircle(
            icon = AppIcon.OVERFLOW,
            label = stringResource(R.string.community_action_more),
            onClick = { showMenu = true },
        )
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            containerColor = AppSurfaceCard,
            modifier = Modifier.padding(end = 16.dp),
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.community_delete_action),
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
}

@Composable
private fun ActionRow(
    isJoined: Boolean,
    isOwner: Boolean = false,
    isGuest: Boolean = false,
    onJoin: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isGuest) return
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        JoinButton(
            isJoined = isJoined,
            onClick = onJoin,
            expanded = true,
            modifier = Modifier.weight(1f),
            enabled = !isOwner,
        )
        CircleIconButton(
            icon = AppIcon.NOTIFICATIONS,
            label = stringResource(R.string.community_action_notifications),
        )
        CircleIconButton(
            icon = AppIcon.SHARE,
            label = stringResource(R.string.community_action_share_community),
            onClick = onShare,
        )
    }
}

@Composable
private fun CircleIconButton(
    icon: AppIcon,
    label: String,
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .size(50.dp)
                .clip(CircleShape)
                .border(1.dp, AppOutline, CircleShape)
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
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun BackCircle(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingCircle(
        icon = AppIcon.BACK,
        label = stringResource(R.string.community_action_back),
        onClick = onClick,
        modifier = modifier,
    )
}

@Composable
private fun FloatingCircle(
    icon: AppIcon,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier =
            modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .pressScale(interactionSource)
                .size(40.dp)
                .clip(CircleShape)
                .background(AppBackground.copy(alpha = 0.55f))
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
            modifier = Modifier.size(20.dp),
        )
    }
}
