package com.lucasdias.gametrackr.feature.app.appshell.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.SuccessScreen
import com.lucasdias.gametrackr.feature.app.about.AboutScreen
import com.lucasdias.gametrackr.feature.app.appshell.ShellRoutes
import com.lucasdias.gametrackr.feature.app.appshell.popBackStackIfResumed
import com.lucasdias.gametrackr.feature.app.help.HelpScreen
import com.lucasdias.gametrackr.feature.app.notifications.NotificationsScreen
import com.lucasdias.gametrackr.feature.app.profilemenu.ProfileMenuActions
import com.lucasdias.gametrackr.feature.app.profilemenu.ProfileMenuItem
import com.lucasdias.gametrackr.feature.app.profilemenu.ProfileMenuScreen
import com.lucasdias.gametrackr.feature.app.profilemenu.ProfileMenuUiState
import com.lucasdias.gametrackr.feature.app.settings.SettingsScreen
import com.lucasdias.gametrackr.feature.app.settings.changepassword.ChangePasswordScreen

internal fun NavGraphBuilder.accountGraph(
    navController: NavController,
    isGuest: Boolean,
    userName: String?,
    email: String?,
    onLogout: () -> Unit,
) {
    composable(ShellRoutes.NOTIFICATIONS) {
        NotificationsScreen(
            isGuest = isGuest,
            onBack = { navController.popBackStackIfResumed() },
            onCreateAccount = onLogout,
        )
    }
    composable(ShellRoutes.MENU) {
        ProfileMenuScreen(
            state = ProfileMenuUiState.from(isGuest = isGuest, userName = userName, email = email),
            actions =
                ProfileMenuActions(
                    onBack = { navController.popBackStackIfResumed() },
                    onItemSelect = { item -> navController.navigate(item.route) },
                    onLogout = onLogout,
                ),
        )
    }
    composable(ShellRoutes.SETTINGS) {
        SettingsScreen(
            onBack = { navController.popBackStackIfResumed() },
            onChangePassword = { navController.navigate(ShellRoutes.CHANGE_PASSWORD) },
            onLogout = onLogout,
        )
    }
    composable(ShellRoutes.CHANGE_PASSWORD) {
        ChangePasswordScreen(
            onBack = { navController.popBackStackIfResumed() },
            onSuccess = {
                navController.navigate(ShellRoutes.CHANGE_PASSWORD_SUCCESS) {
                    popUpTo(ShellRoutes.SETTINGS)
                }
            },
        )
    }
    composable(ShellRoutes.CHANGE_PASSWORD_SUCCESS) {
        SuccessScreen(
            title = stringResource(R.string.change_password_success_title),
            subtitle = stringResource(R.string.change_password_success_subtitle),
            buttonText = stringResource(R.string.change_password_success_button),
            onPrimary = { navController.popBackStack(ShellRoutes.SETTINGS, inclusive = false) },
        )
    }
    composable(ShellRoutes.ABOUT) {
        AboutScreen(onBack = { navController.popBackStackIfResumed() })
    }
    composable(ShellRoutes.HELP) {
        HelpScreen(onBack = { navController.popBackStackIfResumed() })
    }
}

private val ProfileMenuItem.route: String
    get() =
        when (this) {
            ProfileMenuItem.EDIT_PROFILE -> ShellRoutes.EDIT_PROFILE
            ProfileMenuItem.STATS -> ShellRoutes.stats()
            ProfileMenuItem.ACHIEVEMENTS -> ShellRoutes.achievements()
            ProfileMenuItem.SETTINGS -> ShellRoutes.SETTINGS
            ProfileMenuItem.HELP -> ShellRoutes.HELP
            ProfileMenuItem.ABOUT -> ShellRoutes.ABOUT
        }
