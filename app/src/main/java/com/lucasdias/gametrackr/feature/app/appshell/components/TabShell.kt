package com.lucasdias.gametrackr.feature.app.appshell.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.feature.app.appshell.AppTab

@Composable
fun TabShell(
    selected: AppTab,
    onSelect: (AppTab) -> Unit,
    onNotifications: () -> Unit,
    onSearch: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (AppTab) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize().background(AppBackground)) {
        AppHeader(
            onNotifications = onNotifications,
            onSearch = onSearch,
            onMenu = onMenu,
        )

        Box(modifier = Modifier.weight(1f).fillMaxSize()) {
            content(selected)
        }

        AppTabBar(selected = selected, onSelect = onSelect)
    }
}

@Preview
@Composable
private fun TabShellPreview() {
    AppPreview {
        TabShell(
            selected = AppTab.HOME,
            onSelect = {},
            onNotifications = {},
            onSearch = {},
            onMenu = {},
            content = {},
        )
    }
}
