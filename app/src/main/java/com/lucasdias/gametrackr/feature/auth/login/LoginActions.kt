package com.lucasdias.gametrackr.feature.auth.login

data class LoginActions(
    val onBack: () -> Unit = {},
    val onSignUp: () -> Unit = {},
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onForgotPassword: () -> Unit = {},
    val onSubmit: () -> Unit = {},
    val onSignInWithGoogle: () -> Unit = {},
    val onToastShown: () -> Unit = {},
    val onContinueAsGuest: () -> Unit = {},
)
