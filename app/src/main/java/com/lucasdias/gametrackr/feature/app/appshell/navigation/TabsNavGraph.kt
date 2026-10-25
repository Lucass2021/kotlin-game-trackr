package com.lucasdias.gametrackr.feature.app.appshell.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lucasdias.gametrackr.core.model.Profile
import com.lucasdias.gametrackr.feature.app.appshell.AppTab
import com.lucasdias.gametrackr.feature.app.appshell.ShellRoutes
import com.lucasdias.gametrackr.feature.app.appshell.components.TabShell
import com.lucasdias.gametrackr.feature.app.community.CommunityFeedViewModel
import com.lucasdias.gametrackr.feature.app.community.CommunityScreen
import com.lucasdias.gametrackr.feature.app.community.discover.DiscoverCommunitiesViewModel
import com.lucasdias.gametrackr.feature.app.home.HomeScreen
import com.lucasdias.gametrackr.feature.app.library.LibraryScreen
import com.lucasdias.gametrackr.feature.app.library.LibraryViewModel
import com.lucasdias.gametrackr.feature.app.profile.ProfileScreen
import com.lucasdias.gametrackr.feature.app.profile.ProfileViewModel
import com.lucasdias.gametrackr.feature.app.profile.setup.MySetupViewModel
import com.lucasdias.gametrackr.feature.app.search.SearchScope

internal class TabsViewModels(
    val discover: DiscoverCommunitiesViewModel,
    val communityFeed: CommunityFeedViewModel,
    val library: LibraryViewModel,
    val profile: ProfileViewModel,
    val mySetup: MySetupViewModel,
)

internal fun NavGraphBuilder.tabsGraph(
    navController: NavController,
    selection: CommunitySelection,
    selectedTab: () -> AppTab,
    onSelectTab: (AppTab) -> Unit,
    viewModels: TabsViewModels,
    isGuest: Boolean,
    currentUserId: Int?,
    profile: () -> Profile,
    onCreateAccount: () -> Unit,
) {
    composable(ShellRoutes.TABS) {
        TabShell(
            selected = selectedTab(),
            onSelect = onSelectTab,
            onNotifications = { navController.navigate(ShellRoutes.NOTIFICATIONS) },
            onSearch = { navController.navigate(ShellRoutes.search(SearchScope.ALL)) },
            onMenu = { navController.navigate(ShellRoutes.MENU) },
        ) { tab ->
            TabContent(
                tab = tab,
                navController = navController,
                selection = selection,
                onSelectTab = onSelectTab,
                viewModels = viewModels,
                isGuest = isGuest,
                currentUserId = currentUserId,
                profile = profile(),
                onCreateAccount = onCreateAccount,
            )
        }
    }
}

@Composable
private fun TabContent(
    tab: AppTab,
    navController: NavController,
    selection: CommunitySelection,
    onSelectTab: (AppTab) -> Unit,
    viewModels: TabsViewModels,
    isGuest: Boolean,
    currentUserId: Int?,
    profile: Profile,
    onCreateAccount: () -> Unit,
) {
    val onGameClick: (String) -> Unit = { navController.navigate(ShellRoutes.gameDetail(it)) }
    when (tab) {
        AppTab.HOME -> {
            HomeScreen(onViewAll = { navController.navigate(ShellRoutes.search(it)) }, onGameClick = onGameClick)
        }

        AppTab.LIBRARY -> {
            val libraryState by viewModels.library.uiState.collectAsStateWithLifecycle()
            LibraryScreen(
                state = libraryState,
                isGuest = isGuest,
                onFilterChange = viewModels.library::onFilterChange,
                onRetry = viewModels.library::load,
                onLoadMore = viewModels.library::loadMore,
                onToggleFavorite = viewModels.library::onToggleFavorite,
                onErrorShown = viewModels.library::onErrorShown,
                onCreateAccount = onCreateAccount,
                onBrowseGames = { navController.navigate(ShellRoutes.search(SearchScope.ALL)) },
                onGameClick = onGameClick,
            )
        }

        AppTab.COMMUNITY -> {
            CommunityScreen(
                feedViewModel = viewModels.communityFeed,
                discoverViewModel = viewModels.discover,
                isGuest = isGuest,
                onPostClick = { post -> navController.openPost(selection, post) },
                onCommunityClick = { community -> navController.openCommunity(selection, community) },
                onCreatePost = { navController.openCreateTopic(selection, null) },
                onCreateCommunity = { navController.navigate(ShellRoutes.CREATE_COMMUNITY) },
                onCreateAccount = onCreateAccount,
                currentUserId = currentUserId,
            )
        }

        AppTab.PROFILE -> {
            val profileState by viewModels.profile.uiState.collectAsStateWithLifecycle()
            val mySetupState by viewModels.mySetup.uiState.collectAsStateWithLifecycle()
            ProfileScreen(
                state = profileState,
                isGuest = isGuest,
                onRetry = viewModels.profile::load,
                onGameClick = onGameClick,
                onStatusClick = { status ->
                    viewModels.library.onFilterChange(status)
                    onSelectTab(AppTab.LIBRARY)
                },
                onEditProfile = { navController.navigate(ShellRoutes.EDIT_PROFILE) },
                onCreateAccount = onCreateAccount,
                onViewStats = { navController.navigate(ShellRoutes.stats()) },
                onViewSetup = { navController.navigate(ShellRoutes.MY_SETUP) },
                setups = mySetupState.setups,
                profile = profile,
            )
        }
    }
}
