package com.lucasdias.gametrackr.feature.auth.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.anim.staggeredAppear
import com.lucasdias.gametrackr.core.ui.components.AuthScreenScaffold
import com.lucasdias.gametrackr.core.ui.components.BackButton
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.components.SocialLoginSection
import com.lucasdias.gametrackr.core.ui.components.TitleWithSubtitle
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.theme.GameTrackrTheme
import com.lucasdias.gametrackr.feature.auth.login.components.LoginFormSection
import com.lucasdias.gametrackr.feature.auth.login.components.RememberMeRow
import com.lucasdias.gametrackr.feature.auth.login.components.SignUpPrompt

@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onSignUp: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginContent(
        uiState = uiState,
        onBack = onBack,
        onSignUp = onSignUp,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onToggleRememberMe = viewModel::onToggleRememberMe,
        onForgotPassword = viewModel::onForgotPassword,
        onSubmit = viewModel::onSubmit,
        onGoogleSignIn = viewModel::onGoogleSignIn,
        onErrorShown = viewModel::onErrorShown
    )
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onBack: () -> Unit,
    onSignUp: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onToggleRememberMe: () -> Unit,
    onForgotPassword: () -> Unit,
    onSubmit: () -> Unit,
    onGoogleSignIn: () -> Unit,
    onErrorShown: () -> Unit
) {
    AuthScreenScaffold(
        scrollable = false,
        overlay = {
            Toast(
                message = uiState.errorMessage,
                onDismiss = onErrorShown,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    ) {
        BackButton(onBack = onBack, modifier = Modifier.staggeredAppear(0))

        Spacer(modifier = Modifier.weight(1f))

        TitleWithSubtitle(
            title = stringResource(R.string.login_title),
            subtitle = stringResource(R.string.login_subtitle),
            centered = true,
            modifier = Modifier
                .padding(top = 16.dp)
                .staggeredAppear(1)
        )

        LoginFormSection(
            email = uiState.email,
            onEmailChange = onEmailChange,
            password = uiState.password,
            onPasswordChange = onPasswordChange,
            emailError = uiState.emailError,
            passwordError = uiState.passwordError,
            modifier = Modifier
                .padding(top = 28.dp)
                .staggeredAppear(2)
        )

        RememberMeRow(
            rememberMe = uiState.rememberMe,
            onToggle = onToggleRememberMe,
            onForgotPassword = onForgotPassword,
            modifier = Modifier
                .padding(top = 20.dp)
                .staggeredAppear(3)
        )

        PrimaryButton(
            text = stringResource(R.string.login_sign_in),
            onClick = onSubmit,
            isLoading = uiState.isLoading,
            modifier = Modifier
                .padding(top = 24.dp)
                .staggeredAppear(4)
        )

        SocialLoginSection(
            onGoogle = onGoogleSignIn,
            modifier = Modifier
                .padding(top = 28.dp)
                .staggeredAppear(5)
        )

        SignUpPrompt(
            onSignUp = onSignUp,
            modifier = Modifier
                .padding(top = 28.dp)
                .staggeredAppear(6)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    GameTrackrTheme {
        LoginContent(
            uiState = LoginUiState(),
            onBack = {},
            onSignUp = {},
            onEmailChange = {},
            onPasswordChange = {},
            onToggleRememberMe = {},
            onForgotPassword = {},
            onSubmit = {},
            onGoogleSignIn = {},
            onErrorShown = {}
        )
    }
}
