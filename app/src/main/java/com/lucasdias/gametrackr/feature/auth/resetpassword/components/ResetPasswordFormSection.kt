package com.lucasdias.gametrackr.feature.auth.resetpassword.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.AuthTextField
import com.lucasdias.gametrackr.core.ui.components.PasswordStrength
import com.lucasdias.gametrackr.core.ui.components.PasswordStrengthMeter
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.core.ui.theme.Spacing

@Composable
fun ResetPasswordFormSection(
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    passwordError: UiText?,
    confirmPasswordError: UiText?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            AuthTextField(
                label = stringResource(R.string.reset_new_password_label),
                placeholder = stringResource(R.string.register_password_placeholder),
                value = password,
                onValueChange = onPasswordChange,
                isPassword = true,
                keyboardType = KeyboardType.Password,
                error = passwordError,
            )
            if (password.isNotEmpty()) {
                PasswordStrengthMeter(strength = PasswordStrength.of(password))
            }
        }
        AuthTextField(
            label = stringResource(R.string.reset_confirm_password_label),
            placeholder = stringResource(R.string.register_password_placeholder),
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            isPassword = true,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            error = confirmPasswordError,
        )
    }
}

@Preview
@Composable
private fun ResetPasswordFormSectionPreview() {
    AppPreview {
        ResetPasswordFormSection(
            password = "secret123",
            onPasswordChange = {},
            confirmPassword = "secret",
            onConfirmPasswordChange = {},
            passwordError = null,
            confirmPasswordError = UiText.Resource(R.string.validation_passwords_dont_match),
        )
    }
}
