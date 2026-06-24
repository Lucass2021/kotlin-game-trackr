package com.lucasdias.gametrackr.feature.auth.register.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary

@Composable
fun RegisterBottomSection(
    isLoading: Boolean,
    onCreateAccount: () -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrimaryButton(
            text = stringResource(R.string.register_create_account),
            onClick = onCreateAccount,
            isLoading = isLoading
        )
        Row {
            Text(
                text = stringResource(R.string.register_have_account),
                color = AppTextSecondary,
                fontSize = 15.sp
            )
            Text(
                text = stringResource(R.string.register_sign_in),
                color = AppSecondary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(onClick = onSignIn)
            )
        }
    }
}
