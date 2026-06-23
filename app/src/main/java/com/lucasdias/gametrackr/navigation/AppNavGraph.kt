package com.lucasdias.gametrackr.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.feature.auth.welcome.WelcomeScreen

object Routes {
    const val WELCOME = "welcome"
    const val SIGN_IN = "sign_in"
    const val CREATE_ACCOUNT = "create_account"
}

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.WELCOME) {
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onCreateAccount = { navController.navigate(Routes.CREATE_ACCOUNT) },
                onSignIn = { navController.navigate(Routes.SIGN_IN) }
            )
        }
        composable(Routes.SIGN_IN) { PlaceholderScreen("Sign in") }
        composable(Routes.CREATE_ACCOUNT) { PlaceholderScreen("Create account") }
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
