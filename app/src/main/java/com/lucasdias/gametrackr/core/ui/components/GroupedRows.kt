package com.lucasdias.gametrackr.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

internal val GROUPED_ICON_BOX_WIDTH = 24.dp
private val ICON_SIZE = 20.dp
private val CHEVRON_SIZE = 16.dp
private val PROGRESS_SIZE = 16.dp
private val PROGRESS_STROKE = 2.dp
private val TITLE_SIZE = 16.sp

@Composable
fun GroupedToggleRow(
    icon: AppIcon,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    RowLayout(icon = icon, tint = AppPrimary, modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.xxs)) {
        RowTitle(title = title, color = AppTextPrimary)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors =
                SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = AppPrimary,
                    uncheckedThumbColor = AppTextSecondary,
                    uncheckedTrackColor = AppOutline,
                    uncheckedBorderColor = AppOutline,
                ),
        )
    }
}

@Composable
fun GroupedNavRow(
    icon: AppIcon,
    title: String,
    onClick: () -> Unit,
) {
    RowLayout(icon = icon, tint = AppPrimary, modifier = Modifier.pressable(title, onClick)) {
        RowTitle(title = title, color = AppTextPrimary)
        Icon(
            imageVector = AppIcon.FORWARD.image(),
            contentDescription = null,
            tint = AppTextSecondary,
            modifier = Modifier.size(CHEVRON_SIZE),
        )
    }
}

@Composable
fun GroupedDestructiveRow(
    icon: AppIcon,
    title: String,
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    RowLayout(icon = icon, tint = AppTertiary, modifier = Modifier.pressable(title, onClick)) {
        RowTitle(title = title, color = AppTertiary)
        if (isLoading) {
            CircularProgressIndicator(color = AppTertiary, strokeWidth = PROGRESS_STROKE, modifier = Modifier.size(PROGRESS_SIZE))
        }
    }
}

@Composable
private fun Modifier.pressable(
    label: String,
    onClick: () -> Unit,
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return pressScale(interactionSource)
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClickLabel = label,
            role = Role.Button,
            onClick = onClick,
        ).padding(horizontal = Spacing.md, vertical = Spacing.md)
}

@Composable
private fun RowLayout(
    icon: AppIcon,
    tint: Color,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().then(modifier),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.width(GROUPED_ICON_BOX_WIDTH), contentAlignment = Alignment.Center) {
            Icon(imageVector = icon.image(), contentDescription = null, tint = tint, modifier = Modifier.size(ICON_SIZE))
        }
        content()
    }
}

@Composable
private fun RowScope.RowTitle(
    title: String,
    color: Color,
) {
    Text(text = title, color = color, style = AppType.label(TITLE_SIZE), modifier = Modifier.weight(1f))
}

@Preview
@Composable
private fun GroupedRowsPreview() {
    AppPreview {
        GroupedSection(title = stringResource(R.string.settings_title)) {
            GroupedToggleRow(
                icon = AppIcon.NOTIFICATIONS,
                title = stringResource(R.string.settings_push_notifications),
                checked = true,
                onCheckedChange = {},
            )
            GroupedNavRow(icon = AppIcon.SHIELD_CHECK, title = stringResource(R.string.settings_change_password), onClick = {})
            GroupedDestructiveRow(
                icon = AppIcon.CLOSE,
                title = stringResource(R.string.settings_delete_account),
                isLoading = true,
                onClick = {},
            )
        }
    }
}
