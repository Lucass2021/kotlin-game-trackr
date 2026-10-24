package com.lucasdias.gametrackr.feature.auth.forgotpassword

import androidx.compose.foundation.layout.Arrangement
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
import com.lucasdias.gametrackr.feature.auth.forgotpassword.components.ForgotPasswordBottomSection
import com.lucasdias.gametrackr.feature.auth.forgotpassword.components.ForgotPasswordFormSection
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit,
    onCodeSent: (String) -> Unit,
    viewModel: ForgotPasswordViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnCodeSent by rememberUpdatedState(onCodeSent)
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState.sentToEmail) {
        val email = uiState.sentToEmail ?: return@LaunchedEffect
        currentOnCodeSent(email)
        viewModel.onCodeSentHandled()
    }

    ForgotPasswordContent(
        uiState = uiState,
        actions =
            ForgotPasswordActions(
                onBack = onBack,
                onEmailChange = viewModel::onEmailChange,
                onSubmit = {
                    focusManager.clearFocus()
                    viewModel.onSubmit()
                },
                onToastShown = viewModel::onToastShown,
            ),
    )
}

@Composable
private fun ForgotPasswordContent(
    uiState: ForgotPasswordUiState,
    actions: ForgotPasswordActions,
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
        bottomBar = {
            ForgotPasswordBottomSection(
                isLoading = uiState.isLoading,
                onSendCode = actions.onSubmit,
                onBackToLogin = actions.onBack,
                modifier = Modifier.staggeredAppear(3),
            )
        },
    ) {
        TitleWithSubtitle(
            title = stringResource(R.string.forgot_title),
            subtitle = stringResource(R.string.forgot_subtitle),
            modifier =
                Modifier
                    .padding(top = Spacing.md)
                    .staggeredAppear(1),
        )

        ForgotPasswordFormSection(
            email = uiState.email,
            onEmailChange = actions.onEmailChange,
            emailError = uiState.emailError,
            modifier =
                Modifier
                    .padding(top = Spacing.xl)
                    .staggeredAppear(2),
        )
    }
}

@Preview
@Composable
private fun ForgotPasswordPreview() {
    AppPreview {
        ForgotPasswordContent(uiState = ForgotPasswordUiState(), actions = ForgotPasswordActions())
    }
}

@Preview
@Composable
private fun ForgotPasswordErrorPreview() {
    AppPreview {
        ForgotPasswordContent(
            uiState = ForgotPasswordUiState(email = "player", isSubmitted = true),
            actions = ForgotPasswordActions(),
        )
    }
}
