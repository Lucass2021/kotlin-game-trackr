package com.lucasdias.gametrackr.feature.app.appshell.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lucasdias.gametrackr.core.model.Profile
import com.lucasdias.gametrackr.feature.app.appshell.ShellRoutes
import com.lucasdias.gametrackr.feature.app.appshell.popBackStackIfResumed
import com.lucasdias.gametrackr.feature.app.profile.UserProfileScreen
import com.lucasdias.gametrackr.feature.app.profile.editprofile.EditProfileScreen
import com.lucasdias.gametrackr.feature.app.profile.setup.EditSetupRoute
import com.lucasdias.gametrackr.feature.app.profile.setup.MySetupScreen
import com.lucasdias.gametrackr.feature.app.profile.setup.MySetupViewModel

internal fun NavGraphBuilder.profileGraph(
    navController: NavController,
    isGuest: Boolean,
    profile: () -> Profile?,
    mySetupViewModel: MySetupViewModel,
) {
    composable(ShellRoutes.MY_SETUP) {
        val mySetupState by mySetupViewModel.uiState.collectAsStateWithLifecycle()
        MySetupScreen(
            state = mySetupState,
            onBack = { navController.popBackStackIfResumed() },
            onAdd = { navController.navigate(ShellRoutes.editSetup()) },
            onEdit = { setup -> navController.navigate(ShellRoutes.editSetup(setup.id)) },
            onRetry = mySetupViewModel::load,
        )
    }
    composable(
        route = ShellRoutes.EDIT_SETUP_ROUTE,
        arguments =
            listOf(
                navArgument(ShellRoutes.EDIT_SETUP_ARG_ID) {
                    type = NavType.StringType
                    nullable = true
                },
            ),
    ) { backStackEntry ->
        val mySetupState by mySetupViewModel.uiState.collectAsStateWithLifecycle()
        EditSetupRoute(
            setupId = backStackEntry.arguments?.getString(ShellRoutes.EDIT_SETUP_ARG_ID)?.toIntOrNull(),
            mySetupState = mySetupState,
            onBack = { navController.popBackStackIfResumed() },
            onSaved = mySetupViewModel::onSetupSaved,
            onDeleted = mySetupViewModel::onSetupDeleted,
        )
    }
    composable(ShellRoutes.EDIT_PROFILE) {
        val current = profile() ?: return@composable
        EditProfileScreen(
            profile = current,
            onBack = { navController.popBackStackIfResumed() },
        )
    }
    composable(
        route = ShellRoutes.USER_PROFILE_ROUTE,
        arguments =
            listOf(
                navArgument(ShellRoutes.USER_PROFILE_ARG_USERNAME) {
                    type = NavType.StringType
                    defaultValue = ""
                },
            ),
    ) { backStackEntry ->
        val username = backStackEntry.arguments?.getString(ShellRoutes.USER_PROFILE_ARG_USERNAME).orEmpty()
        UserProfileScreen(
            username = username,
            isGuest = isGuest,
            onBack = { navController.popBackStackIfResumed() },
            onGameClick = { navController.navigate(ShellRoutes.gameDetail(it)) },
            onViewStats = { navController.navigate(ShellRoutes.stats("@$username")) },
        )
    }
}
