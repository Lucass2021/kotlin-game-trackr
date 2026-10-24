package com.lucasdias.gametrackr.feature.auth.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.AuthPrompt
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.theme.Spacing

@Composable
fun RegisterBottomSection(
    isLoading: Boolean,
    onCreateAccount: () -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
    ) {
        PrimaryButton(
            text = stringResource(R.string.register_create_account),
            onClick = onCreateAccount,
            isLoading = isLoading,
        )
        AuthPrompt(
            prefix = stringResource(R.string.register_have_account),
            actionText = stringResource(R.string.register_sign_in),
            onClick = onSignIn,
        )
    }
}

@Preview
@Composable
private fun RegisterBottomSectionPreview() {
    AppPreview {
        RegisterBottomSection(isLoading = false, onCreateAccount = {}, onSignIn = {})
    }
}
