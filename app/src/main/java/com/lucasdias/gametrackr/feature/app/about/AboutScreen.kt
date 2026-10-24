package com.lucasdias.gametrackr.feature.app.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.BuildConfig
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.GroupedDivider
import com.lucasdias.gametrackr.core.ui.components.GroupedNavRow
import com.lucasdias.gametrackr.core.ui.components.GroupedSection
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.text.asString
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.app.about.components.AboutHero
import com.lucasdias.gametrackr.feature.app.stats.components.StatsTopBar

private val CREDITS_SIZE = 13.sp

@Composable
fun AboutScreen(onBack: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val legalUrl = stringResource(R.string.welcome_terms_url)

    AboutContent(
        state = AboutUiState(versionName = BuildConfig.VERSION_NAME),
        actions =
            AboutActions(
                onBack = onBack,
                onOpenTerms = { uriHandler.openUri(legalUrl) },
                onOpenPrivacy = { uriHandler.openUri(legalUrl) },
            ),
    )
}

@Composable
fun AboutContent(
    state: AboutUiState,
    actions: AboutActions,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(AppBackground)) {
        StatsTopBar(title = stringResource(R.string.about_title), onBack = actions.onBack)

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Spacing.lg)
                    .padding(top = Spacing.xl, bottom = Spacing.xxl),
            verticalArrangement = Arrangement.spacedBy(Spacing.xxl),
        ) {
            AboutHero(versionText = state.versionText.asString())
            GroupedSection(title = stringResource(R.string.about_section_legal)) {
                GroupedNavRow(icon = AppIcon.SHIELD_CHECK, title = stringResource(R.string.about_terms), onClick = actions.onOpenTerms)
                GroupedDivider()
                GroupedNavRow(icon = AppIcon.SHIELD_CHECK, title = stringResource(R.string.about_privacy), onClick = actions.onOpenPrivacy)
            }
        }

        Text(
            text = stringResource(R.string.about_credits),
            color = AppTextSecondary,
            style = AppType.body(CREDITS_SIZE),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(bottom = Spacing.md),
        )
    }
}

@Preview
@Composable
private fun AboutContentPreview() {
    AppPreview {
        AboutContent(state = AboutUiState(versionName = "1.0"), actions = AboutActions())
    }
}
