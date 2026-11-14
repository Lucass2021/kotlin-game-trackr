package com.lucasdias.gametrackr.navigation

import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

object AuthRoute {
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

fun NavHostController.navigateOnce(
    from: NavBackStackEntry,
    route: String,
) {
    if (from.lifecycle.currentState == Lifecycle.State.RESUMED) {
        navigate(route)
    }
}

fun NavHostController.openAuthRoute(
    from: NavBackStackEntry,
    route: String,
) {
    if (from.lifecycle.currentState != Lifecycle.State.RESUMED || currentDestination?.route == route) return
    if (!popBackStack(route, inclusive = false)) navigate(route)
}
