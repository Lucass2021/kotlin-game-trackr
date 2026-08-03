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
import com.lucasdias.gametrackr.feature.app.achievements.AchievementsMockData
import com.lucasdias.gametrackr.feature.app.achievements.AchievementsScreen
import com.lucasdias.gametrackr.feature.app.achievements.GameAchievementsScreen
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
import com.lucasdias.gametrackr.feature.app.profile.Profile
import com.lucasdias.gametrackr.feature.app.profile.ProfileMockData
import com.lucasdias.gametrackr.feature.app.profile.ProfileScreen
import com.lucasdias.gametrackr.feature.app.profile.UserProfileMockData
import com.lucasdias.gametrackr.feature.app.profile.UserProfileScreen
import com.lucasdias.gametrackr.feature.app.profile.editprofile.EditProfileScreen
import com.lucasdias.gametrackr.feature.app.profilemenu.ProfileMenuScreen
import com.lucasdias.gametrackr.feature.app.search.SearchScope
import com.lucasdias.gametrackr.feature.app.search.SearchScreen
import com.lucasdias.gametrackr.feature.app.stats.StatsScreen

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
    const val STATS = "stats"
    const val STATS_ARG_OWNER = "owner"
    const val STATS_ROUTE = "$STATS?$STATS_ARG_OWNER={$STATS_ARG_OWNER}"
    const val USER_PROFILE = "userprofile"
    const val USER_PROFILE_ARG_NAME = "name"
    const val USER_PROFILE_ROUTE = "$USER_PROFILE?$USER_PROFILE_ARG_NAME={$USER_PROFILE_ARG_NAME}"
    const val EDIT_PROFILE = "editprofile"
    const val ACHIEVEMENTS = "achievements"
    const val ACHIEVEMENTS_ARG_OWNER = "owner"
    const val ACHIEVEMENTS_ROUTE = "$ACHIEVEMENTS?$ACHIEVEMENTS_ARG_OWNER={$ACHIEVEMENTS_ARG_OWNER}"
    const val GAME_ACHIEVEMENTS = "gameachievements"
    const val GAME_ACHIEVEMENTS_ARG_ID = "gameId"
    const val GAME_ACHIEVEMENTS_ROUTE = "$GAME_ACHIEVEMENTS/{$GAME_ACHIEVEMENTS_ARG_ID}"
    const val CREATE_TOPIC = "createtopic"
    const val CREATE_TOPIC_ARG_COMMUNITY = "community"
    const val CREATE_TOPIC_ROUTE = "$CREATE_TOPIC?$CREATE_TOPIC_ARG_COMMUNITY={$CREATE_TOPIC_ARG_COMMUNITY}"

    fun search(scope: SearchScope) = "$SEARCH?$SEARCH_ARG_SCOPE=${scope.name}"

    fun stats(ownerName: String = "") = "$STATS?$STATS_ARG_OWNER=${Uri.encode(ownerName)}"

    fun achievements(ownerName: String = "") = "$ACHIEVEMENTS?$ACHIEVEMENTS_ARG_OWNER=${Uri.encode(ownerName)}"

    fun gameAchievements(gameId: Long) = "$GAME_ACHIEVEMENTS/$gameId"

    fun userProfile(name: String) = "$USER_PROFILE?$USER_PROFILE_ARG_NAME=${Uri.encode(name)}"

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
    var profile by remember { mutableStateOf(ProfileMockData.profile) }

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
                onViewStats = { navController.navigate(ShellRoutes.stats()) },
                profile = profile,
                onEditProfile = { navController.navigate(ShellRoutes.EDIT_PROFILE) },
            )
        }
        composable(ShellRoutes.EDIT_PROFILE) {
            EditProfileScreen(
                profile = profile,
                onBack = { navController.popBackStackIfResumed() },
                onSave = { profile = it },
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
                onMemberClick = { member ->
                    navController.navigate(ShellRoutes.userProfile(member.author))
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
                onAuthorClick = {
                    navController.navigate(ShellRoutes.userProfile(CommunityMockData.detailPost.author))
                },
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
                onStats = { navController.navigate(ShellRoutes.stats()) },
                onEditProfile = { navController.navigate(ShellRoutes.EDIT_PROFILE) },
                onAchievements = { navController.navigate(ShellRoutes.achievements()) },
                profile = profile,
            )
        }
        composable(
            route = ShellRoutes.STATS_ROUTE,
            arguments =
                listOf(
                    navArgument(ShellRoutes.STATS_ARG_OWNER) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                ),
        ) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString(ShellRoutes.STATS_ARG_OWNER).orEmpty()
            StatsScreen(
                onBack = { navController.popBackStackIfResumed() },
                onAchievements = { navController.navigate(ShellRoutes.achievements(owner)) },
                ownerName = owner.ifBlank { null },
            )
        }
        composable(
            route = ShellRoutes.ACHIEVEMENTS_ROUTE,
            arguments =
                listOf(
                    navArgument(ShellRoutes.ACHIEVEMENTS_ARG_OWNER) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                ),
        ) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString(ShellRoutes.ACHIEVEMENTS_ARG_OWNER).orEmpty()
            AchievementsScreen(
                onBack = { navController.popBackStackIfResumed() },
                onGameClick = { navController.navigate(ShellRoutes.gameAchievements(it)) },
                ownerName = owner.ifBlank { null },
            )
        }
        composable(
            route = ShellRoutes.GAME_ACHIEVEMENTS_ROUTE,
            arguments = listOf(navArgument(ShellRoutes.GAME_ACHIEVEMENTS_ARG_ID) { type = NavType.LongType }),
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getLong(ShellRoutes.GAME_ACHIEVEMENTS_ARG_ID) ?: 0L
            GameAchievementsScreen(
                game = AchievementsMockData.byId(gameId),
                onBack = { navController.popBackStackIfResumed() },
            )
        }
        composable(
            route = ShellRoutes.USER_PROFILE_ROUTE,
            arguments =
                listOf(
                    navArgument(ShellRoutes.USER_PROFILE_ARG_NAME) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                ),
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString(ShellRoutes.USER_PROFILE_ARG_NAME).orEmpty()
            UserProfileScreen(
                user = UserProfileMockData.forName(name),
                isGuest = isGuest,
                onBack = { navController.popBackStackIfResumed() },
                onGameClick = { navController.navigate(ShellRoutes.GAME_DETAIL) },
                onViewStats = { navController.navigate(ShellRoutes.stats(name)) },
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
    onViewStats: () -> Unit,
    profile: Profile,
    onEditProfile: () -> Unit,
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
                        onEditProfile = onEditProfile,
                        onCreateAccount = onCreateAccount,
                        onViewStats = onViewStats,
                        profile = profile,
                    )
                }
            }
        }

        AppTabBar(selected = selected, onSelect = onSelect)
    }
}
