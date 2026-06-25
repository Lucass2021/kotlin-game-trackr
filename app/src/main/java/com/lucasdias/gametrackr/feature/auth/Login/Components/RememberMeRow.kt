package com.lucasdias.gametrackr.feature.auth.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary

@Composable
fun RememberMeRow(
    rememberMe: Boolean,
    onToggle: () -> Unit,
    onForgotPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (rememberMe) AppPrimary else Color.Transparent)
                .border(1.5.dp, if (rememberMe) AppPrimary else AppOutline, RoundedCornerShape(6.dp))
                .clickable(onClick = onToggle),
            contentAlignment = Alignment.Center
        ) {
            if (rememberMe) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = AppOnPrimary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = stringResource(R.string.login_remember_me),
            color = AppTextSecondary,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.login_forgot_password),
            color = AppSecondary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable(onClick = onForgotPassword)
        )
    }
}
