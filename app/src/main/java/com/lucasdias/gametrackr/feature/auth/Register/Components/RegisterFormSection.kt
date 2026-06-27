package com.lucasdias.gametrackr.feature.auth.register.components

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
fun RegisterFormSection(
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    @StringRes nameError: Int?,
    @StringRes emailError: Int?,
    @StringRes passwordError: Int?,
    @StringRes confirmPasswordError: Int?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        AuthTextField(
            label = stringResource(R.string.register_name_label),
            placeholder = stringResource(R.string.register_name_placeholder),
            value = name,
            onValueChange = onNameChange,
            error = nameError
        )
        AuthTextField(
            label = stringResource(R.string.register_email_label),
            placeholder = stringResource(R.string.register_email_placeholder),
            value = email,
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            error = emailError
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            AuthTextField(
                label = stringResource(R.string.register_password_label),
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
            label = stringResource(R.string.register_confirm_password_label),
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
