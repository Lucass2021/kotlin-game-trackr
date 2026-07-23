package com.lucasdias.gametrackr.feature.app.appshell

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.feature.app.appshell.components.AppHeader
import com.lucasdias.gametrackr.feature.app.appshell.components.AppTabBar
import com.lucasdias.gametrackr.feature.app.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.CommunityPost
import com.lucasdias.gametrackr.feature.app.community.CommunityScreen
import com.lucasdias.gametrackr.feature.app.community.createtopic.CreateTopicScreen
import com.lucasdias.gametrackr.feature.app.community.detail.CommunityDetailScreen
import com.lucasdias.gametrackr.feature.app.community.postdetail.PostDetailScreen
import com.lucasdias.gametrackr.feature.app.gamedetail.GameDetailScreen
import com.lucasdias.gametrackr.feature.app.home.HomeScreen
import com.lucasdias.gametrackr.feature.app.library.LibraryScreen
import com.lucasdias.gametrackr.feature.app.library.LibraryStatus
import com.lucasdias.gametrackr.feature.app.notifications.NotificationsScreen
import com.lucasdias.gametrackr.feature.app.profile.ProfileScreen
import com.lucasdias.gametrackr.feature.app.profilemenu.ProfileMenuScreen
import com.lucasdias.gametrackr.feature.app.search.SearchScope
import com.lucasdias.gametrackr.feature.app.search.SearchScreen

private object ShellRoutes {
    const val TABS = "tabs"
    const val SEARCH = "search"
    const val SEARCH_ARG_SCOPE = "scope"
    const val SEARCH_ROUTE = "$SEARCH?$SEARCH_ARG_SCOPE={$SEARCH_ARG_SCOPE}"
    const val NOTIFICATIONS = "notifications"
    const val MENU = "menu"
    const val GAME_DETAIL = "gamedetail"
    const val COMMUNITY_DETAIL = "communitydetail"
    const val POST_DETAIL = "postdetail"
    const val CREATE_TOPIC = "createtopic"
    const val CREATE_TOPIC_ARG_COMMUNITY = "community"
    const val CREATE_TOPIC_ROUTE = "$CREATE_TOPIC?$CREATE_TOPIC_ARG_COMMUNITY={$CREATE_TOPIC_ARG_COMMUNITY}"

    fun search(scope: SearchScope) = "$SEARCH?$SEARCH_ARG_SCOPE=${scope.name}"

    fun createTopic(communityName: String = "") = "$CREATE_TOPIC?$CREATE_TOPIC_ARG_COMMUNITY=${Uri.encode(communityName)}"
}

private fun NavController.popBackStackIfResumed() {
    if (currentBackStackEntry?.lifecycle?.currentState?.isAtLeast(Lifecycle.State.RESUMED) == true) {
        popBackStack()
    }
}

