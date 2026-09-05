package com.lucasdias.gametrackr.feature.app.profile.editprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.core.model.ProfileColor
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.darkened
import com.lucasdias.gametrackr.core.ui.theme.toAppColor

@Composable
fun AvatarColorPicker(
    colors: List<ProfileColor>,
    selection: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        colors.forEach { palette ->
            Swatch(
                color = palette,
                isSelected = palette.hex.equals(selection, ignoreCase = true),
                onClick = { onSelect(palette.hex) },
            )
        }
    }
}

@Composable
private fun Swatch(
    color: ProfileColor,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }

    Box(
        modifier =
            Modifier
                .pressScale(interaction)
                .size(46.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(color.hex.toAppColor(), color.hex.toAppColor().darkened(GRADIENT_DEPTH)),
                    ),
                ).border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) AppPrimary else AppOutline,
                    shape = CircleShape,
                ).clickable(
                    interactionSource = interaction,
                    indication = null,
                    onClickLabel = color.name,
                    role = Role.RadioButton,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        if (isSelected) {
            Icon(
                imageVector = AppIcon.CHECK.image(),
                contentDescription = null,
                tint = AppTextPrimary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

private const val GRADIENT_DEPTH = 0.28f
