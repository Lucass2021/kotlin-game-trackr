package com.lucasdias.gametrackr.feature.app.community

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.feature.app.appshell.components.ComingSoon

@Composable
fun CommunityScreen(modifier: Modifier = Modifier) {
    ComingSoon(
        icon = Icons.Outlined.Groups,
        title = stringResource(R.string.community_title),
        subtitle = stringResource(R.string.community_subtitle),
        modifier = modifier,
    )
}
