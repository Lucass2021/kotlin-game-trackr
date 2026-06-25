package com.lucasdias.gametrackr.feature.auth.login.components

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
import com.lucasdias.gametrackr.feature.auth.login.LoginFormState

@Composable
fun LoginFormSection(form: LoginFormState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        AuthTextField(
            label = stringResource(R.string.login_email_label),
            placeholder = stringResource(R.string.login_email_placeholder),
            value = form.email,
            onValueChange = { form.email = it },
            keyboardType = KeyboardType.Email,
            error = form.emailError
        )
        AuthTextField(
            label = stringResource(R.string.login_password_label),
            placeholder = stringResource(R.string.login_password_placeholder),
            value = form.password,
            onValueChange = { form.password = it },
            isPassword = true,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            error = form.passwordError
        )
    }
}
