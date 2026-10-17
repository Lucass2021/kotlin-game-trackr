package com.lucasdias.gametrackr.feature.app.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.BackButton
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val TITLE_SIZE = 22.sp
private val MENU_BUTTON_SIZE = 40.dp
private val MENU_ICON_SIZE = 20.dp

@Composable
fun NotificationsTopBar(
    onBack: () -> Unit,
    onMarkAllRead: () -> Unit,
    onClearAll: () -> Unit,
    showActions: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(AppBackground)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = Spacing.lg, vertical = Spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
    ) {
        BackButton(onBack = onBack)
        Text(
            text = stringResource(R.string.notifications_title),
            color = AppTextPrimary,
            style = AppType.headline(TITLE_SIZE),
            modifier = Modifier.weight(1f),
        )
        if (showActions) {
            var menuExpanded by remember { mutableStateOf(false) }
            Box {
                val interactionSource = remember { MutableInteractionSource() }
                Icon(
                    imageVector = AppIcon.OVERFLOW.image(),
                    contentDescription = stringResource(R.string.notifications_more_options),
                    tint = AppTextPrimary,
                    modifier =
                        Modifier
                            .pressScale(interactionSource)
                            .size(MENU_BUTTON_SIZE)
                            .padding(Spacing.xs)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = { menuExpanded = true },
                            ),
                )
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.notifications_mark_all_read)) },
                        leadingIcon = {
                            Icon(
                                imageVector = AppIcon.CHECK.image(),
                                contentDescription = null,
                                modifier = Modifier.size(MENU_ICON_SIZE),
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            onMarkAllRead()
                        },
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                stringResource(R.string.notifications_clear_all),
                                color = AppTertiary,
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = AppIcon.TRASH.image(),
                                contentDescription = null,
                                tint = AppTertiary,
                                modifier = Modifier.size(MENU_ICON_SIZE),
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            onClearAll()
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun NotificationsTopBarPreview() {
    AppPreview {
        NotificationsTopBar(onBack = {}, onMarkAllRead = {}, onClearAll = {}, showActions = true)
    }
}
