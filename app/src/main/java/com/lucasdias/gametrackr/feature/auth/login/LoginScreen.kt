package com.lucasdias.gametrackr.feature.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.anim.staggeredAppear
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.AuthPrompt
import com.lucasdias.gametrackr.core.ui.components.AuthScreenScaffold
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.components.SocialLoginSection
import com.lucasdias.gametrackr.core.ui.components.TitleWithSubtitle
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.text.asString
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.auth.login.components.ForgotPasswordRow
import com.lucasdias.gametrackr.feature.auth.login.components.LoginFormSection
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onSignUp: () -> Unit,
    onForgotPassword: () -> Unit,
    onContinueAsGuest: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LoginContent(
        uiState = uiState,
        actions =
            LoginActions(
                onBack = onBack,
                onSignUp = onSignUp,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onForgotPassword = onForgotPassword,
                onSubmit = {
                    focusManager.clearFocus()
                    viewModel.onSubmit()
                },
                onSignInWithGoogle = viewModel::onSignInWithGoogle,
                onToastShown = viewModel::onToastShown,
                onContinueAsGuest = onContinueAsGuest,
            ),
    )
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    actions: LoginActions,
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
            title = stringResource(R.string.login_title),
            subtitle = stringResource(R.string.login_subtitle),
            centered = true,
            modifier =
                Modifier
                    .padding(top = Spacing.md)
                    .staggeredAppear(1),
        )

        LoginFormSection(
            email = uiState.email,
            onEmailChange = actions.onEmailChange,
            password = uiState.password,
            onPasswordChange = actions.onPasswordChange,
            emailError = uiState.emailError,
            passwordError = uiState.passwordError,
            modifier =
                Modifier
                    .padding(top = Spacing.xl)
                    .staggeredAppear(2),
        )

        ForgotPasswordRow(
            onForgotPassword = actions.onForgotPassword,
            modifier =
                Modifier
                    .padding(top = Spacing.lg)
                    .staggeredAppear(3),
        )

        PrimaryButton(
            text = stringResource(R.string.login_sign_in),
            onClick = actions.onSubmit,
            isLoading = uiState.isLoading,
            modifier =
                Modifier
                    .padding(top = Spacing.xl)
                    .staggeredAppear(4),
        )

        SocialLoginSection(
            onGoogle = actions.onSignInWithGoogle,
            modifier =
                Modifier
                    .padding(top = Spacing.xl)
                    .staggeredAppear(5),
        )

        AuthPrompt(
            prefix = stringResource(R.string.login_no_account),
            actionText = stringResource(R.string.login_sign_up),
            onClick = actions.onSignUp,
            modifier =
                Modifier
                    .padding(top = Spacing.xl)
                    .staggeredAppear(6),
        )

        AuthPrompt(
            prefix = stringResource(R.string.login_guest_prefix),
            actionText = stringResource(R.string.login_guest_action),
            onClick = actions.onContinueAsGuest,
            modifier =
                Modifier
                    .padding(top = Spacing.md)
                    .staggeredAppear(7),
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    AppPreview {
        LoginContent(uiState = LoginUiState(), actions = LoginActions())
    }
}

@Preview
@Composable
private fun LoginScreenErrorsPreview() {
    AppPreview {
        LoginContent(
            uiState = LoginUiState(email = "player", password = "abc", isSubmitted = true),
            actions = LoginActions(),
        )
    }
}
