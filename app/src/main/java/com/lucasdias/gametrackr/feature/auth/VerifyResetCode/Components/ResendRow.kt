package com.lucasdias.gametrackr.feature.auth.verifyresetcode.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary

@Composable
fun ResendRow(
    seconds: Int,
    onResend: () -> Unit,
    modifier: Modifier = Modifier
) {
    val canResend = seconds <= 0
    val text = buildAnnotatedString {
        append(stringResource(R.string.verify_didnt_get))
        if (canResend) {
            withStyle(SpanStyle(color = AppSecondary, fontWeight = FontWeight.SemiBold)) {
                append(stringResource(R.string.verify_resend))
            }
        } else {
            append(stringResource(R.string.verify_resend_in, seconds))
        }
    }

    Text(
        text = text,
        color = AppTextSecondary,
        fontSize = 15.sp,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .then(if (canResend) Modifier.clickable(onClick = onResend) else Modifier)
    )
}
