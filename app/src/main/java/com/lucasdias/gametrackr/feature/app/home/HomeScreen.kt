package com.lucasdias.gametrackr.feature.app.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.feature.app.appshell.components.ComingSoon

@Composable
fun HomeScreen(
    isGuest: Boolean,
    userName: String?,
    modifier: Modifier = Modifier,
) {
    val greeting =
        when {
            isGuest -> stringResource(R.string.home_greeting_guest)
            !userName.isNullOrBlank() -> stringResource(R.string.home_greeting_user, userName)
            else -> stringResource(R.string.home_greeting_generic)
        }
    ComingSoon(
        icon = AppIcon.DISCOVER,
        title = greeting,
        subtitle = stringResource(R.string.home_coming_subtitle),
        modifier = modifier,
    )
}
