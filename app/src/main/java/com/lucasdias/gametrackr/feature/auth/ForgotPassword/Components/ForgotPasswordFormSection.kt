package com.lucasdias.gametrackr.feature.auth.forgotpassword.components

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AuthTextField

@Composable
fun ForgotPasswordFormSection(
    email: String,
    onEmailChange: (String) -> Unit,
    @StringRes emailError: Int?,
    modifier: Modifier = Modifier,
) {
    AuthTextField(
        label = stringResource(R.string.forgot_email_label),
        placeholder = stringResource(R.string.login_email_placeholder),
        value = email,
        onValueChange = onEmailChange,
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Done,
        error = emailError,
        modifier = modifier,
    )
}
