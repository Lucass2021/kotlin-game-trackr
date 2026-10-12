package com.lucasdias.gametrackr.feature.auth.forgotpassword

import androidx.compose.foundation.layout.Arrangement
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
import com.lucasdias.gametrackr.core.ui.components.TitleWithSubtitle
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.theme.GameTrackrTheme
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

    LaunchedEffect(uiState.sentToEmail) {
        val email = uiState.sentToEmail ?: return@LaunchedEffect
        onCodeSent(email)
        viewModel.onCodeSentHandled()
    }

    ForgotPasswordContent(
        uiState = uiState,
        onBack = onBack,
        onEmailChange = viewModel::onEmailChange,
        onSubmit = viewModel::onSubmit,
        onErrorShown = viewModel::onErrorShown,
    )
}

@Composable
private fun ForgotPasswordContent(
    uiState: ForgotPasswordUiState,
    onBack: () -> Unit,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onErrorShown: () -> Unit,
) {
    AuthScreenScaffold(
        onBack = onBack,
        contentArrangement = Arrangement.Center,
        overlay = {
            Toast(
                message = uiState.errorMessage,
                onDismiss = onErrorShown,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        },
        bottomBar = {
            ForgotPasswordBottomSection(
                isLoading = uiState.isLoading,
                onSendCode = onSubmit,
                onBackToLogin = onBack,
                modifier = Modifier.staggeredAppear(3),
            )
        },
    ) {
        TitleWithSubtitle(
            title = stringResource(R.string.forgot_title),
            subtitle = stringResource(R.string.forgot_subtitle),
            modifier =
                Modifier
                    .padding(top = 16.dp)
                    .staggeredAppear(1),
        )

        ForgotPasswordFormSection(
            email = uiState.email,
            onEmailChange = onEmailChange,
            emailError = uiState.emailError,
            modifier =
                Modifier
                    .padding(top = 28.dp)
                    .staggeredAppear(2),
        )
    }
}

@Preview
@Composable
private fun ForgotPasswordPreview() {
    GameTrackrTheme {
        ForgotPasswordContent(
            uiState = ForgotPasswordUiState(),
            onBack = {},
            onEmailChange = {},
            onSubmit = {},
            onErrorShown = {},
        )
    }
}
