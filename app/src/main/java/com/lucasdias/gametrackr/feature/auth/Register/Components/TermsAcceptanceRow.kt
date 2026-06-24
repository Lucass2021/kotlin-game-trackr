package com.lucasdias.gametrackr.feature.auth.register.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary

@Composable
fun TermsAcceptanceRow(
    accepted: Boolean,
    onToggle: () -> Unit,
    @StringRes error: Int?,
    modifier: Modifier = Modifier
) {
    val borderColor = when {
        error != null -> AppTertiary
        accepted -> AppPrimary
        else -> AppOutline
    }
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (accepted) AppPrimary else Color.Transparent)
                    .border(1.5.dp, borderColor, RoundedCornerShape(6.dp))
                    .clickable(onClick = onToggle),
                contentAlignment = Alignment.Center
            ) {
                if (accepted) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = AppOnPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(12.dp))
            TermsText()
        }
        if (error != null) {
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = stringResource(error), color = AppTertiary, fontSize = 13.sp)
        }
    }
}

@Composable
private fun TermsText() {
    val linkStyles = TextLinkStyles(
        style = SpanStyle(color = AppSecondary, fontWeight = FontWeight.SemiBold)
    )
    val url = stringResource(R.string.welcome_terms_url)
    val text = buildAnnotatedString {
        append(stringResource(R.string.register_terms_prefix))
        withLink(LinkAnnotation.Url(url, linkStyles)) {
            append(stringResource(R.string.register_terms))
        }
        append(stringResource(R.string.register_terms_and))
        withLink(LinkAnnotation.Url(url, linkStyles)) {
            append(stringResource(R.string.register_privacy))
        }
        append(".")
    }
    Text(text = text, color = AppTextSecondary, fontSize = 14.sp, lineHeight = 19.sp)
}
