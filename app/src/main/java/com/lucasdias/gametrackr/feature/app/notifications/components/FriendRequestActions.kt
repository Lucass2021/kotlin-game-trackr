package com.lucasdias.gametrackr.feature.app.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val LABEL_SIZE = 14.sp

@Composable
fun FriendRequestActions(
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val acceptSource = remember { MutableInteractionSource() }
    val declineSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier.padding(top = Spacing.xxxs),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.notifications_accept),
            color = AppOnPrimary,
            fontSize = LABEL_SIZE,
            fontWeight = FontWeight.SemiBold,
            modifier =
                Modifier
                    .pressScale(acceptSource)
                    .clip(CircleShape)
                    .clickable(interactionSource = acceptSource, indication = null, onClick = onAccept)
                    .background(AppPrimary)
                    .padding(horizontal = Spacing.lg, vertical = Spacing.xs),
        )
        Text(
            text = stringResource(R.string.notifications_decline),
            color = AppTertiary,
            fontSize = LABEL_SIZE,
            fontWeight = FontWeight.SemiBold,
            modifier =
                Modifier
                    .pressScale(declineSource)
                    .clickable(interactionSource = declineSource, indication = null, onClick = onDecline)
                    .padding(vertical = Spacing.xs),
        )
    }
}

@Preview
@Composable
private fun FriendRequestActionsPreview() {
    AppPreview {
        FriendRequestActions(onAccept = {}, onDecline = {})
    }
}
