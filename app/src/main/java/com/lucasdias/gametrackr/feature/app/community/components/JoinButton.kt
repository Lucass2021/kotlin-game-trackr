package com.lucasdias.gametrackr.feature.app.community.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.glow
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val BORDER_WIDTH = 1.dp
private val ICON_SIZE = 14.dp
private val LABEL_SIZE = 14.sp
private const val GLOW_ALPHA = 0.35f

@Composable
fun JoinButton(
    isJoined: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = CircleShape
    val background = if (isJoined) Color.Transparent else AppPrimary
    val content = if (isJoined) AppTextPrimary else AppOnPrimary

    Row(
        modifier =
            modifier
                .then(if (expanded) Modifier.fillMaxWidth() else Modifier)
                .pressScale(interactionSource)
                .then(if (isJoined) Modifier else Modifier.glow(color = AppPrimary, cornerRadius = Radius.xl, alpha = GLOW_ALPHA))
                .clip(shape)
                .background(background)
                .then(if (isJoined) Modifier.border(BorderStroke(BORDER_WIDTH, AppOutline), shape) else Modifier)
                .clickable(interactionSource = interactionSource, indication = null, enabled = enabled, onClick = onClick)
                .padding(
                    horizontal = if (expanded) Spacing.xl else Spacing.lg,
                    vertical = if (expanded) Spacing.md else Spacing.xs,
                ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isJoined) {
            Icon(
                imageVector = AppIcon.CHECK.image(),
                contentDescription = null,
                tint = content,
                modifier = Modifier.size(ICON_SIZE),
            )
        }
        Text(
            text = stringResource(if (isJoined) R.string.community_joined else R.string.community_join),
            color = content,
            style = AppType.label(LABEL_SIZE),
            modifier = Modifier.padding(start = if (isJoined) Spacing.xxs else 0.dp),
        )
    }
}

@Preview
@Composable
private fun JoinButtonPreview() {
    AppPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.padding(Spacing.md)) {
            JoinButton(isJoined = false, onClick = {})
            JoinButton(isJoined = true, onClick = {})
            JoinButton(isJoined = true, onClick = {}, enabled = false)
        }
    }
}
