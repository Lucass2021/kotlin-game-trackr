package com.lucasdias.gametrackr.feature.app.help

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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

private data class FaqItem(
    val question: String,
    val answer: String,
)

@Composable
fun HelpScreen(onBack: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val githubUrl = stringResource(R.string.welcome_terms_url)

    val faqItems =
        listOf(
            FaqItem(
                question = stringResource(R.string.help_faq_1_question),
                answer = stringResource(R.string.help_faq_1_answer),
            ),
            FaqItem(
                question = stringResource(R.string.help_faq_2_question),
                answer = stringResource(R.string.help_faq_2_answer),
            ),
            FaqItem(
                question = stringResource(R.string.help_faq_3_question),
                answer = stringResource(R.string.help_faq_3_answer),
            ),
            FaqItem(
                question = stringResource(R.string.help_faq_4_question),
                answer = stringResource(R.string.help_faq_4_answer),
            ),
        )

    var expandedIndex by rememberSaveable { mutableIntStateOf(-1) }

    Column(
        modifier = Modifier.fillMaxSize().background(AppBackground),
    ) {
        DetailTopBar(title = stringResource(R.string.help_title), onBack = onBack)

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            HeroSection()

            SectionCard(title = stringResource(R.string.help_section_contact)) {
                NavRow(
                    icon = AppIcon.ENVELOPE,
                    title = stringResource(R.string.help_send_feedback),
                    onClick = { uriHandler.openUri(githubUrl) },
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = AppOutline,
                    modifier = Modifier.padding(start = 54.dp),
                )
                NavRow(
                    icon = AppIcon.COMMENT,
                    title = stringResource(R.string.help_report_bug),
                    onClick = { uriHandler.openUri(githubUrl) },
                )
            }

            SectionCard(title = stringResource(R.string.help_section_faq)) {
                faqItems.forEachIndexed { index, item ->
                    FaqRow(
                        question = item.question,
                        answer = item.answer,
                        isExpanded = expandedIndex == index,
                        onClick = { expandedIndex = if (expandedIndex == index) -1 else index },
                    )
                    if (index < faqItems.lastIndex) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = AppOutline,
                            modifier = Modifier.padding(start = 54.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroSection() {
    Surface(color = AppSurfaceCard, shape = RoundedCornerShape(16.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = AppIcon.HELP.image(filled = true),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(48.dp),
            )

            Text(
                text = stringResource(R.string.help_hero_title),
                color = AppTextPrimary,
                style = AppType.headline(22.sp),
            )

            Text(
                text = stringResource(R.string.help_hero_subtitle),
                color = AppTextSecondary,
                style = AppType.body(15.sp),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            color = AppTextSecondary,
            style = AppType.label(13.sp),
            modifier = Modifier.padding(start = 4.dp),
        )

        Surface(color = AppSurfaceCard, shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
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

@Composable
private fun FaqRow(
    question: String,
    answer: String,
    isExpanded: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button,
                    onClick = onClick,
                ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.width(24.dp), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = AppIcon.HELP.image(),
                    contentDescription = null,
                    tint = AppPrimary,
                    modifier = Modifier.size(20.dp),
                )
            }
            Text(
                text = question,
                color = AppTextPrimary,
                style = AppType.label(16.sp),
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = AppIcon.CARET_DOWN.image(),
                contentDescription = null,
                tint = AppTextSecondary,
                modifier = Modifier.size(14.dp).rotate(if (isExpanded) 180f else 0f),
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Text(
                text = answer,
                color = AppTextSecondary,
                style = AppType.body(14.sp),
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp)
                        .padding(start = 38.dp, bottom = 15.dp),
            )
        }
    }
}

@Preview
@Composable
private fun HelpScreenPreview() {
    HelpScreen(onBack = {})
}
