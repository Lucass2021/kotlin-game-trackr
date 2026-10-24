package com.lucasdias.gametrackr.feature.app.profilemenu.components

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.app.profilemenu.ProfileMenuItem

private val ICON_BOX_WIDTH = 24.dp
private val ICON_SIZE = 20.dp
private val CHEVRON_SIZE = 16.dp
private val DIVIDER_THICKNESS = 1.dp
private val DIVIDER_INSET = Spacing.md + ICON_BOX_WIDTH + Spacing.sm
private val TITLE_SIZE = 16.sp

@Composable
fun ProfileMenuSection(
    items: List<ProfileMenuItem>,
    onItemSelect: (ProfileMenuItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(color = AppSurfaceCard, shape = RoundedCornerShape(Radius.lg), modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            items.forEachIndexed { index, item ->
                MenuRow(item = item, onClick = { onItemSelect(item) })
                if (index < items.lastIndex) {
                    HorizontalDivider(
                        thickness = DIVIDER_THICKNESS,
                        color = AppOutline,
                        modifier = Modifier.padding(start = DIVIDER_INSET),
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuRow(
    item: ProfileMenuItem,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val title = stringResource(item.titleRes)
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .pressScale(interactionSource)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = title,
                    role = Role.Button,
                    onClick = onClick,
                ).padding(horizontal = Spacing.md, vertical = Spacing.md),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.width(ICON_BOX_WIDTH), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = item.icon.image(),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(ICON_SIZE),
            )
        }
        Text(text = title, color = AppTextPrimary, style = AppType.label(TITLE_SIZE), modifier = Modifier.weight(1f))
        Icon(
            imageVector = AppIcon.FORWARD.image(),
            contentDescription = null,
            tint = AppTextSecondary,
            modifier = Modifier.size(CHEVRON_SIZE),
        )
    }
}

@Preview
@Composable
private fun ProfileMenuSectionPreview() {
    AppPreview {
        ProfileMenuSection(items = ProfileMenuItem.entries, onItemSelect = {})
    }
}
