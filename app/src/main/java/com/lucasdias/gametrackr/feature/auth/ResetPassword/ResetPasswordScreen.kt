package com.lucasdias.gametrackr.feature.auth.resetpassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
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
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.anim.staggeredAppear
import com.lucasdias.gametrackr.core.ui.components.AuthScreenScaffold
import com.lucasdias.gametrackr.core.ui.components.TitleWithSubtitle
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.theme.GameTrackrTheme
import com.lucasdias.gametrackr.feature.auth.resetpassword.components.ResetPasswordBottomSection
import com.lucasdias.gametrackr.feature.auth.resetpassword.components.ResetPasswordFormSection

@Composable
fun ResetPasswordScreen(
    email: String,
    code: String,
    onBack: () -> Unit,
    onReset: () -> Unit,
    viewModel: ResetPasswordViewModel = koinViewModel { parametersOf(email, code) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.done) {
        if (uiState.done) onReset()
    }

    ResetPasswordContent(
        uiState = uiState,
        onBack = onBack,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onSubmit = viewModel::onSubmit,
        onErrorShown = viewModel::onErrorShown
    )
}

@Composable
private fun ResetPasswordContent(
    uiState: ResetPasswordUiState,
    onBack: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onErrorShown: () -> Unit
) {
    AuthScreenScaffold(
        onBack = onBack,
        contentArrangement = Arrangement.Center,
        overlay = {
            Toast(
                message = uiState.errorMessage,
                onDismiss = onErrorShown,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    ) {
        TitleWithSubtitle(
            title = stringResource(R.string.reset_title),
            subtitle = stringResource(R.string.reset_subtitle),
            centered = true,
            modifier = Modifier
                .padding(top = 16.dp)
                .staggeredAppear(1)
        )

        ResetPasswordFormSection(
            password = uiState.password,
            onPasswordChange = onPasswordChange,
            confirmPassword = uiState.confirmPassword,
            onConfirmPasswordChange = onConfirmPasswordChange,
            passwordError = uiState.passwordError,
            confirmPasswordError = uiState.confirmPasswordError,
            modifier = Modifier
                .padding(top = 28.dp)
                .staggeredAppear(2)
        )

        ResetPasswordBottomSection(
            isLoading = uiState.isLoading,
            onSave = onSubmit,
            modifier = Modifier
                .padding(top = 28.dp)
                .fillMaxWidth()
                .staggeredAppear(3)
        )
    }
}

@Preview
@Composable
private fun ResetPasswordPreview() {
    GameTrackrTheme {
        ResetPasswordContent(
            uiState = ResetPasswordUiState(password = "secret1"),
            onBack = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onSubmit = {},
            onErrorShown = {}
        )
    }
}
