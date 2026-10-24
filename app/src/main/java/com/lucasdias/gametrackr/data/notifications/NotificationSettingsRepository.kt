package com.lucasdias.gametrackr.data.notifications

import com.lucasdias.gametrackr.core.model.NotificationSetting
import com.lucasdias.gametrackr.core.model.NotificationSettings

interface NotificationSettingsRepository {
    suspend fun fetchSettings(): Result<NotificationSettings>

    suspend fun updateSetting(
        setting: NotificationSetting,
        enabled: Boolean,
    ): Result<Unit>
}
