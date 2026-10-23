package com.lucasdias.gametrackr.feature.app.profile.editprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.ProfileVisibility
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val BORDER_WIDTH = 1.dp
private val ICON_BOX_WIDTH = 24.dp
private val ICON_SIZE = 20.dp
private val TITLE_SIZE = 15.sp
private val DETAIL_SIZE = 13.sp
private const val DETAIL_SELECTED_ALPHA = 0.75f

@Composable
fun VisibilitySelector(
    selection: ProfileVisibility,
    onSelect: (ProfileVisibility) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        ProfileVisibility.entries.forEach { option ->
            VisibilityRow(
                option = option,
                isSelected = option == selection,
                onClick = { onSelect(option) },
            )
        }
    }
}

@Composable
private fun VisibilityRow(
    option: ProfileVisibility,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(Radius.md)
    val title = stringResource(option.titleRes)

    Row(
        modifier =
            Modifier
                .pressScale(interaction)
                .fillMaxWidth()
                .clip(shape)
                .background(if (isSelected) AppPrimary else AppSurfaceCard)
                .border(BORDER_WIDTH, if (isSelected) Color.Transparent else AppOutline, shape)
                .selectable(
                    selected = isSelected,
                    interactionSource = interaction,
                    indication = null,
                    role = Role.RadioButton,
                    onClick = onClick,
                ).padding(Spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.width(ICON_BOX_WIDTH), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = option.icon.image(),
                contentDescription = null,
                tint = if (isSelected) AppOnPrimary else AppTextSecondary,
                modifier = Modifier.size(ICON_SIZE),
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxxs)) {
            Text(
                text = title,
                color = if (isSelected) AppOnPrimary else AppTextPrimary,
                style = AppType.label(TITLE_SIZE),
            )
            Text(
                text = stringResource(option.detailRes),
                color = if (isSelected) AppOnPrimary.copy(alpha = DETAIL_SELECTED_ALPHA) else AppTextSecondary,
                style = AppType.body(DETAIL_SIZE),
            )
        }
    }
}

@Preview
@Composable
private fun VisibilitySelectorPreview() {
    AppPreview {
        VisibilitySelector(selection = ProfileVisibility.PUBLIC, onSelect = {})
    }
}
