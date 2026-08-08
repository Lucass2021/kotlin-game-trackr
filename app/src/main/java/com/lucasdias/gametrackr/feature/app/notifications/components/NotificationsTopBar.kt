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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.BackButton
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType

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
                .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        BackButton(onBack = onBack)
        Text(
            text = stringResource(R.string.notifications_title),
            color = AppTextPrimary,
            style = AppType.headline(22.sp),
            modifier = Modifier.weight(1f),
        )
        if (showActions) {
            var menuExpanded by remember { mutableStateOf(false) }
            Box {
                val interactionSource = remember { MutableInteractionSource() }
                Icon(
                    imageVector = AppIcon.OVERFLOW.image(),
                    contentDescription = null,
                    tint = AppTextPrimary,
                    modifier =
                        Modifier
                            .pressScale(interactionSource)
                            .size(40.dp)
                            .padding(9.dp)
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
                                modifier = Modifier.size(20.dp),
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
                                color = Color.Red,
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = AppIcon.TRASH.image(),
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp),
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
