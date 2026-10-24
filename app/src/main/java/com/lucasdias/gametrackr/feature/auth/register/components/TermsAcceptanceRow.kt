package com.lucasdias.gametrackr.feature.auth.register.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.core.ui.text.asString
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val CHECKBOX_SIZE = 24.dp
private val CHECKBOX_BORDER_WIDTH = 1.5.dp
private val CHECK_ICON_SIZE = 16.dp

@Composable
fun TermsAcceptanceRow(
    accepted: Boolean,
    onToggle: () -> Unit,
    error: UiText?,
    modifier: Modifier = Modifier,
) {
    val borderColor =
        when {
            error != null -> AppTertiary
            accepted -> AppPrimary
            else -> AppOutline
        }
    val checkboxDescription = stringResource(R.string.register_terms_checkbox)
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier =
                    Modifier
                        .size(CHECKBOX_SIZE)
                        .clip(RoundedCornerShape(Radius.sm))
                        .background(if (accepted) AppPrimary else Color.Transparent)
                        .border(CHECKBOX_BORDER_WIDTH, borderColor, RoundedCornerShape(Radius.sm))
                        .toggleable(value = accepted, role = Role.Checkbox, onValueChange = { onToggle() })
                        .semantics { contentDescription = checkboxDescription },
                contentAlignment = Alignment.Center,
            ) {
                if (accepted) {
                    Icon(
                        imageVector = AppIcon.CHECK.image(),
                        contentDescription = null,
                        tint = AppOnPrimary,
                        modifier = Modifier.size(CHECK_ICON_SIZE),
                    )
                }
            }
            Spacer(modifier = Modifier.size(Spacing.sm))
            TermsText()
        }
        if (error != null) {
            Spacer(modifier = Modifier.size(Spacing.xs))
            Text(text = error.asString(), color = AppTertiary, fontSize = 13.sp)
        }
    }
}

@Composable
private fun TermsText() {
    val linkStyles =
        TextLinkStyles(
            style = SpanStyle(color = AppSecondary, fontWeight = FontWeight.SemiBold),
        )
    val url = stringResource(R.string.welcome_terms_url)
    val text =
        buildAnnotatedString {
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

@Preview
@Composable
private fun TermsAcceptanceRowPreview() {
    AppPreview {
        TermsAcceptanceRow(accepted = true, onToggle = {}, error = null)
    }
}

@Preview
@Composable
private fun TermsAcceptanceRowErrorPreview() {
    AppPreview {
        TermsAcceptanceRow(
            accepted = false,
            onToggle = {},
            error = UiText.Resource(R.string.validation_terms_required),
        )
    }
}
