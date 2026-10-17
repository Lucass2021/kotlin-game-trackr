package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.app.profile.ProfileHeaderMode

private val BUTTON_HEIGHT = 46.dp
private val BORDER_WIDTH = 1.dp
private val PILL_ICON_SIZE = 18.dp
private val CIRCLE_ICON_SIZE = 20.dp
private val LABEL_SIZE = 15.sp

@Composable
fun ProfileHeaderActions(
    mode: ProfileHeaderMode,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
    isGuest: Boolean = false,
    onEdit: () -> Unit = {},
    onAddFriend: () -> Unit = {},
    onMessage: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (mode) {
            ProfileHeaderMode.Own -> {
                Pill(
                    icon = AppIcon.EDIT_PROFILE,
                    text = stringResource(R.string.profile_action_edit),
                    isPrimary = true,
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                )
            }

            is ProfileHeaderMode.Other -> {
                if (!isGuest) {
                    Pill(
                        icon = if (mode.isFriend) AppIcon.CHECK else AppIcon.ADD_FRIEND,
                        text =
                            stringResource(
                                if (mode.isFriend) R.string.profile_action_friends else R.string.profile_action_add_friend,
                            ),
                        isPrimary = !mode.isFriend,
                        onClick = onAddFriend,
                        modifier = Modifier.weight(1f),
                    )
                    CircleAction(
                        icon = AppIcon.ENVELOPE,
                        label = stringResource(R.string.profile_action_message),
                        onClick = onMessage,
                    )
                }
            }
        }

        CircleAction(
            icon = AppIcon.SHARE,
            label = stringResource(R.string.profile_action_share),
            onClick = onShare,
        )
    }
}

@Composable
private fun Pill(
    icon: AppIcon,
    text: String,
    isPrimary: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interaction = remember { MutableInteractionSource() }
    val contentColor = if (isPrimary) AppOnPrimary else AppTextPrimary
    val surface =
        if (isPrimary) {
            Modifier.background(AppPrimary)
        } else {
            Modifier.border(BORDER_WIDTH, AppOutline, CircleShape)
        }

    Row(
        modifier =
            modifier
                .pressScale(interaction)
                .height(BUTTON_HEIGHT)
                .clip(CircleShape)
                .then(surface)
                .clickable(
                    interactionSource = interaction,
                    indication = null,
                    onClickLabel = text,
                    role = Role.Button,
                    onClick = onClick,
                ),
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon.image(),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(PILL_ICON_SIZE),
        )
        Text(text = text, color = contentColor, style = AppType.label(LABEL_SIZE))
    }
}

@Composable
private fun CircleAction(
    icon: AppIcon,
    label: String,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    Box(
        modifier =
            Modifier
                .pressScale(interaction)
                .size(BUTTON_HEIGHT)
                .clip(CircleShape)
                .border(BORDER_WIDTH, AppOutline, CircleShape)
                .clickable(
                    interactionSource = interaction,
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
            modifier = Modifier.size(CIRCLE_ICON_SIZE),
        )
    }
}

@Preview
@Composable
private fun ProfileHeaderActionsPreview() {
    AppPreview {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
            ProfileHeaderActions(mode = ProfileHeaderMode.Own, onShare = {})
            ProfileHeaderActions(mode = ProfileHeaderMode.Other(isFriend = false), onShare = {})
            ProfileHeaderActions(mode = ProfileHeaderMode.Other(isFriend = true), onShare = {})
        }
    }
}
