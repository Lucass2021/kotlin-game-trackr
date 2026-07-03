package com.lucasdias.gametrackr.feature.app.appshell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.feature.app.appshell.components.AppHeader
import com.lucasdias.gametrackr.feature.app.appshell.components.AppTabBar
import com.lucasdias.gametrackr.feature.app.community.CommunityScreen
import com.lucasdias.gametrackr.feature.app.home.HomeScreen
import com.lucasdias.gametrackr.feature.app.library.LibraryScreen
import com.lucasdias.gametrackr.feature.app.notifications.NotificationsScreen
import com.lucasdias.gametrackr.feature.app.profile.ProfileScreen
import com.lucasdias.gametrackr.feature.app.profilemenu.ProfileMenuScreen
import com.lucasdias.gametrackr.feature.app.search.SearchScreen

private object ShellRoutes {
    const val TABS = "tabs"
    const val SEARCH = "search"
    const val NOTIFICATIONS = "notifications"
    const val MENU = "menu"
}

@Composable
fun MainTabScreen(
    isGuest: Boolean,
    userName: String?,
    email: String?,
    onLogout: () -> Unit,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ShellRoutes.TABS) {
        composable(ShellRoutes.TABS) {
            TabShell(
                isGuest = isGuest,
                userName = userName,
                onNotifications = { navController.navigate(ShellRoutes.NOTIFICATIONS) },
                onSearch = { navController.navigate(ShellRoutes.SEARCH) },
                onMenu = { navController.navigate(ShellRoutes.MENU) },
            )
        }
        composable(ShellRoutes.SEARCH) {
            SearchScreen(onBack = { navController.popBackStack() })
        }
        composable(ShellRoutes.NOTIFICATIONS) {
            NotificationsScreen(onBack = { navController.popBackStack() })
        }
        composable(ShellRoutes.MENU) {
            ProfileMenuScreen(
                isGuest = isGuest,
                userName = userName,
                email = email,
                onBack = { navController.popBackStack() },
                onLogout = onLogout,
            )
        }
    }
}

@Composable
private fun TabShell(
    isGuest: Boolean,
    userName: String?,
    onNotifications: () -> Unit,
    onSearch: () -> Unit,
    onMenu: () -> Unit,
) {
    var selected by rememberSaveable { mutableStateOf(AppTab.HOME) }

    Column(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        AppHeader(
            onNotifications = onNotifications,
            onSearch = onSearch,
            onMenu = onMenu,
        )

        Box(modifier = Modifier.weight(1f).fillMaxSize()) {
            when (selected) {
                AppTab.HOME -> HomeScreen(isGuest = isGuest, userName = userName)
                AppTab.LIBRARY -> LibraryScreen()
                AppTab.COMMUNITY -> CommunityScreen()
                AppTab.PROFILE -> ProfileScreen(isGuest = isGuest, userName = userName)
            }
        }

        AppTabBar(selected = selected, onSelect = { selected = it })
    }
}
