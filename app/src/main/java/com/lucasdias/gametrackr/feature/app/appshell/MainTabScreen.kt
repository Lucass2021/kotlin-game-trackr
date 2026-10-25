package com.lucasdias.gametrackr.feature.app.appshell

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.lucasdias.gametrackr.core.model.User
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import com.lucasdias.gametrackr.feature.app.appshell.navigation.CommunitySelection
import com.lucasdias.gametrackr.feature.app.appshell.navigation.TabsViewModels
import com.lucasdias.gametrackr.feature.app.appshell.navigation.accountGraph
import com.lucasdias.gametrackr.feature.app.appshell.navigation.communityGraph
import com.lucasdias.gametrackr.feature.app.appshell.navigation.gamesGraph
import com.lucasdias.gametrackr.feature.app.appshell.navigation.profileGraph
import com.lucasdias.gametrackr.feature.app.appshell.navigation.tabsGraph
import com.lucasdias.gametrackr.feature.app.community.CommunityFeedViewModel
import com.lucasdias.gametrackr.feature.app.community.discover.DiscoverCommunitiesViewModel
import com.lucasdias.gametrackr.feature.app.library.LibraryViewModel
import com.lucasdias.gametrackr.feature.app.profile.ProfileViewModel
import com.lucasdias.gametrackr.feature.app.profile.setup.MySetupViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainTabScreen(
    isGuest: Boolean,
    userName: String?,
    email: String?,
    currentUserId: Int? = null,
    user: User? = null,
    onLogout: () -> Unit,
) {
    val navController = rememberNavController()
    var selectedTab by rememberSaveable { mutableStateOf(AppTab.HOME) }
    val selection = remember { CommunitySelection() }
    val discover: DiscoverCommunitiesViewModel = koinViewModel()
    val communityFeed: CommunityFeedViewModel = koinViewModel()
    val library: LibraryViewModel = koinViewModel()
    val profileViewModel: ProfileViewModel = koinViewModel()
    val mySetup: MySetupViewModel = koinViewModel()
    val viewModels =
        remember(discover, communityFeed, library, profileViewModel, mySetup) {
            TabsViewModels(discover, communityFeed, library, profileViewModel, mySetup)
        }
    var profile by remember { mutableStateOf(ProfileMockData.profile) }

    LaunchedEffect(user?.id) {
        profile = profile.applying(user)
        val userId = if (isGuest) null else user?.id
        viewModels.mySetup.loadFor(userId)
        viewModels.library.loadFor(userId)
        viewModels.profile.loadFor(userId)
    }

    NavHost(navController = navController, startDestination = ShellRoutes.TABS) {
        tabsGraph(
            navController = navController,
            selection = selection,
            selectedTab = { selectedTab },
            onSelectTab = { selectedTab = it },
            viewModels = viewModels,
            isGuest = isGuest,
            currentUserId = currentUserId,
            profile = { profile },
            onCreateAccount = onLogout,
        )
        gamesGraph(navController = navController, isGuest = isGuest)
        communityGraph(
            navController = navController,
            selection = selection,
            isGuest = isGuest,
            currentUserId = currentUserId,
            discoverViewModel = viewModels.discover,
            communityFeedViewModel = viewModels.communityFeed,
            onCreateAccount = onLogout,
        )
        profileGraph(
            navController = navController,
            isGuest = isGuest,
            profile = { profile },
            mySetupViewModel = viewModels.mySetup,
            onProfileSave = { profile = it },
        )
        accountGraph(
            navController = navController,
            isGuest = isGuest,
            userName = userName,
            email = email,
            onLogout = onLogout,
        )
    }
}
