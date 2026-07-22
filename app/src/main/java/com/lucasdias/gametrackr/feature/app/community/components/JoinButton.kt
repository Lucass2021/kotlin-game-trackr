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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType

@Composable
fun JoinButton(
    isJoined: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
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
                .clip(shape)
                .background(background)
                .then(if (isJoined) Modifier.border(BorderStroke(1.dp, AppOutline), shape) else Modifier)
                .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
                .padding(
                    horizontal = if (expanded) 28.dp else 20.dp,
                    vertical = if (expanded) 15.dp else 10.dp,
                ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isJoined) {
            Icon(
                imageVector = AppIcon.CHECK.image(),
                contentDescription = null,
                tint = content,
                modifier = Modifier.size(14.dp).padding(end = 0.dp),
            )
        }
        Text(
            text = if (isJoined) "Joined" else "Join",
            color = content,
            style = AppType.label(14.sp),
            modifier = Modifier.padding(start = if (isJoined) 6.dp else 0.dp),
        )
    }
}
