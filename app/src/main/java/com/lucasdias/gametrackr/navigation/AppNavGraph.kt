package com.lucasdias.gametrackr.navigation

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucasdias.gametrackr.core.auth.AuthStatus
import com.lucasdias.gametrackr.feature.auth.AuthViewModel
import com.lucasdias.gametrackr.feature.auth.login.LoginScreen
import com.lucasdias.gametrackr.feature.auth.register.RegisterScreen
import com.lucasdias.gametrackr.feature.auth.welcome.WelcomeScreen
import com.lucasdias.gametrackr.feature.home.HomePlaceholderScreen
import com.lucasdias.gametrackr.feature.splash.SplashScreen
import org.koin.androidx.compose.koinViewModel

object Routes {
    const val WELCOME = "welcome"
    const val REGISTER = "register"
    const val LOGIN = "login"
}

@Composable
fun RootScreen(authViewModel: AuthViewModel = koinViewModel()) {
    val status by authViewModel.status.collectAsStateWithLifecycle()
    var animationFinished by rememberSaveable { mutableStateOf(false) }

    val authResolved = status !is AuthStatus.Loading
    val showSplash = !animationFinished || !authResolved

    Box(modifier = Modifier.fillMaxSize()) {
        when (val current = status) {
            AuthStatus.Loading -> Unit
            is AuthStatus.Authenticated -> HomePlaceholderScreen(
                userName = current.user?.name,
                onSignOut = authViewModel::logout
            )

            AuthStatus.Unauthenticated -> AuthNavGraph()
        }

        if (showSplash) {
            SplashScreen(onAnimationFinished = { animationFinished = true })
        }
    }
}

@Composable
private fun AuthNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.WELCOME) {
        composable(Routes.WELCOME) { entry ->
            val activity = LocalActivity.current
            BackHandler { activity?.finish() }
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
        composable(Routes.LOGIN) { entry ->
            LoginScreen(
                onBack = { navController.popBackStack() },
                onSignUp = { navController.navigateOnce(entry, Routes.REGISTER) }
            )
        }
    }
}

private fun NavHostController.navigateOnce(from: NavBackStackEntry, route: String) {
    if (from.lifecycle.currentState == Lifecycle.State.RESUMED) {
        navigate(route)
    }
}
