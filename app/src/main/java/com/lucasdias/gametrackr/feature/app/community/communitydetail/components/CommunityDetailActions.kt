package com.lucasdias.gametrackr.feature.app.community.communitydetail.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.app.community.components.JoinButton

private val CIRCLE_SIZE = 50.dp
private val CIRCLE_ICON_SIZE = 20.dp
private val BORDER_WIDTH = 1.dp

@Composable
fun CommunityDetailActions(
    isJoined: Boolean,
    isOwner: Boolean,
    onJoin: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        JoinButton(
            isJoined = isJoined,
            onClick = onJoin,
            expanded = true,
            modifier = Modifier.weight(1f),
            enabled = !isOwner,
        )
        CircleIconButton(
            icon = AppIcon.NOTIFICATIONS,
            label = stringResource(R.string.community_action_notifications),
        )
        CircleIconButton(
            icon = AppIcon.SHARE,
            label = stringResource(R.string.community_action_share_community),
            onClick = onShare,
        )
    }
}

@Composable
private fun CircleIconButton(
    icon: AppIcon,
    label: String,
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .size(CIRCLE_SIZE)
                .clip(CircleShape)
                .border(BORDER_WIDTH, AppOutline, CircleShape)
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
            modifier = Modifier.size(CIRCLE_ICON_SIZE),
        )
    }
}

@Preview
@Composable
private fun CommunityDetailActionsPreview() {
    AppPreview {
        CommunityDetailActions(isJoined = false, isOwner = false, onJoin = {}, onShare = {})
    }
}
