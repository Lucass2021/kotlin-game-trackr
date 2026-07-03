package com.lucasdias.gametrackr.feature.app.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.feature.app.appshell.components.ComingSoon

@Composable
fun ProfileScreen(
    isGuest: Boolean,
    userName: String?,
    modifier: Modifier = Modifier,
) {
    val title =
        when {
            isGuest -> stringResource(R.string.profile_guest_title)
            !userName.isNullOrBlank() -> userName
            else -> stringResource(R.string.profile_title_fallback)
        }
    val subtitle =
        if (isGuest) {
            stringResource(R.string.profile_guest_subtitle)
        } else {
            stringResource(R.string.profile_subtitle)
        }
    ComingSoon(
        icon = Icons.Outlined.AccountCircle,
        title = title,
        subtitle = subtitle,
        modifier = modifier,
    )
}
