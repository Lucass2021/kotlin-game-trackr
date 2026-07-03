package com.lucasdias.gametrackr.navigation

import android.net.Uri
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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.auth.AuthStatus
import com.lucasdias.gametrackr.core.ui.components.SuccessScreen
import com.lucasdias.gametrackr.feature.app.appshell.MainTabScreen
import com.lucasdias.gametrackr.feature.auth.AuthViewModel
import com.lucasdias.gametrackr.feature.auth.forgotpassword.ForgotPasswordScreen
import com.lucasdias.gametrackr.feature.auth.login.LoginScreen
import com.lucasdias.gametrackr.feature.auth.register.RegisterScreen
import com.lucasdias.gametrackr.feature.auth.resetpassword.ResetPasswordScreen
import com.lucasdias.gametrackr.feature.auth.verifyresetcode.VerifyResetCodeScreen
import com.lucasdias.gametrackr.feature.auth.welcome.WelcomeScreen
import com.lucasdias.gametrackr.feature.splash.SplashScreen
import org.koin.androidx.compose.koinViewModel

object Routes {
    const val WELCOME = "welcome"
    const val REGISTER = "register"
    const val LOGIN = "login"
    const val FORGOT_PASSWORD = "forgot_password"

    const val VERIFY_CODE = "verify_code/{email}"

    fun verifyCode(email: String) = "verify_code/${Uri.encode(email)}"

    const val RESET_PASSWORD = "reset_password/{email}/{code}"

    fun resetPassword(
        email: String,
        code: String,
    ) = "reset_password/${Uri.encode(email)}/${Uri.encode(code)}"

    const val SUCCESS_RESET = "success_reset"
    const val SUCCESS_REGISTER = "success_register"
}

@Composable
fun RootScreen(authViewModel: AuthViewModel = koinViewModel()) {
    val status by authViewModel.status.collectAsStateWithLifecycle()
    var animationFinished by rememberSaveable { mutableStateOf(false) }

    val authResolved = status !is AuthStatus.Loading
    val showSplash = !animationFinished || !authResolved

    Box(modifier = Modifier.fillMaxSize()) {
        when (val current = status) {
            AuthStatus.Loading -> {
                Unit
            }

            is AuthStatus.Authenticated -> {
                MainTabScreen(
                    isGuest = false,
                    userName = current.user?.name,
                    email = current.user?.email,
                    onLogout = authViewModel::logout,
                )
            }

            AuthStatus.Guest -> {
                MainTabScreen(
                    isGuest = true,
                    userName = null,
                    email = null,
                    onLogout = authViewModel::logout,
                )
            }

            AuthStatus.Unauthenticated -> {
                AuthNavGraph(authViewModel = authViewModel)
            }
        }

        if (showSplash) {
            SplashScreen(onAnimationFinished = { animationFinished = true })
        }
    }
}

@Composable
private fun AuthNavGraph(
    authViewModel: AuthViewModel,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = Routes.WELCOME) {
        composable(Routes.WELCOME) { entry ->
            val activity = LocalActivity.current
            BackHandler { activity?.finish() }
            WelcomeScreen(
                onCreateAccount = { navController.navigateOnce(entry, Routes.REGISTER) },
                onSignIn = { navController.navigateOnce(entry, Routes.LOGIN) },
            )
        }
        composable(Routes.REGISTER) { entry ->
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onSignIn = { navController.navigateOnce(entry, Routes.LOGIN) },
                onRegistered = { navController.navigate(Routes.SUCCESS_REGISTER) },
            )
        }
        composable(Routes.LOGIN) { entry ->
            LoginScreen(
                onBack = { navController.popBackStack() },
                onSignUp = { navController.navigateOnce(entry, Routes.REGISTER) },
                onForgotPassword = { navController.navigateOnce(entry, Routes.FORGOT_PASSWORD) },
                onContinueAsGuest = authViewModel::continueAsGuest,
            )
        }
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() },
                onCodeSent = { email -> navController.navigate(Routes.verifyCode(email)) },
            )
        }
        composable(
            Routes.VERIFY_CODE,
            arguments = listOf(navArgument("email") { type = NavType.StringType }),
        ) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            VerifyResetCodeScreen(
                email = email,
                onBack = { navController.popBackStack() },
                onVerified = { code -> navController.navigate(Routes.resetPassword(email, code)) },
            )
        }
        composable(
            Routes.RESET_PASSWORD,
            arguments =
                listOf(
                    navArgument("email") { type = NavType.StringType },
                    navArgument("code") { type = NavType.StringType },
                ),
        ) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            val code = entry.arguments?.getString("code").orEmpty()
            ResetPasswordScreen(
                email = email,
                code = code,
                onBack = { navController.popBackStack() },
                onReset = { navController.navigate(Routes.SUCCESS_RESET) },
            )
        }
        composable(Routes.SUCCESS_RESET) {
            SuccessScreen(
                title = stringResource(R.string.success_reset_title),
                subtitle = stringResource(R.string.success_reset_subtitle),
                buttonText = stringResource(R.string.success_reset_button),
                statusTitle = stringResource(R.string.success_status_title),
                statusValue = stringResource(R.string.success_status_value),
                onPrimary = {
                    authViewModel.completePasswordReset(
                        onFailure = {
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.WELCOME)
                                launchSingleTop = true
                            }
                        },
                    )
                },
            )
        }
        composable(Routes.SUCCESS_REGISTER) {
            SuccessScreen(
                title = stringResource(R.string.success_register_title),
                subtitle = stringResource(R.string.success_register_subtitle),
                buttonText = stringResource(R.string.success_register_button),
                statusTitle = stringResource(R.string.success_status_title),
                statusValue = stringResource(R.string.success_status_value),
                onPrimary = authViewModel::completeRegistration,
            )
        }
    }
}

private fun NavHostController.navigateOnce(
    from: NavBackStackEntry,
    route: String,
) {
    if (from.lifecycle.currentState == Lifecycle.State.RESUMED) {
        navigate(route)
    }
}
