package com.lucasdias.gametrackr.feature.auth.login.components

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
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.core.ui.theme.Spacing

@Composable
fun LoginFormSection(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    emailError: UiText?,
    passwordError: UiText?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
    ) {
        AuthTextField(
            label = stringResource(R.string.login_email_label),
            placeholder = stringResource(R.string.login_email_placeholder),
            value = email,
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            error = emailError,
        )
        AuthTextField(
            label = stringResource(R.string.login_password_label),
            placeholder = stringResource(R.string.login_password_placeholder),
            value = password,
            onValueChange = onPasswordChange,
            isPassword = true,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            error = passwordError,
        )
    }
}

@Preview
@Composable
private fun LoginFormSectionPreview() {
    AppPreview {
        LoginFormSection(
            email = "player@gametrackr.com",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            emailError = null,
            passwordError = UiText.Resource(R.string.validation_password_required),
        )
    }
}
