package com.lucasdias.gametrackr.feature.app.library

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.feature.app.appshell.components.ComingSoon

@Composable
fun LibraryScreen(modifier: Modifier = Modifier) {
    ComingSoon(
        icon = Icons.Outlined.Layers,
        title = stringResource(R.string.library_title),
        subtitle = stringResource(R.string.library_subtitle),
        modifier = modifier,
    )
}
