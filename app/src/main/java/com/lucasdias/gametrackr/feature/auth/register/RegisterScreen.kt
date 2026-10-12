package com.lucasdias.gametrackr.feature.auth.register

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.anim.staggeredAppear
import com.lucasdias.gametrackr.core.ui.components.AuthScreenScaffold
import com.lucasdias.gametrackr.core.ui.components.SocialLoginSection
import com.lucasdias.gametrackr.core.ui.components.TitleWithSubtitle
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.theme.GameTrackrTheme
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

    LaunchedEffect(uiState.registered) {
        if (uiState.registered) onRegistered()
    }

    RegisterContent(
        uiState = uiState,
        onBack = onBack,
        onSignIn = onSignIn,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onToggleTerms = viewModel::onToggleTerms,
        onSubmit = viewModel::onSubmit,
        onGoogleSignUp = viewModel::onGoogleSignUp,
        onErrorShown = viewModel::onErrorShown,
    )
}

@Composable
private fun RegisterContent(
    uiState: RegisterUiState,
    onBack: () -> Unit,
    onSignIn: () -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onToggleTerms: () -> Unit,
    onSubmit: () -> Unit,
    onGoogleSignUp: () -> Unit,
    onErrorShown: () -> Unit,
) {
    AuthScreenScaffold(
        onBack = onBack,
        overlay = {
            Toast(
                message = uiState.errorMessage,
                onDismiss = onErrorShown,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        },
    ) {
        TitleWithSubtitle(
            title = stringResource(R.string.register_title),
            subtitle = stringResource(R.string.register_subtitle),
            modifier =
                Modifier
                    .padding(top = 16.dp)
                    .staggeredAppear(1),
        )

        RegisterFormSection(
            name = uiState.name,
            onNameChange = onNameChange,
            email = uiState.email,
            onEmailChange = onEmailChange,
            password = uiState.password,
            onPasswordChange = onPasswordChange,
            confirmPassword = uiState.confirmPassword,
            onConfirmPasswordChange = onConfirmPasswordChange,
            nameError = uiState.nameError,
            emailError = uiState.emailError,
            passwordError = uiState.passwordError,
            confirmPasswordError = uiState.confirmPasswordError,
            modifier =
                Modifier
                    .padding(top = 28.dp)
                    .staggeredAppear(2),
        )

        TermsAcceptanceRow(
            accepted = uiState.acceptedTerms,
            onToggle = onToggleTerms,
            error = uiState.termsError,
            modifier =
                Modifier
                    .padding(top = 24.dp)
                    .staggeredAppear(3),
        )

        RegisterBottomSection(
            isLoading = uiState.isLoading,
            onCreateAccount = onSubmit,
            onSignIn = onSignIn,
            modifier =
                Modifier
                    .padding(top = 24.dp)
                    .staggeredAppear(4),
        )

        SocialLoginSection(
            onGoogle = onGoogleSignUp,
            modifier =
                Modifier
                    .padding(top = 28.dp)
                    .staggeredAppear(5),
        )
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    GameTrackrTheme {
        RegisterContent(
            uiState = RegisterUiState(),
            onBack = {},
            onSignIn = {},
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onToggleTerms = {},
            onSubmit = {},
            onGoogleSignUp = {},
            onErrorShown = {},
        )
    }
}
