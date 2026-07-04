package com.lucasdias.gametrackr.feature.app.community

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.feature.app.appshell.components.ComingSoon

@Composable
fun CommunityScreen(modifier: Modifier = Modifier) {
    ComingSoon(
        icon = AppIcon.COMMUNITY,
        title = stringResource(R.string.community_title),
        subtitle = stringResource(R.string.community_subtitle),
        modifier = modifier,
    )
}
