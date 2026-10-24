package com.lucasdias.gametrackr.feature.app.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.NotificationSetting
import com.lucasdias.gametrackr.core.model.NotificationSettings
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val MESSAGE_SIZE = 14.sp

@Composable
fun SettingsNotificationsSection(
    settings: NotificationSettings?,
    hasError: Boolean,
    onToggle: (NotificationSetting, Boolean) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsSection(title = stringResource(R.string.settings_section_notifications), modifier = modifier) {
        when {
            settings != null -> {
                NotificationSetting.entries.forEachIndexed { index, setting ->
                    SettingsToggleRow(
                        icon = setting.icon,
                        title = stringResource(setting.titleRes),
                        checked = settings.isEnabled(setting),
                        onCheckedChange = { onToggle(setting, it) },
                    )
                    if (index < NotificationSetting.entries.lastIndex) SettingsDivider()
                }
            }

            hasError -> {
                MessageRow(text = stringResource(R.string.settings_notifications_error)) {
                    TextButton(onClick = onRetry) {
                        Text(text = stringResource(R.string.settings_try_again), color = AppPrimary, style = AppType.label(MESSAGE_SIZE))
                    }
                }
            }

            else -> {
                MessageRow(text = stringResource(R.string.settings_notifications_loading))
            }
        }
    }
}

private val NotificationSetting.icon: AppIcon
    get() =
        when (this) {
            NotificationSetting.PUSH -> AppIcon.NOTIFICATIONS
            NotificationSetting.FRIEND_REQUESTS -> AppIcon.ADD_FRIEND
            NotificationSetting.COMMUNITY_REPLIES -> AppIcon.COMMENT
        }

private val NotificationSetting.titleRes: Int
    get() =
        when (this) {
            NotificationSetting.PUSH -> R.string.settings_push_notifications
            NotificationSetting.FRIEND_REQUESTS -> R.string.settings_friend_requests
            NotificationSetting.COMMUNITY_REPLIES -> R.string.settings_community_replies
        }

@Composable
private fun MessageRow(
    text: String,
    action: @Composable () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.md, vertical = Spacing.xxs),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            color = AppTextSecondary,
            style = AppType.body(MESSAGE_SIZE),
            modifier = Modifier.weight(1f).padding(vertical = Spacing.sm),
        )
        action()
    }
}

@Preview
@Composable
private fun SettingsNotificationsSectionPreview() {
    AppPreview {
        SettingsNotificationsSection(
            settings = NotificationSettings(friendRequests = false),
            hasError = false,
            onToggle = { _, _ -> },
            onRetry = {},
        )
    }
}

@Preview
@Composable
private fun SettingsNotificationsSectionErrorPreview() {
    AppPreview {
        SettingsNotificationsSection(settings = null, hasError = true, onToggle = { _, _ -> }, onRetry = {})
    }
}
