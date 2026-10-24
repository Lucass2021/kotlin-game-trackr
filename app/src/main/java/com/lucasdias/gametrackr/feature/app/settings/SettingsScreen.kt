package com.lucasdias.gametrackr.feature.app.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.NotificationSettings
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.text.asString
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.app.settings.components.DeleteAccountDialog
import com.lucasdias.gametrackr.feature.app.settings.components.SettingsDestructiveRow
import com.lucasdias.gametrackr.feature.app.settings.components.SettingsNavRow
import com.lucasdias.gametrackr.feature.app.settings.components.SettingsNotificationsSection
import com.lucasdias.gametrackr.feature.app.settings.components.SettingsSection
import com.lucasdias.gametrackr.feature.app.stats.components.StatsTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onChangePassword: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnLogout by rememberUpdatedState(onLogout)

    val close = { if (!state.isDeleting) onBack() }
    BackHandler(onBack = close)

    LaunchedEffect(Unit) { viewModel.loadIfNeeded() }
    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) currentOnLogout()
    }

    SettingsContent(
        state = state,
        actions =
            SettingsActions(
                onBack = close,
                onChangePassword = onChangePassword,
                onNotificationToggle = viewModel::onNotificationToggle,
                onRetry = viewModel::load,
                onDeleteAccount = viewModel::onDeleteAccount,
                onToastShown = viewModel::onToastShown,
            ),
        modifier = modifier,
    )
}

@Composable
fun SettingsContent(
    state: SettingsUiState,
    actions: SettingsActions,
    modifier: Modifier = Modifier,
) {
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize().background(AppBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            StatsTopBar(title = stringResource(R.string.settings_title), onBack = actions.onBack)

            LazyColumn(
                contentPadding = PaddingValues(start = Spacing.lg, end = Spacing.lg, top = Spacing.sm, bottom = Spacing.xxl),
                verticalArrangement = Arrangement.spacedBy(Spacing.xl),
            ) {
                item {
                    SettingsNotificationsSection(
                        settings = state.notificationSettings,
                        hasError = state.hasError,
                        onToggle = actions.onNotificationToggle,
                        onRetry = actions.onRetry,
                    )
                }
                item {
                    SettingsSection(title = stringResource(R.string.settings_section_account)) {
                        SettingsNavRow(
                            icon = AppIcon.SHIELD_CHECK,
                            title = stringResource(R.string.settings_change_password),
                            onClick = actions.onChangePassword,
                        )
                    }
                }
                item {
                    SettingsSection(title = stringResource(R.string.settings_section_danger)) {
                        SettingsDestructiveRow(
                            icon = AppIcon.CLOSE,
                            title = stringResource(R.string.settings_delete_account),
                            isLoading = state.isDeleting,
                            onClick = { if (!state.isDeleting) showDeleteDialog = true },
                        )
                    }
                }
            }
        }

        Toast(
            message = state.toastMessage?.asString(),
            onDismiss = actions.onToastShown,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }

    if (showDeleteDialog) {
        DeleteAccountDialog(
            onConfirm = {
                showDeleteDialog = false
                actions.onDeleteAccount()
            },
            onDismiss = { showDeleteDialog = false },
        )
    }
}

@Preview
@Composable
private fun SettingsContentPreview() {
    AppPreview {
        SettingsContent(state = SettingsUiState(notificationSettings = NotificationSettings()), actions = SettingsActions())
    }
}

@Preview
@Composable
private fun SettingsContentLoadingPreview() {
    AppPreview {
        SettingsContent(state = SettingsUiState(isLoading = true), actions = SettingsActions())
    }
}
