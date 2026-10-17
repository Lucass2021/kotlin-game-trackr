package com.lucasdias.gametrackr.feature.app.community.communitydetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val BUTTON_SIZE = 40.dp
private val ICON_SIZE = 20.dp
private const val BACKGROUND_ALPHA = 0.55f
private val MENU_LABEL_SIZE = 15.sp

@Composable
fun CommunityDetailFloatingButton(
    icon: AppIcon,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier =
            modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = Spacing.md, vertical = Spacing.xs)
                .pressScale(interactionSource)
                .size(BUTTON_SIZE)
                .clip(CircleShape)
                .background(AppBackground.copy(alpha = BACKGROUND_ALPHA))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = label,
                    role = Role.Button,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon.image(),
            contentDescription = label,
            tint = AppTextPrimary,
            modifier = Modifier.size(ICON_SIZE),
        )
    }
}

@Composable
fun CommunityDetailOwnerMenu(
    onDeleteRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        CommunityDetailFloatingButton(
            icon = AppIcon.OVERFLOW,
            label = stringResource(R.string.community_action_more),
            onClick = { showMenu = true },
        )
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            containerColor = AppSurfaceCard,
            modifier = Modifier.padding(end = Spacing.md),
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.community_delete_action),
                        color = AppTertiary,
                        style = AppType.label(MENU_LABEL_SIZE),
                    )
                },
                onClick = {
                    showMenu = false
                    onDeleteRequest()
                },
            )
        }
    }
}

@Preview
@Composable
private fun CommunityDetailFloatingButtonPreview() {
    AppPreview {
        Row {
            CommunityDetailFloatingButton(icon = AppIcon.BACK, label = "Back", onClick = {})
            CommunityDetailOwnerMenu(onDeleteRequest = {})
        }
    }
}
