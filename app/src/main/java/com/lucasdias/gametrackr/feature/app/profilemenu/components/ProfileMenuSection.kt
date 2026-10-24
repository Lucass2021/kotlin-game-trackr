package com.lucasdias.gametrackr.feature.app.profilemenu.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.GroupedDivider
import com.lucasdias.gametrackr.core.ui.components.GroupedNavRow
import com.lucasdias.gametrackr.core.ui.components.GroupedSection
import com.lucasdias.gametrackr.feature.app.profilemenu.ProfileMenuItem

@Composable
fun ProfileMenuSection(
    items: List<ProfileMenuItem>,
    onItemSelect: (ProfileMenuItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    GroupedSection(title = null, modifier = modifier) {
        items.forEachIndexed { index, item ->
            GroupedNavRow(icon = item.icon, title = stringResource(item.titleRes), onClick = { onItemSelect(item) })
            if (index < items.lastIndex) GroupedDivider()
        }
    }
}

@Preview
@Composable
private fun ProfileMenuSectionPreview() {
    AppPreview {
        ProfileMenuSection(items = ProfileMenuItem.entries, onItemSelect = {})
    }
}
