package com.lucasdias.gametrackr.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary

@Composable
fun AuthPrompt(
    prefix: String,
    actionText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button,
                    onClick = onClick,
                ),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = prefix,
            color = AppTextSecondary,
            fontSize = 15.sp,
        )
        Text(
            text = actionText,
            color = AppSecondary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview
@Composable
private fun AuthPromptPreview() {
    AppPreview {
        AuthPrompt(
            prefix = stringResource(R.string.login_no_account),
            actionText = stringResource(R.string.login_sign_up),
            onClick = {},
        )
    }
}
