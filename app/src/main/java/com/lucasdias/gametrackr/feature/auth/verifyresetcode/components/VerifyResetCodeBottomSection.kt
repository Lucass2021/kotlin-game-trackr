package com.lucasdias.gametrackr.feature.auth.verifyresetcode.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.theme.Spacing

@Composable
fun VerifyResetCodeBottomSection(
    seconds: Int,
    onResend: () -> Unit,
    isLoading: Boolean,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ResendRow(seconds = seconds, onResend = onResend)
        PrimaryButton(
            text = stringResource(R.string.verify_confirm),
            onClick = onConfirm,
            isLoading = isLoading,
        )
    }
}

@Preview
@Composable
private fun VerifyResetCodeBottomSectionPreview() {
    AppPreview {
        VerifyResetCodeBottomSection(seconds = 12, onResend = {}, isLoading = false, onConfirm = {})
    }
}
