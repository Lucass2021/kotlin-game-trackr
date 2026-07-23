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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.Community
import com.lucasdias.gametrackr.feature.app.community.CommunityDetailTab
import com.lucasdias.gametrackr.feature.app.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.CommunityPost
import com.lucasdias.gametrackr.feature.app.community.components.CommunityEmptyState
import com.lucasdias.gametrackr.feature.app.community.components.CommunityPostCard
import com.lucasdias.gametrackr.feature.app.community.components.CreatePostButton
import com.lucasdias.gametrackr.feature.app.community.components.JoinButton

@Composable
fun CommunityDetailScreen(
    community: Community,
    posts: SnapshotStateList<CommunityPost>,
    onBack: () -> Unit,
    onPostClick: () -> Unit,
    onCreatePost: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isJoined by remember { mutableStateOf(community.isJoined) }
    var tab by remember { mutableStateOf(CommunityDetailTab.POSTS) }

    Box(modifier = modifier.fillMaxSize().background(AppBackground)) {
        LazyColumn(contentPadding = PaddingValues(bottom = 96.dp)) {
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
                ActionRow(
                    isJoined = isJoined,
                    onJoin = { isJoined = !isJoined },
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
                    if (posts.isEmpty()) {
                        item {
                            CommunityEmptyState(
                                icon = AppIcon.COMMUNITY,
                                title = "No posts yet",
                                message = "Start the first discussion in this community.",
                                actionTitle = "Create post",
                                onAction = onCreatePost,
                            )
                        }
                    } else {
                        itemsIndexed(posts, key = { _, post -> post.id }) { index, post ->
                            CommunityPostCard(
                                post = post,
                                showsCommunityName = false,
                                onSelect = onPostClick,
                                onLike = {
                                    posts[index] = post.copy(isLiked = !post.isLiked, likes = post.likes + if (post.isLiked) -1 else 1)
                                },
                                onComment = onPostClick,
                                onBookmark = { posts[index] = post.copy(isBookmarked = !post.isBookmarked) },
                                modifier = Modifier.padding(horizontal = 20.dp),
                            )
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }

                CommunityDetailTab.ABOUT -> {
                    item { CommunityAboutSection(community = community) }
                }

                CommunityDetailTab.MEMBERS -> {
                    item { CommunityMembersSection() }
                }
            }
        }

        BackCircle(icon = AppIcon.BACK, onClick = onBack, modifier = Modifier.align(Alignment.TopStart))
        BackCircle(icon = AppIcon.OVERFLOW, onClick = {}, modifier = Modifier.align(Alignment.TopEnd))

        if (tab == CommunityDetailTab.POSTS && posts.isNotEmpty()) {
            CreatePostButton(onClick = onCreatePost, modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp))
        }
    }
}

@Composable
private fun ActionRow(
    isJoined: Boolean,
    onJoin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        JoinButton(isJoined = isJoined, onClick = onJoin, expanded = true, modifier = Modifier.weight(1f))
        CircleIconButton(
            icon = AppIcon.NOTIFICATIONS,
            label = stringResource(R.string.community_action_notifications),
        )
        CircleIconButton(
            icon = AppIcon.SHARE,
            label = stringResource(R.string.community_action_share_community),
        )
    }
}

@Composable
private fun CircleIconButton(
    icon: AppIcon,
    label: String,
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
                    onClick = {},
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
    icon: AppIcon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val label = stringResource(R.string.community_action_back)
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
