package com.lucasdias.gametrackr.feature.auth.verifyresetcode.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.AuthPrompt
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary

@Composable
fun ResendRow(
    seconds: Int,
    onResend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (seconds <= 0) {
        AuthPrompt(
            prefix = stringResource(R.string.verify_didnt_get),
            actionText = stringResource(R.string.verify_resend),
            onClick = onResend,
            modifier = modifier,
        )
    } else {
        Text(
            text = stringResource(R.string.verify_didnt_get) + stringResource(R.string.verify_resend_in, seconds),
            color = AppTextSecondary,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun ResendRowCountdownPreview() {
    AppPreview {
        ResendRow(seconds = 12, onResend = {})
    }
}

@Preview
@Composable
private fun ResendRowReadyPreview() {
    AppPreview {
        ResendRow(seconds = 0, onResend = {})
    }
}
