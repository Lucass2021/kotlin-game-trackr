package com.lucasdias.gametrackr.feature.app.notifications.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.NotificationFilter
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val BORDER_WIDTH = 1.dp
private val LABEL_SIZE = 14.sp

@Composable
fun NotificationFilterTabs(
    selected: NotificationFilter,
    onSelect: (NotificationFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = Spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        NotificationFilter.entries.forEach { filter ->
            FilterChip(
                label = stringResource(filter.label),
                isSelected = filter == selected,
                onClick = { onSelect(filter) },
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val base =
        Modifier
            .pressScale(interactionSource)
            .clip(CircleShape)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
    val styled =
        if (isSelected) {
            base.background(AppPrimary)
        } else {
            base.border(BorderStroke(BORDER_WIDTH, AppOutline), CircleShape)
        }
    Text(
        text = label,
        color = if (isSelected) AppOnPrimary else AppTextPrimary,
        fontSize = LABEL_SIZE,
        fontWeight = FontWeight.SemiBold,
        modifier = styled.padding(horizontal = Spacing.md, vertical = Spacing.xs),
    )
}

@Preview
@Composable
private fun NotificationFilterTabsPreview() {
    AppPreview {
        NotificationFilterTabs(selected = NotificationFilter.ALL, onSelect = {})
    }
}
