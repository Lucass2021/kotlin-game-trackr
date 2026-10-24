package com.lucasdias.gametrackr.feature.auth.register

data class RegisterActions(
    val onBack: () -> Unit = {},
    val onSignIn: () -> Unit = {},
    val onNameChange: (String) -> Unit = {},
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onConfirmPasswordChange: (String) -> Unit = {},
    val onToggleTerms: () -> Unit = {},
    val onSubmit: () -> Unit = {},
    val onSignUpWithGoogle: () -> Unit = {},
    val onToastShown: () -> Unit = {},
)
