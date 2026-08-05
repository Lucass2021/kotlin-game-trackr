package com.lucasdias.gametrackr.feature.app.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.stats.components.StatsTopBar

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onChangePassword: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var pushNotifications by rememberSaveable { mutableStateOf(true) }
    var friendRequests by rememberSaveable { mutableStateOf(true) }
    var communityReplies by rememberSaveable { mutableStateOf(true) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize().background(AppBackground)) {
        StatsTopBar(
            title = stringResource(R.string.settings_title),
            onBack = onBack,
        )

        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item {
                SettingsSection(title = stringResource(R.string.settings_section_notifications)) {
                    ToggleRow(
                        icon = AppIcon.NOTIFICATIONS,
                        title = stringResource(R.string.settings_push_notifications),
                        checked = pushNotifications,
                        onCheckedChange = { pushNotifications = it },
                    )
                    SectionDivider()
                    ToggleRow(
                        icon = AppIcon.ADD_FRIEND,
                        title = stringResource(R.string.settings_friend_requests),
                        checked = friendRequests,
                        onCheckedChange = { friendRequests = it },
                    )
                    SectionDivider()
                    ToggleRow(
                        icon = AppIcon.COMMENT,
                        title = stringResource(R.string.settings_community_replies),
                        checked = communityReplies,
                        onCheckedChange = { communityReplies = it },
                    )
                }
            }

            item {
                SettingsSection(title = stringResource(R.string.settings_section_account)) {
                    NavRow(
                        icon = AppIcon.SHIELD_CHECK,
                        title = stringResource(R.string.settings_change_password),
                        onClick = onChangePassword,
                    )
                }
            }

            item {
                SettingsSection(title = stringResource(R.string.settings_section_danger)) {
                    DestructiveRow(
                        icon = AppIcon.CLOSE,
                        title = stringResource(R.string.settings_delete_account),
                        onClick = { showDeleteDialog = true },
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.settings_delete_dialog_title),
                    color = AppTextPrimary,
                    style = AppType.headline(18.sp),
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.settings_delete_dialog_message),
                    color = AppTextSecondary,
                    style = AppType.body(15.sp),
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onLogout()
                }) {
                    Text(
                        text = stringResource(R.string.settings_delete_dialog_confirm),
                        color = AppTertiary,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(
                        text = stringResource(R.string.settings_delete_dialog_cancel),
                        color = AppTextSecondary,
                    )
                }
            },
            containerColor = AppSurfaceCard,
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title.uppercase(),
            color = AppTextSecondary,
            style = AppType.label(13.sp),
            modifier = Modifier.padding(start = 4.dp),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppSurfaceCard),
        ) {
            content()
        }
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(
        color = AppOutline,
        modifier = Modifier.padding(start = 54.dp),
    )
}

@Composable
private fun ToggleRow(
    icon: AppIcon,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
    ) {
        Icon(
            imageVector = icon.image(),
            contentDescription = null,
            tint = AppPrimary,
            modifier = Modifier.size(20.dp),
        )

        Text(
            text = title,
            color = AppTextPrimary,
            style = AppType.label(16.sp),
            modifier = Modifier.weight(1f),
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors =
                SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = AppPrimary,
                    uncheckedThumbColor = AppTextSecondary,
                    uncheckedTrackColor = AppOutline,
                    uncheckedBorderColor = AppOutline,
                ),
        )
    }
}

@Composable
private fun NavRow(
    icon: AppIcon,
    title: String,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier
                .fillMaxWidth()
                .pressScale(interaction)
                .clickable(
                    interactionSource = interaction,
                    indication = null,
                    role = Role.Button,
                    onClick = onClick,
                ).padding(horizontal = 16.dp, vertical = 15.dp),
    ) {
        Icon(
            imageVector = icon.image(),
            contentDescription = null,
            tint = AppPrimary,
            modifier = Modifier.size(20.dp),
        )

        Text(
            text = title,
            color = AppTextPrimary,
            style = AppType.label(16.sp),
            modifier = Modifier.weight(1f),
        )

        Icon(
            imageVector = AppIcon.FORWARD.image(),
            contentDescription = null,
            tint = AppTextSecondary,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun DestructiveRow(
    icon: AppIcon,
    title: String,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier
                .fillMaxWidth()
                .pressScale(interaction)
                .clickable(
                    interactionSource = interaction,
                    indication = null,
                    role = Role.Button,
                    onClick = onClick,
                ).padding(horizontal = 16.dp, vertical = 15.dp),
    ) {
        Icon(
            imageVector = icon.image(),
            contentDescription = null,
            tint = AppTertiary,
            modifier = Modifier.size(20.dp),
        )

        Text(
            text = title,
            color = AppTertiary,
            style = AppType.label(16.sp),
        )
    }
}
