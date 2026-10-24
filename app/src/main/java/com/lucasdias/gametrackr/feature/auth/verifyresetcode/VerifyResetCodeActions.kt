package com.lucasdias.gametrackr.feature.auth.verifyresetcode

data class VerifyResetCodeActions(
    val onBack: () -> Unit = {},
    val onCodeChange: (String) -> Unit = {},
    val onSubmit: () -> Unit = {},
    val onResend: () -> Unit = {},
    val onToastShown: () -> Unit = {},
)
