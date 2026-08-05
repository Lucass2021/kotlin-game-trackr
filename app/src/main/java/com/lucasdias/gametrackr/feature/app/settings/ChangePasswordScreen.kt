package com.lucasdias.gametrackr.feature.app.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AuthTextField
import com.lucasdias.gametrackr.core.ui.components.PasswordStrength
import com.lucasdias.gametrackr.core.ui.components.PasswordStrengthMeter
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.feature.app.stats.components.StatsTopBar

@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var submitted by rememberSaveable { mutableStateOf(false) }

    val strength = PasswordStrength.of(newPassword)

    val currentError =
        if (submitted && currentPassword.isEmpty()) R.string.change_password_error_current_required else null
    val newError =
        when {
            submitted && newPassword.isEmpty() -> R.string.change_password_error_new_required
            submitted && newPassword.length < 6 -> R.string.validation_password_too_short
            else -> null
        }
    val confirmError =
        when {
            submitted && confirmPassword.isEmpty() -> {
                R.string.change_password_error_confirm_required
            }

            submitted && confirmPassword.isNotEmpty() && confirmPassword != newPassword -> {
                R.string.validation_passwords_dont_match
            }

            else -> {
                null
            }
        }

    val canSave = currentPassword.isNotEmpty() && newPassword.length >= 6 && confirmPassword == newPassword

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(AppBackground),
    ) {
        StatsTopBar(
            title = stringResource(R.string.change_password_title),
            onBack = onBack,
        )

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
        ) {
            AuthTextField(
                label = stringResource(R.string.change_password_current_label),
                placeholder = stringResource(R.string.change_password_placeholder),
                value = currentPassword,
                onValueChange = { currentPassword = it },
                isPassword = true,
                keyboardType = KeyboardType.Password,
                error = currentError,
            )

            Spacer(modifier = Modifier.height(22.dp))

            AuthTextField(
                label = stringResource(R.string.change_password_new_label),
                placeholder = stringResource(R.string.change_password_placeholder),
                value = newPassword,
                onValueChange = { newPassword = it },
                isPassword = true,
                keyboardType = KeyboardType.Password,
                error = newError,
            )

            if (newPassword.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                PasswordStrengthMeter(strength = strength)
            }

            Spacer(modifier = Modifier.height(22.dp))

            AuthTextField(
                label = stringResource(R.string.change_password_confirm_label),
                placeholder = stringResource(R.string.change_password_placeholder),
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                isPassword = true,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                error = confirmError,
            )
        }

        PrimaryButton(
            text = stringResource(R.string.change_password_save),
            onClick = {
                submitted = true
                if (canSave) onSuccess()
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp)
                    .navigationBarsPadding()
                    .alpha(if (canSave) 1f else 0.45f),
        )
    }
}
