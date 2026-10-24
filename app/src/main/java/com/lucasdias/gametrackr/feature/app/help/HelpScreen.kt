package com.lucasdias.gametrackr.feature.app.help

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.GroupedDivider
import com.lucasdias.gametrackr.core.ui.components.GroupedNavRow
import com.lucasdias.gametrackr.core.ui.components.GroupedSection
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.app.help.components.HelpFaqRow
import com.lucasdias.gametrackr.feature.app.help.components.HelpHero
import com.lucasdias.gametrackr.feature.app.stats.components.StatsTopBar

@Composable
fun HelpScreen(onBack: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val contactUrl = stringResource(R.string.welcome_terms_url)

    HelpContent(
        actions =
            HelpActions(
                onBack = onBack,
                onSendFeedback = { uriHandler.openUri(contactUrl) },
                onReportBug = { uriHandler.openUri(contactUrl) },
            ),
    )
}

@Composable
fun HelpContent(
    actions: HelpActions,
    modifier: Modifier = Modifier,
    initiallyExpanded: HelpFaq? = null,
) {
    var expanded by rememberSaveable { mutableStateOf(initiallyExpanded) }

    Column(modifier = modifier.fillMaxSize().background(AppBackground)) {
        StatsTopBar(title = stringResource(R.string.help_title), onBack = actions.onBack)

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Spacing.lg)
                    .padding(top = Spacing.sm, bottom = Spacing.xxl),
            verticalArrangement = Arrangement.spacedBy(Spacing.xl),
        ) {
            HelpHero()

            GroupedSection(title = stringResource(R.string.help_section_contact)) {
                GroupedNavRow(icon = AppIcon.ENVELOPE, title = stringResource(R.string.help_send_feedback), onClick = actions.onSendFeedback)
                GroupedDivider()
                GroupedNavRow(icon = AppIcon.COMMENT, title = stringResource(R.string.help_report_bug), onClick = actions.onReportBug)
            }

            GroupedSection(title = stringResource(R.string.help_section_faq)) {
                HelpFaq.entries.forEachIndexed { index, faq ->
                    HelpFaqRow(
                        faq = faq,
                        isExpanded = expanded == faq,
                        onClick = { expanded = if (expanded == faq) null else faq },
                    )
                    if (index < HelpFaq.entries.lastIndex) GroupedDivider()
                }
            }
        }
    }
}

@Preview
@Composable
private fun HelpContentPreview() {
    AppPreview {
        HelpContent(actions = HelpActions(), initiallyExpanded = HelpFaq.PRIVACY)
    }
}
