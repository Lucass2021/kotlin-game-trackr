package com.lucasdias.gametrackr.feature.app.settings

import com.lucasdias.gametrackr.core.model.NotificationSettings
import com.lucasdias.gametrackr.core.ui.text.UiText

data class SettingsUiState(
    val notificationSettings: NotificationSettings? = null,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val isDeleting: Boolean = false,
    val isDeleted: Boolean = false,
    val toastMessage: UiText? = null,
)
