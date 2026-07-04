package com.lucasdias.gametrackr.feature.app.appshell.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.GameTrackrTheme

@Composable
fun AppHeader(
    onNotifications: () -> Unit,
    onSearch: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(AppBackground)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.logo_wordmark),
            contentDescription = stringResource(R.string.app_logo),
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(26.dp),
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.End),
        ) {
            HeaderButton(AppIcon.NOTIFICATIONS.image(), R.string.header_notifications, onNotifications)
            HeaderButton(AppIcon.SEARCH.image(), R.string.header_search, onSearch)
            HeaderButton(AppIcon.SETTINGS.image(), R.string.header_menu, onMenu)
        }
    }
}

@Composable
private fun HeaderButton(
    icon: ImageVector,
    contentDescription: Int,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Icon(
        imageVector = icon,
        contentDescription = stringResource(contentDescription),
        tint = AppTextPrimary,
        modifier =
            Modifier
                .pressScale(interactionSource)
                .size(40.dp)
                .clip(CircleShape)
                .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
                .padding(10.dp),
    )
}

@Preview
@Composable
private fun AppHeaderPreview() {
    GameTrackrTheme {
        AppHeader(onNotifications = {}, onSearch = {}, onMenu = {})
    }
}
