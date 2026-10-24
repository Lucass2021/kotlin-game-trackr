package com.lucasdias.gametrackr.feature.auth.verifyresetcode

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.anim.staggeredAppear
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.AuthScreenScaffold
import com.lucasdias.gametrackr.core.ui.components.OtpField
import com.lucasdias.gametrackr.core.ui.components.TitleWithSubtitle
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.text.asString
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.auth.verifyresetcode.VerifyResetCodeUiState.Companion.CODE_LENGTH
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
    val currentOnVerified by rememberUpdatedState(onVerified)

    LaunchedEffect(uiState.verifiedCode) {
        val code = uiState.verifiedCode ?: return@LaunchedEffect
        currentOnVerified(code)
        viewModel.onVerifiedHandled()
    }

    VerifyResetCodeContent(
        email = email,
        uiState = uiState,
        actions =
            VerifyResetCodeActions(
                onBack = onBack,
                onCodeChange = viewModel::onCodeChange,
                onSubmit = viewModel::onSubmit,
                onResend = viewModel::onResend,
                onToastShown = viewModel::onToastShown,
            ),
    )
}

@Composable
private fun VerifyResetCodeContent(
    email: String,
    uiState: VerifyResetCodeUiState,
    actions: VerifyResetCodeActions,
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
        bottomBar = {
            VerifyResetCodeBottomSection(
                seconds = uiState.resendSeconds,
                onResend = actions.onResend,
                isLoading = uiState.isLoading,
                onConfirm = actions.onSubmit,
                modifier = Modifier.staggeredAppear(3),
            )
        },
    ) {
        TitleWithSubtitle(
            title = stringResource(R.string.verify_title),
            subtitle = stringResource(R.string.verify_subtitle_prefix) + email,
            modifier =
                Modifier
                    .padding(top = Spacing.md)
                    .staggeredAppear(1),
        )

        OtpField(
            value = uiState.code,
            onValueChange = actions.onCodeChange,
            length = CODE_LENGTH,
            onImeDone = actions.onSubmit,
            modifier =
                Modifier
                    .padding(top = Spacing.xxl)
                    .staggeredAppear(2),
        )

        uiState.codeError?.let { error ->
            Text(
                text = error.asString(),
                color = AppTertiary,
                fontSize = 13.sp,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.sm, start = Spacing.xxs),
            )
        }
    }
}

@Preview
@Composable
private fun VerifyResetCodePreview() {
    AppPreview {
        VerifyResetCodeContent(
            email = "you@email.com",
            uiState = VerifyResetCodeUiState(code = "123"),
            actions = VerifyResetCodeActions(),
        )
    }
}

@Preview
@Composable
private fun VerifyResetCodeErrorPreview() {
    AppPreview {
        VerifyResetCodeContent(
            email = "you@email.com",
            uiState = VerifyResetCodeUiState(code = "123", isSubmitted = true, resendSeconds = 0),
            actions = VerifyResetCodeActions(),
        )
    }
}
