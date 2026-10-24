package com.lucasdias.gametrackr.feature.app.about.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val LOGO_SIZE = 64.dp
private val NAME_SIZE = 28.sp
private val VERSION_SIZE = 14.sp
private val DESCRIPTION_SIZE = 15.sp

@Composable
fun AboutHero(
    versionText: String,
    modifier: Modifier = Modifier,
) {
    Surface(color = AppSurfaceCard, shape = RoundedCornerShape(Radius.lg), modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = Spacing.xl, horizontal = Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            Icon(
                imageVector = AppIcon.BRAND.image(filled = true),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(LOGO_SIZE),
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
            ) {
                Text(text = stringResource(R.string.app_name), color = AppTextPrimary, style = AppType.headline(NAME_SIZE))
                Text(text = versionText, color = AppTextSecondary, style = AppType.body(VERSION_SIZE))
            }
            Text(
                text = stringResource(R.string.about_description),
                color = AppTextSecondary,
                style = AppType.body(DESCRIPTION_SIZE),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Spacing.md),
            )
        }
    }
}

@Preview
@Composable
private fun AboutHeroPreview() {
    AppPreview {
        AboutHero(versionText = stringResource(R.string.about_version, "1.0"))
    }
}
