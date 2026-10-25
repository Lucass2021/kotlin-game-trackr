package com.lucasdias.gametrackr.feature.app.appshell.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lucasdias.gametrackr.feature.app.achievements.AchievementsScreen
import com.lucasdias.gametrackr.feature.app.achievements.GameAchievementsScreen
import com.lucasdias.gametrackr.feature.app.appshell.ShellRoutes
import com.lucasdias.gametrackr.feature.app.appshell.popBackStackIfResumed
import com.lucasdias.gametrackr.feature.app.gamedetail.GameDetailScreen
import com.lucasdias.gametrackr.feature.app.search.SearchScope
import com.lucasdias.gametrackr.feature.app.search.SearchScreen
import com.lucasdias.gametrackr.feature.app.stats.StatsScreen

internal fun NavGraphBuilder.gamesGraph(
    navController: NavController,
    isGuest: Boolean,
) {
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
            onGameClick = { navController.navigate(ShellRoutes.gameDetail(it)) },
            scope = scope,
        )
    }
    composable(
        route = ShellRoutes.GAME_DETAIL_ROUTE,
        arguments =
            listOf(
                navArgument(ShellRoutes.GAME_DETAIL_ARG_SLUG) {
                    type = NavType.StringType
                    defaultValue = ""
                },
            ),
    ) { backStackEntry ->
        GameDetailScreen(
            onBack = { navController.popBackStackIfResumed() },
            isGuest = isGuest,
            slug = backStackEntry.arguments?.getString(ShellRoutes.GAME_DETAIL_ARG_SLUG).orEmpty(),
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
            gameId = gameId,
            onBack = { navController.popBackStackIfResumed() },
        )
    }
}
