package com.lucasdias.gametrackr.feature.auth.welcome.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.components.SecondaryButton
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import androidx.compose.ui.res.stringResource

@Composable
fun WelcomeBottomSection(
    onCreateAccount: () -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrimaryButton(
            text = stringResource(R.string.welcome_create_account),
            onClick = onCreateAccount
        )
        SecondaryButton(
            text = stringResource(R.string.welcome_sign_in),
            onClick = onSignIn
        )

        val linkStyles = TextLinkStyles(style = SpanStyle(color = AppSecondary))
        val termsUrl = stringResource(R.string.welcome_terms_url)
        val terms = buildAnnotatedString {
            append(stringResource(R.string.welcome_terms_prefix))
            withLink(LinkAnnotation.Url(termsUrl, linkStyles)) {
                append(stringResource(R.string.welcome_terms))
            }
            append(stringResource(R.string.welcome_terms_and))
            withLink(LinkAnnotation.Url(termsUrl, linkStyles)) {
                append(stringResource(R.string.welcome_privacy))
            }
            append(".")
        }
        Text(
            text = terms,
            color = AppTextSecondary,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}
