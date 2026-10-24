package com.lucasdias.gametrackr.feature.auth.register

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
import com.lucasdias.gametrackr.core.ui.components.SocialLoginSection
import com.lucasdias.gametrackr.core.ui.components.TitleWithSubtitle
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.text.asString
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.auth.register.components.RegisterBottomSection
import com.lucasdias.gametrackr.feature.auth.register.components.RegisterFormSection
import com.lucasdias.gametrackr.feature.auth.register.components.TermsAcceptanceRow
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onSignIn: () -> Unit,
    onRegistered: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnRegistered by rememberUpdatedState(onRegistered)
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState.isRegistered) {
        if (uiState.isRegistered) currentOnRegistered()
    }

    RegisterContent(
        uiState = uiState,
        actions =
            RegisterActions(
                onBack = onBack,
                onSignIn = onSignIn,
                onNameChange = viewModel::onNameChange,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                onToggleTerms = viewModel::onToggleTerms,
                onSubmit = {
                    focusManager.clearFocus()
                    viewModel.onSubmit()
                },
                onSignUpWithGoogle = viewModel::onSignUpWithGoogle,
                onToastShown = viewModel::onToastShown,
            ),
    )
}

@Composable
private fun RegisterContent(
    uiState: RegisterUiState,
    actions: RegisterActions,
) {
    AuthScreenScaffold(
        onBack = actions.onBack,
        overlay = {
            Toast(
                message = uiState.toastMessage?.asString(),
                onDismiss = actions.onToastShown,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        },
    ) {
        TitleWithSubtitle(
            title = stringResource(R.string.register_title),
            subtitle = stringResource(R.string.register_subtitle),
            modifier =
                Modifier
                    .padding(top = Spacing.md)
                    .staggeredAppear(1),
        )

        RegisterFormSection(
            name = uiState.name,
            onNameChange = actions.onNameChange,
            email = uiState.email,
            onEmailChange = actions.onEmailChange,
            password = uiState.password,
            onPasswordChange = actions.onPasswordChange,
            confirmPassword = uiState.confirmPassword,
            onConfirmPasswordChange = actions.onConfirmPasswordChange,
            nameError = uiState.nameError,
            emailError = uiState.emailError,
            passwordError = uiState.passwordError,
            confirmPasswordError = uiState.confirmPasswordError,
            modifier =
                Modifier
                    .padding(top = Spacing.xl)
                    .staggeredAppear(2),
        )

        TermsAcceptanceRow(
            accepted = uiState.acceptedTerms,
            onToggle = actions.onToggleTerms,
            error = uiState.termsError,
            modifier =
                Modifier
                    .padding(top = Spacing.xl)
                    .staggeredAppear(3),
        )

        RegisterBottomSection(
            isLoading = uiState.isLoading,
            onCreateAccount = actions.onSubmit,
            onSignIn = actions.onSignIn,
            modifier =
                Modifier
                    .padding(top = Spacing.xl)
                    .staggeredAppear(4),
        )

        SocialLoginSection(
            onGoogle = actions.onSignUpWithGoogle,
            modifier =
                Modifier
                    .padding(top = Spacing.xl)
                    .staggeredAppear(5),
        )
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    AppPreview {
        RegisterContent(uiState = RegisterUiState(), actions = RegisterActions())
    }
}

@Preview
@Composable
private fun RegisterScreenErrorsPreview() {
    AppPreview {
        RegisterContent(
            uiState = RegisterUiState(name = "Al", email = "player", password = "abc", isSubmitted = true),
            actions = RegisterActions(),
        )
    }
}
