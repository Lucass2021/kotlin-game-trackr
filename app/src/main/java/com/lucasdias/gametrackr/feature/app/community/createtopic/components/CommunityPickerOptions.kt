package com.lucasdias.gametrackr.feature.app.community.createtopic.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.components.CommunityIcon

private val ICON_SIZE = 26.dp
private val SHADOW_ELEVATION = 16.dp
private val BORDER_WIDTH = 1.dp
private val NAME_SIZE = 16.sp
private val SUBTITLE_SIZE = 12.sp
private val MESSAGE_SIZE = 14.sp

@Composable
fun CommunityPickerOptions(
    selection: Community?,
    options: List<Community>,
    isLoading: Boolean,
    isFailed: Boolean,
    onSelect: (Community) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(Radius.md)
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .shadow(SHADOW_ELEVATION, shape)
                .clip(shape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppOutline, shape),
    ) {
        when {
            isFailed -> {
                FailedRow(onRetry = onRetry)
            }

            isLoading -> {
                MessageRow(text = stringResource(R.string.create_topic_communities_loading))
            }

            options.isEmpty() -> {
                MessageRow(text = stringResource(R.string.create_topic_communities_empty))
            }

            else -> {
                options.forEachIndexed { index, option ->
                    OptionRow(option = option, isSelected = option.id == selection?.id, onClick = { onSelect(option) })
                    if (index < options.lastIndex) HorizontalDivider(color = AppOutline)
                }
            }
        }
    }
}

@Composable
private fun OptionRow(
    option: Community,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
                .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        CommunityIcon(start = option.iconStart, end = option.iconEnd, size = ICON_SIZE, cornerRadius = Radius.sm)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = option.name,
                color = if (isSelected) AppPrimary else AppTextPrimary,
                style = AppType.body(NAME_SIZE),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = option.subtitle,
                color = AppTextSecondary,
                style = AppType.body(SUBTITLE_SIZE),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun MessageRow(text: String) {
    Text(
        text = text,
        color = AppTextSecondary,
        style = AppType.body(MESSAGE_SIZE),
        modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.sm, vertical = Spacing.md),
    )
}

@Composable
private fun FailedRow(onRetry: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = Spacing.sm, end = Spacing.xxs, top = Spacing.xxs, bottom = Spacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.create_topic_communities_error),
            color = AppTextSecondary,
            style = AppType.body(MESSAGE_SIZE),
            modifier = Modifier.weight(1f),
        )
        TextButton(onClick = onRetry) {
            Text(text = stringResource(R.string.create_topic_try_again), color = AppPrimary, style = AppType.label(MESSAGE_SIZE))
        }
    }
}

@Preview
@Composable
private fun CommunityPickerOptionsPreview() {
    AppPreview {
        CommunityPickerOptions(
            selection = null,
            options = CommunityMockData.all.filter { it.isJoined },
            isLoading = false,
            isFailed = false,
            onSelect = {},
            onRetry = {},
        )
    }
}

@Preview
@Composable
private fun CommunityPickerOptionsFailedPreview() {
    AppPreview {
        CommunityPickerOptions(selection = null, options = emptyList(), isLoading = false, isFailed = true, onSelect = {}, onRetry = {})
    }
}
