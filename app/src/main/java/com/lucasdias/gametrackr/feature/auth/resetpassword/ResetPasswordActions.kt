package com.lucasdias.gametrackr.feature.auth.resetpassword

data class ResetPasswordActions(
    val onBack: () -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onConfirmPasswordChange: (String) -> Unit = {},
    val onSubmit: () -> Unit = {},
    val onToastShown: () -> Unit = {},
)
