package com.lucasdias.gametrackr.feature.auth.register.components

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
import com.lucasdias.gametrackr.feature.auth.register.RegisterFormState

@Composable
fun RegisterFormSection(form: RegisterFormState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        AuthTextField(
            label = stringResource(R.string.register_name_label),
            placeholder = stringResource(R.string.register_name_placeholder),
            value = form.name,
            onValueChange = { form.name = it },
            error = form.nameError
        )
        AuthTextField(
            label = stringResource(R.string.register_email_label),
            placeholder = stringResource(R.string.register_email_placeholder),
            value = form.email,
            onValueChange = { form.email = it },
            keyboardType = KeyboardType.Email,
            error = form.emailError
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            AuthTextField(
                label = stringResource(R.string.register_password_label),
                placeholder = stringResource(R.string.register_password_placeholder),
                value = form.password,
                onValueChange = { form.password = it },
                isPassword = true,
                keyboardType = KeyboardType.Password,
                error = form.passwordError
            )
            if (form.password.isNotEmpty()) {
                PasswordStrengthMeter(strength = PasswordStrength.of(form.password))
            }
        }
        AuthTextField(
            label = stringResource(R.string.register_confirm_password_label),
            placeholder = stringResource(R.string.register_password_placeholder),
            value = form.confirmPassword,
            onValueChange = { form.confirmPassword = it },
            isPassword = true,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            error = form.confirmPasswordError
        )
    }
}
