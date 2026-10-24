package com.lucasdias.gametrackr.feature.app.settings.changepassword

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.AuthTextField
import com.lucasdias.gametrackr.core.ui.components.PasswordStrength
import com.lucasdias.gametrackr.core.ui.components.PasswordStrengthMeter
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.text.asString
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.app.stats.components.StatsTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChangePasswordViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnSuccess by rememberUpdatedState(onSuccess)
    val focusManager = LocalFocusManager.current

    LaunchedEffect(state.isChanged) {
        if (state.isChanged) currentOnSuccess()
    }

    val close = { if (!state.isSaving) onBack() }
    BackHandler(onBack = close)

    ChangePasswordContent(
        state = state,
        actions =
            ChangePasswordActions(
                onBack = close,
                onSave = {
                    focusManager.clearFocus()
                    viewModel.onSave()
                },
                onCurrentPasswordChange = viewModel::onCurrentPasswordChange,
                onNewPasswordChange = viewModel::onNewPasswordChange,
                onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                onToastShown = viewModel::onToastShown,
            ),
        modifier = modifier,
    )
}

@Composable
fun ChangePasswordContent(
    state: ChangePasswordUiState,
    actions: ChangePasswordActions,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize().background(AppBackground)) {
        Column(modifier = Modifier.fillMaxSize().imePadding()) {
            StatsTopBar(title = stringResource(R.string.change_password_title), onBack = actions.onBack)

            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = Spacing.lg, vertical = Spacing.md),
            ) {
                AuthTextField(
                    label = stringResource(R.string.change_password_current_label),
                    placeholder = stringResource(R.string.change_password_placeholder),
                    value = state.currentPassword,
                    onValueChange = actions.onCurrentPasswordChange,
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                    error = state.currentPasswordError,
                )

                Spacer(modifier = Modifier.height(Spacing.lg))

                AuthTextField(
                    label = stringResource(R.string.change_password_new_label),
                    placeholder = stringResource(R.string.change_password_placeholder),
                    value = state.newPassword,
                    onValueChange = actions.onNewPasswordChange,
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                    error = state.newPasswordError,
                )

                if (state.newPassword.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(Spacing.sm))
                    PasswordStrengthMeter(strength = PasswordStrength.of(state.newPassword))
                }

                Spacer(modifier = Modifier.height(Spacing.lg))

                AuthTextField(
                    label = stringResource(R.string.change_password_confirm_label),
                    placeholder = stringResource(R.string.change_password_placeholder),
                    value = state.confirmPassword,
                    onValueChange = actions.onConfirmPasswordChange,
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    error = state.confirmPasswordError,
                )
            }

            PrimaryButton(
                text = stringResource(R.string.change_password_save),
                onClick = actions.onSave,
                isLoading = state.isSaving,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.lg)
                        .padding(bottom = Spacing.md)
                        .navigationBarsPadding(),
            )
        }

        Toast(
            message = state.toastMessage?.asString(),
            onDismiss = actions.onToastShown,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Preview
@Composable
private fun ChangePasswordContentPreview() {
    AppPreview {
        ChangePasswordContent(state = ChangePasswordUiState(newPassword = "hunter22"), actions = ChangePasswordActions())
    }
}

@Preview
@Composable
private fun ChangePasswordContentErrorsPreview() {
    AppPreview {
        ChangePasswordContent(state = ChangePasswordUiState(isSubmitted = true), actions = ChangePasswordActions())
    }
}
