package com.lucasdias.gametrackr.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.feature.auth.register.RegisterScreen
import com.lucasdias.gametrackr.feature.auth.welcome.WelcomeScreen

object Routes {
    const val WELCOME = "welcome"
    const val REGISTER = "register"
    const val LOGIN = "login"
}

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.WELCOME) {
        composable(Routes.WELCOME) { entry ->
            WelcomeScreen(
                onCreateAccount = { navController.navigateOnce(entry, Routes.REGISTER) },
                onSignIn = { navController.navigateOnce(entry, Routes.LOGIN) }
            )
        }
        composable(Routes.REGISTER) { entry ->
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onSignIn = { navController.navigateOnce(entry, Routes.LOGIN) }
            )
        }
        composable(Routes.LOGIN) { PlaceholderScreen("Login") }
    }
}

private fun NavHostController.navigateOnce(from: NavBackStackEntry, route: String) {
    if (from.lifecycle.currentState == Lifecycle.State.RESUMED) {
        navigate(route)
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize().background(AppBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, color = AppTextPrimary)
    }
}
