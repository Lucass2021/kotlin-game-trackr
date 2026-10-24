package com.lucasdias.gametrackr.feature.app.help.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.GROUPED_ICON_BOX_WIDTH
import com.lucasdias.gametrackr.core.ui.components.GroupedSection
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Durations
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.app.help.HelpFaq

private val ICON_SIZE = 20.dp
private val CARET_SIZE = 14.dp
private val QUESTION_SIZE = 16.sp
private val ANSWER_SIZE = 14.sp
private const val EXPANDED_ROTATION = 180f
private val ANSWER_INSET = Spacing.md + GROUPED_ICON_BOX_WIDTH + Spacing.sm

@Composable
fun HelpFaqRow(
    faq: HelpFaq,
    isExpanded: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .pressScale(interactionSource)
                .clickable(interactionSource = interactionSource, indication = null, role = Role.Button, onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(Spacing.md),
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.width(GROUPED_ICON_BOX_WIDTH), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = AppIcon.HELP.image(),
                    contentDescription = null,
                    tint = AppPrimary,
                    modifier = Modifier.size(ICON_SIZE),
                )
            }
            Text(
                text = stringResource(faq.questionRes),
                color = AppTextPrimary,
                style = AppType.label(QUESTION_SIZE),
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = AppIcon.CARET_DOWN.image(),
                contentDescription = null,
                tint = AppTextSecondary,
                modifier = Modifier.size(CARET_SIZE).rotate(if (isExpanded) EXPANDED_ROTATION else 0f),
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(tween(Durations.SHORT)),
            exit = shrinkVertically(tween(Durations.SHORT)),
        ) {
            Text(
                text = stringResource(faq.answerRes),
                color = AppTextSecondary,
                style = AppType.body(ANSWER_SIZE),
                modifier = Modifier.padding(start = ANSWER_INSET, end = Spacing.md, bottom = Spacing.md),
            )
        }
    }
}

@Preview
@Composable
private fun HelpFaqRowPreview() {
    AppPreview {
        GroupedSection(title = null) {
            HelpFaqRow(faq = HelpFaq.ADD_GAME, isExpanded = true, onClick = {})
        }
    }
}
