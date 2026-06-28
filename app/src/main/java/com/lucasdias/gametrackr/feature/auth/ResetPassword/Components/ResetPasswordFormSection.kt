package com.lucasdias.gametrackr.feature.auth.resetpassword.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AuthTextField
import com.lucasdias.gametrackr.core.ui.components.PasswordStrength
import com.lucasdias.gametrackr.core.ui.components.PasswordStrengthMeter

@Composable
fun ResetPasswordFormSection(
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    @StringRes passwordError: Int?,
    @StringRes confirmPasswordError: Int?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            AuthTextField(
                label = stringResource(R.string.reset_new_password_label),
                placeholder = stringResource(R.string.register_password_placeholder),
                value = password,
                onValueChange = onPasswordChange,
                isPassword = true,
                keyboardType = KeyboardType.Password,
                error = passwordError
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
            error = confirmPasswordError
        )
    }
}
