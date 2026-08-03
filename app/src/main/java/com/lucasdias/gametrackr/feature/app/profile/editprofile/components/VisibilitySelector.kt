package com.lucasdias.gametrackr.feature.app.profile.editprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.profile.ProfileVisibility

@Composable
fun VisibilitySelector(
    selection: ProfileVisibility,
    onSelect: (ProfileVisibility) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
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
    val shape = RoundedCornerShape(12.dp)
    val title = stringResource(option.titleRes)

    Row(
        modifier =
            Modifier
                .pressScale(interaction)
                .fillMaxWidth()
                .clip(shape)
                .background(if (isSelected) AppPrimary else AppSurfaceCard)
                .border(1.dp, if (isSelected) Color.Transparent else AppOutline, shape)
                .clickable(
                    interactionSource = interaction,
                    indication = null,
                    onClickLabel = title,
                    role = Role.RadioButton,
                    onClick = onClick,
                ).padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.width(24.dp), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = option.icon.image(),
                contentDescription = null,
                tint = if (isSelected) AppOnPrimary else AppTextSecondary,
                modifier = Modifier.size(20.dp),
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = title,
                color = if (isSelected) AppOnPrimary else AppTextPrimary,
                style = AppType.label(15.sp),
            )
            Text(
                text = stringResource(option.detailRes),
                color = if (isSelected) AppOnPrimary.copy(alpha = 0.75f) else AppTextSecondary,
                style = AppType.body(13.sp),
            )
        }
    }
}
