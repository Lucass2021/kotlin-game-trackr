package com.lucasdias.gametrackr.feature.auth.resetpassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.anim.staggeredAppear
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.AuthScreenScaffold
import com.lucasdias.gametrackr.core.ui.components.TitleWithSubtitle
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.text.asString
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.auth.resetpassword.components.ResetPasswordBottomSection
import com.lucasdias.gametrackr.feature.auth.resetpassword.components.ResetPasswordFormSection
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ResetPasswordScreen(
    email: String,
    code: String,
    onBack: () -> Unit,
    onReset: () -> Unit,
    viewModel: ResetPasswordViewModel = koinViewModel { parametersOf(email, code) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnReset by rememberUpdatedState(onReset)
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState.isReset) {
        if (uiState.isReset) currentOnReset()
    }

    ResetPasswordContent(
        uiState = uiState,
        actions =
            ResetPasswordActions(
                onBack = onBack,
                onPasswordChange = viewModel::onPasswordChange,
                onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                onSubmit = {
                    focusManager.clearFocus()
                    viewModel.onSubmit()
                },
                onToastShown = viewModel::onToastShown,
            ),
    )
}

@Composable
private fun ResetPasswordContent(
    uiState: ResetPasswordUiState,
    actions: ResetPasswordActions,
) {
    AuthScreenScaffold(
        onBack = actions.onBack,
        contentArrangement = Arrangement.Center,
        overlay = {
            Toast(
                message = uiState.toastMessage?.asString(),
                onDismiss = actions.onToastShown,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        },
    ) {
        TitleWithSubtitle(
            title = stringResource(R.string.reset_title),
            subtitle = stringResource(R.string.reset_subtitle),
            centered = true,
            modifier =
                Modifier
                    .padding(top = Spacing.md)
                    .staggeredAppear(1),
        )

        ResetPasswordFormSection(
            password = uiState.password,
            onPasswordChange = actions.onPasswordChange,
            confirmPassword = uiState.confirmPassword,
            onConfirmPasswordChange = actions.onConfirmPasswordChange,
            passwordError = uiState.passwordError,
            confirmPasswordError = uiState.confirmPasswordError,
            modifier =
                Modifier
                    .padding(top = Spacing.xl)
                    .staggeredAppear(2),
        )

        ResetPasswordBottomSection(
            isLoading = uiState.isLoading,
            onSave = actions.onSubmit,
            modifier =
                Modifier
                    .padding(top = Spacing.xl)
                    .fillMaxWidth()
                    .staggeredAppear(3),
        )
    }
}

@Preview
@Composable
private fun ResetPasswordPreview() {
    AppPreview {
        ResetPasswordContent(uiState = ResetPasswordUiState(password = "secret1"), actions = ResetPasswordActions())
    }
}

@Preview
@Composable
private fun ResetPasswordErrorsPreview() {
    AppPreview {
        ResetPasswordContent(
            uiState = ResetPasswordUiState(password = "abc", confirmPassword = "abd", isSubmitted = true),
            actions = ResetPasswordActions(),
        )
    }
}
