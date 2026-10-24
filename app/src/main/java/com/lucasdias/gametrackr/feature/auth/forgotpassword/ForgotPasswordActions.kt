package com.lucasdias.gametrackr.feature.auth.forgotpassword

data class ForgotPasswordActions(
    val onBack: () -> Unit = {},
    val onEmailChange: (String) -> Unit = {},
    val onSubmit: () -> Unit = {},
    val onToastShown: () -> Unit = {},
)
