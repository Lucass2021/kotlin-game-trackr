package com.lucasdias.gametrackr.feature.app.about

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.appshell.components.DetailTopBar

@Composable
fun AboutScreen(onBack: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val githubUrl = stringResource(R.string.welcome_terms_url)

    Column(
        modifier = Modifier.fillMaxSize().background(AppBackground),
    ) {
        DetailTopBar(title = stringResource(R.string.about_title), onBack = onBack)

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            HeroSection()
            LegalSection(onOpenUrl = { uriHandler.openUri(githubUrl) })
        }

        Text(
            text = stringResource(R.string.about_credits),
            color = AppTextSecondary,
            style = AppType.body(13.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        )
    }
}

@Composable
private fun HeroSection() {
    Surface(color = AppSurfaceCard, shape = RoundedCornerShape(16.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Icon(
                imageVector = AppIcon.BRAND.image(filled = true),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(64.dp),
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    color = AppTextPrimary,
                    style = AppType.headline(28.sp),
                )
                Text(
                    text = stringResource(R.string.about_version),
                    color = AppTextSecondary,
                    style = AppType.body(14.sp),
                )
            }

            Text(
                text = stringResource(R.string.about_description),
                color = AppTextSecondary,
                style = AppType.body(15.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
private fun LegalSection(onOpenUrl: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = stringResource(R.string.about_section_legal),
            color = AppTextSecondary,
            style = AppType.label(13.sp),
            modifier = Modifier.padding(start = 4.dp),
        )

        Surface(color = AppSurfaceCard, shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                NavRow(icon = AppIcon.SHIELD_CHECK, title = stringResource(R.string.about_terms), onClick = onOpenUrl)
                HorizontalDivider(
                    thickness = 1.dp,
                    color = AppOutline,
                    modifier = Modifier.padding(start = 54.dp),
                )
                NavRow(icon = AppIcon.SHIELD_CHECK, title = stringResource(R.string.about_privacy), onClick = onOpenUrl)
            }
        }
    }
}

@Composable
private fun NavRow(
    icon: AppIcon,
    title: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button,
                    onClick = onClick,
                ).padding(horizontal = 16.dp, vertical = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.width(24.dp), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon.image(),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(20.dp),
            )
        }
        Text(
            text = title,
            color = AppTextPrimary,
            style = AppType.label(16.sp),
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = AppIcon.FORWARD.image(),
            contentDescription = null,
            tint = AppTextSecondary,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Preview
@Composable
private fun AboutScreenPreview() {
    AboutScreen(onBack = {})
}
