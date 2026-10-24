package com.lucasdias.gametrackr.feature.auth.forgotpassword.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.Spacing

@Composable
fun ForgotPasswordBottomSection(
    isLoading: Boolean,
    onSendCode: () -> Unit,
    onBackToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PrimaryButton(
            text = stringResource(R.string.forgot_send_code),
            onClick = onSendCode,
            isLoading = isLoading,
        )
        Text(
            text = stringResource(R.string.forgot_back_to_login),
            color = AppSecondary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable(role = Role.Button, onClick = onBackToLogin),
        )
    }
}

@Preview
@Composable
private fun ForgotPasswordBottomSectionPreview() {
    AppPreview {
        ForgotPasswordBottomSection(isLoading = false, onSendCode = {}, onBackToLogin = {})
    }
}
