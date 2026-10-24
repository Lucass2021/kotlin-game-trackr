package com.lucasdias.gametrackr.feature.auth.forgotpassword.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.AuthTextField
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.text.UiText

@Composable
fun ForgotPasswordFormSection(
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: UiText?,
    modifier: Modifier = Modifier,
) {
    AuthTextField(
        label = stringResource(R.string.forgot_email_label),
        placeholder = stringResource(R.string.login_email_placeholder),
        value = email,
        onValueChange = onEmailChange,
        icon = AppIcon.ENVELOPE,
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Done,
        error = emailError,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun ForgotPasswordFormSectionPreview() {
    AppPreview {
        ForgotPasswordFormSection(
            email = "player",
            onEmailChange = {},
            emailError = UiText.Resource(R.string.validation_email_invalid),
        )
    }
}
