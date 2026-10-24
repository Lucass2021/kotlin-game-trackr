package com.lucasdias.gametrackr.feature.app.settings

import com.lucasdias.gametrackr.core.model.NotificationSetting

data class SettingsActions(
    val onBack: () -> Unit = {},
    val onChangePassword: () -> Unit = {},
    val onNotificationToggle: (NotificationSetting, Boolean) -> Unit = { _, _ -> },
    val onRetry: () -> Unit = {},
    val onDeleteAccount: () -> Unit = {},
    val onToastShown: () -> Unit = {},
)
