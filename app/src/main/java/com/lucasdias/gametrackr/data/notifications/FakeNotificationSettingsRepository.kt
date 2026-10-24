package com.lucasdias.gametrackr.data.notifications

import com.lucasdias.gametrackr.core.model.NotificationSetting
import com.lucasdias.gametrackr.core.model.NotificationSettings
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeNotificationSettingsRepository(
    settings: NotificationSettings = NotificationSettings(),
    var failure: Throwable? = null,
) : NotificationSettingsRepository {
    var settings = settings
        private set

    var fetchCount = 0
        private set

    var updateCount = 0
        private set

    override suspend fun fetchSettings(): Result<NotificationSettings> {
        fetchCount++
        val error = failure
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return if (error != null) Result.failure(error) else Result.success(settings)
    }

    override suspend fun updateSetting(
        setting: NotificationSetting,
        enabled: Boolean,
    ): Result<Unit> {
        updateCount++
        val error = failure
        delay(FakeLatency.RESPONSE_DELAY_MS)
        if (error != null) return Result.failure(error)
        settings = settings.with(setting, enabled)
        return Result.success(Unit)
    }
}