@Composable
fun MainTabScreen(
    isGuest: Boolean,
    userName: String?,
    email: String?,
    onLogout: () -> Unit,
) {
    val navController = rememberNavController()
    var selectedTab by rememberSaveable { mutableStateOf(AppTab.HOME) }
    val feed = remember { CommunityMockData.feed.toMutableStateList() }
    val communityPosts = remember { CommunityMockData.communityPosts.toMutableStateList() }
    var libraryFilter by rememberSaveable { mutableStateOf<LibraryStatus?>(null) }

    NavHost(navController = navController, startDestination = ShellRoutes.TABS) {
        composable(ShellRoutes.TABS) {
            TabShell(
                isGuest = isGuest,
                selected = selectedTab,
                onSelect = { selectedTab = it },
                feed = feed,
                libraryFilter = libraryFilter,
                onLibraryFilterChange = { libraryFilter = it },
                onStatusClick = { status ->
                    libraryFilter = status
                    selectedTab = AppTab.LIBRARY
                },
                onCreateAccount = onLogout,
                onNotifications = { navController.navigate(ShellRoutes.NOTIFICATIONS) },
                onSearch = { navController.navigate(ShellRoutes.search(SearchScope.ALL)) },
                onViewAll = { navController.navigate(ShellRoutes.search(it)) },
                onMenu = { navController.navigate(ShellRoutes.MENU) },
                onGameClick = { navController.navigate(ShellRoutes.GAME_DETAIL) },
                onPostClick = { navController.navigate(ShellRoutes.POST_DETAIL) },
                onCommunityClick = { navController.navigate(ShellRoutes.COMMUNITY_DETAIL) },
                onCreatePost = { navController.navigate(ShellRoutes.createTopic()) },
            )
        }
        composable(
            route = ShellRoutes.SEARCH_ROUTE,
            arguments =
                listOf(
                    navArgument(ShellRoutes.SEARCH_ARG_SCOPE) {
                        type = NavType.StringType
                        defaultValue = SearchScope.ALL.name
                    },
                ),
        ) { backStackEntry ->
            val scope =
                backStackEntry.arguments
                    ?.getString(ShellRoutes.SEARCH_ARG_SCOPE)
                    ?.let { runCatching { SearchScope.valueOf(it) }.getOrNull() }
                    ?: SearchScope.ALL
            SearchScreen(
                onBack = { navController.popBackStackIfResumed() },
                onExploreCommunity = {
                    selectedTab = AppTab.COMMUNITY
                    navController.popBackStackIfResumed()
                },
                onGameClick = { navController.navigate(ShellRoutes.GAME_DETAIL) },
                scope = scope,
            )
        }
        composable(ShellRoutes.GAME_DETAIL) {
            GameDetailScreen(
                onBack = { navController.popBackStackIfResumed() },
                onExploreCommunity = {
                    selectedTab = AppTab.COMMUNITY
                    navController.popBackStack(ShellRoutes.TABS, inclusive = false)
                },
            )
        }
        composable(ShellRoutes.COMMUNITY_DETAIL) {
            CommunityDetailScreen(
                community = CommunityMockData.detailCommunity,
                posts = communityPosts,
                onBack = { navController.popBackStackIfResumed() },
                onPostClick = { navController.navigate(ShellRoutes.POST_DETAIL) },
                onCreatePost = {
                    navController.navigate(ShellRoutes.createTopic(CommunityMockData.detailCommunity.name))
                },
            )
        }
        composable(
            route = ShellRoutes.CREATE_TOPIC_ROUTE,
            arguments =
                listOf(
                    navArgument(ShellRoutes.CREATE_TOPIC_ARG_COMMUNITY) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                ),
        ) { backStackEntry ->
            val communityName =
                backStackEntry.arguments?.getString(ShellRoutes.CREATE_TOPIC_ARG_COMMUNITY).orEmpty()
            CreateTopicScreen(
                isGuest = isGuest,
                communityName = communityName,
                onBack = { navController.popBackStackIfResumed() },
                onCreateAccount = onLogout,
                onPost = { post ->
                    if (communityName.isBlank()) feed.add(0, post) else communityPosts.add(0, post)
                },
            )
        }
        composable(ShellRoutes.POST_DETAIL) {
            PostDetailScreen(
                post = CommunityMockData.detailPost,
                onBack = { navController.popBackStackIfResumed() },
                onCommunityClick = { navController.navigate(ShellRoutes.COMMUNITY_DETAIL) },
            )
        }
        composable(ShellRoutes.NOTIFICATIONS) {
            NotificationsScreen(
                isGuest = isGuest,
                onBack = { navController.popBackStackIfResumed() },
                onCreateAccount = onLogout,
            )
        }
        composable(ShellRoutes.MENU) {
            ProfileMenuScreen(
                isGuest = isGuest,
                userName = userName,
                email = email,
                onBack = { navController.popBackStackIfResumed() },
                onLogout = onLogout,
            )
        }
    }
}

@Composable
private fun TabShell(
    isGuest: Boolean,
    selected: AppTab,
    onSelect: (AppTab) -> Unit,
    feed: SnapshotStateList<CommunityPost>,
    libraryFilter: LibraryStatus?,
    onLibraryFilterChange: (LibraryStatus?) -> Unit,
    onNotifications: () -> Unit,
    onSearch: () -> Unit,
    onViewAll: (SearchScope) -> Unit,
    onMenu: () -> Unit,
    onGameClick: () -> Unit,
    onPostClick: () -> Unit,
    onCommunityClick: () -> Unit,
    onCreatePost: () -> Unit,
    onStatusClick: (LibraryStatus) -> Unit,
    onCreateAccount: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        AppHeader(
            onNotifications = onNotifications,
            onSearch = onSearch,
            onMenu = onMenu,
        )

        Box(modifier = Modifier.weight(1f).fillMaxSize()) {
            when (selected) {
                AppTab.HOME -> {
                    HomeScreen(onViewAll = onViewAll, onGameClick = onGameClick)
                }

                AppTab.LIBRARY -> {
                    LibraryScreen(
                        filter = libraryFilter,
                        onFilterChange = onLibraryFilterChange,
                        onBrowseGames = onSearch,
                        onGameClick = onGameClick,
                    )
                }

                AppTab.COMMUNITY -> {
                    CommunityScreen(
                        feed = feed,
                        onPostClick = onPostClick,
                        onCommunityClick = onCommunityClick,
                        onCreatePost = onCreatePost,
                    )
                }

                AppTab.PROFILE -> {
                    ProfileScreen(
                        isGuest = isGuest,
                        onGameClick = onGameClick,
                        onStatusClick = onStatusClick,
                        onEditProfile = onMenu,
                        onCreateAccount = onCreateAccount,
                    )
                }
            }
        }

        AppTabBar(selected = selected, onSelect = onSelect)
    }
}
