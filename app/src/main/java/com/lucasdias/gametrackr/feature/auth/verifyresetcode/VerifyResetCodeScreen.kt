package com.lucasdias.gametrackr.feature.auth.verifyresetcode

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.anim.staggeredAppear
import com.lucasdias.gametrackr.core.ui.components.AuthScreenScaffold
import com.lucasdias.gametrackr.core.ui.components.OtpField
import com.lucasdias.gametrackr.core.ui.components.TitleWithSubtitle
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.GameTrackrTheme
import com.lucasdias.gametrackr.feature.auth.verifyresetcode.components.VerifyResetCodeBottomSection
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun VerifyResetCodeScreen(
    email: String,
    onBack: () -> Unit,
    onVerified: (String) -> Unit,
    viewModel: VerifyResetCodeViewModel = koinViewModel { parametersOf(email) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.verified) {
        if (uiState.verified) {
            onVerified(uiState.code)
            viewModel.onVerifiedHandled()
        }
    }

    VerifyResetCodeContent(
        email = email,
        uiState = uiState,
        onBack = onBack,
        onCodeChange = viewModel::onCodeChange,
        onConfirm = viewModel::onConfirm,
        onResend = viewModel::onResend,
        onErrorShown = viewModel::onErrorShown,
    )
}

@Composable
private fun VerifyResetCodeContent(
    email: String,
    uiState: VerifyResetCodeUiState,
    onBack: () -> Unit,
    onCodeChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onResend: () -> Unit,
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
        bottomBar = {
            VerifyResetCodeBottomSection(
                seconds = uiState.resendSeconds,
                onResend = onResend,
                isLoading = uiState.isLoading,
                onConfirm = onConfirm,
                modifier = Modifier.staggeredAppear(3),
            )
        },
    ) {
        TitleWithSubtitle(
            title = stringResource(R.string.verify_title),
            subtitle = stringResource(R.string.verify_subtitle_prefix) + email,
            modifier =
                Modifier
                    .padding(top = 16.dp)
                    .staggeredAppear(1),
        )

        OtpField(
            value = uiState.code,
            onValueChange = onCodeChange,
            onImeDone = onConfirm,
            modifier =
                Modifier
                    .padding(top = 32.dp)
                    .staggeredAppear(2),
        )

        if (uiState.codeError != null) {
            Text(
                text = stringResource(uiState.codeError),
                color = AppTertiary,
                fontSize = 13.sp,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 4.dp),
            )
        }
    }
}

@Preview
@Composable
private fun VerifyResetCodePreview() {
    GameTrackrTheme {
        VerifyResetCodeContent(
            email = "you@email.com",
            uiState = VerifyResetCodeUiState(code = "123"),
            onBack = {},
            onCodeChange = {},
            onConfirm = {},
            onResend = {},
            onErrorShown = {},
        )
    }
}
