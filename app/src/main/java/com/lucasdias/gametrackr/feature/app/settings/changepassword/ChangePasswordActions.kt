package com.lucasdias.gametrackr.feature.app.settings.changepassword

data class ChangePasswordActions(
    val onBack: () -> Unit = {},
    val onSave: () -> Unit = {},
    val onCurrentPasswordChange: (String) -> Unit = {},
    val onNewPasswordChange: (String) -> Unit = {},
    val onConfirmPasswordChange: (String) -> Unit = {},
    val onToastShown: () -> Unit = {},
)
