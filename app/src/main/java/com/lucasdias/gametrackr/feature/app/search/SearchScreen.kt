package com.lucasdias.gametrackr.feature.app.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.feature.app.appshell.components.ComingSoon
import com.lucasdias.gametrackr.feature.app.appshell.components.DetailTopBar

@Composable
fun SearchScreen(onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    Column(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        DetailTopBar(title = stringResource(R.string.search_title), onBack = onBack)
        ComingSoon(
            icon = AppIcon.SEARCH,
            title = stringResource(R.string.search_title),
            subtitle = stringResource(R.string.search_subtitle),
            modifier = Modifier.weight(1f),
        )
    }
}
