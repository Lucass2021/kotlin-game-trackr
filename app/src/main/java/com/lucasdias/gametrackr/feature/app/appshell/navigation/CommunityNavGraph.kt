package com.lucasdias.gametrackr.feature.app.appshell.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.feature.app.appshell.ShellRoutes
import com.lucasdias.gametrackr.feature.app.appshell.popBackStackIfResumed
import com.lucasdias.gametrackr.feature.app.community.CommunityFeedViewModel
import com.lucasdias.gametrackr.feature.app.community.communitydetail.CommunityDetailScreen
import com.lucasdias.gametrackr.feature.app.community.createcommunity.CreateCommunityScreen
import com.lucasdias.gametrackr.feature.app.community.createtopic.CreateTopicScreen
import com.lucasdias.gametrackr.feature.app.community.discover.DiscoverCommunitiesViewModel
import com.lucasdias.gametrackr.feature.app.community.postdetail.PostDetailScreen

@Stable
internal class CommunitySelection {
    var community by mutableStateOf<Community?>(null)
    var post by mutableStateOf<CommunityPost?>(null)
    var createTopicCommunity by mutableStateOf<Community?>(null)
}

internal fun NavController.openCommunity(
    selection: CommunitySelection,
    community: Community,
) {
    selection.community = community
    navigate(ShellRoutes.COMMUNITY_DETAIL)
}

internal fun NavController.openPost(
    selection: CommunitySelection,
    post: CommunityPost,
) {
    selection.post = post
    navigate(ShellRoutes.POST_DETAIL)
}

internal fun NavController.openCreateTopic(
    selection: CommunitySelection,
    community: Community?,
) {
    selection.createTopicCommunity = community
    navigate(ShellRoutes.CREATE_TOPIC)
}

internal fun NavGraphBuilder.communityGraph(
    navController: NavController,
    selection: CommunitySelection,
    isGuest: Boolean,
    currentUserId: Int?,
    discoverViewModel: DiscoverCommunitiesViewModel,
    communityFeedViewModel: CommunityFeedViewModel,
    onCreateAccount: () -> Unit,
) {
    composable(ShellRoutes.CREATE_COMMUNITY) {
        CreateCommunityScreen(
            onBack = { navController.popBackStackIfResumed() },
            onCreated = { community ->
                discoverViewModel.onCommunityCreated(community)
                selection.community = community
                navController.navigate(ShellRoutes.COMMUNITY_DETAIL) {
                    popUpTo(ShellRoutes.CREATE_COMMUNITY) { inclusive = true }
                }
            },
        )
    }
    composable(ShellRoutes.COMMUNITY_DETAIL) {
        val community = selection.community ?: return@composable
        CommunityDetailScreen(
            community = community,
            isGuest = isGuest,
            currentUserId = currentUserId,
            onBack = { navController.popBackStackIfResumed() },
            onDeleted = {
                discoverViewModel.onCommunityRemoved(community.id)
                communityFeedViewModel.onCommunityRemoved(community.id)
            },
            onPostClick = { post -> navController.openPost(selection, post) },
            onCreatePost = { navController.openCreateTopic(selection, community) },
            onMemberClick = { member -> member.username?.let { navController.navigate(ShellRoutes.userProfile(it)) } },
        )
    }
    composable(ShellRoutes.CREATE_TOPIC) {
        CreateTopicScreen(
            isGuest = isGuest,
            community = selection.createTopicCommunity,
            onBack = { navController.popBackStackIfResumed() },
            onCreateAccount = onCreateAccount,
        )
    }
    composable(ShellRoutes.POST_DETAIL) {
        val post = selection.post ?: return@composable
        PostDetailScreen(
            post = post,
            isGuest = isGuest,
            currentUserId = currentUserId,
            onBack = { navController.popBackStackIfResumed() },
            onCommunityClick = { community -> navController.openCommunity(selection, community) },
            onAuthorClick = { post.authorUsername?.let { navController.navigate(ShellRoutes.userProfile(it)) } },
            onCreateAccount = onCreateAccount,
            onDelete = { communityFeedViewModel.onPostDeleted(post.id) },
        )
    }
}
