package com.lucasdias.gametrackr.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.anim.subtleBounce
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val TILE_SIZE = 120.dp
private val TILE_CORNER_RADIUS = 28.dp
private val GLOW_BLUR_RADIUS = 34.dp
private const val GLOW_ALPHA = 0.35f
private val ICON_SIZE = 56.dp
private val TITLE_SIZE = 24.sp
private val MESSAGE_SIZE = 15.sp

@Composable
fun EmptyState(
    icon: AppIcon,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionTitle: String? = null,
    actionIcon: AppIcon? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.xxl, vertical = Spacing.xxxl),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .subtleBounce()
                    .size(TILE_SIZE)
                    .glow(
                        color = AppPrimary,
                        cornerRadius = TILE_CORNER_RADIUS,
                        blurRadius = GLOW_BLUR_RADIUS,
                        alpha = GLOW_ALPHA,
                    ).clip(RoundedCornerShape(TILE_CORNER_RADIUS))
                    .background(AppSurfaceCard),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon.image(),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(ICON_SIZE),
            )
        }

        Text(
            text = title,
            color = AppTextPrimary,
            style = AppType.headline(TITLE_SIZE, FontWeight.ExtraBold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Spacing.xl),
        )
        Text(
            text = message,
            color = AppTextSecondary,
            style = AppType.body(MESSAGE_SIZE),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Spacing.xs),
        )

        if (actionTitle != null && onAction != null) {
            PrimaryButton(
                text = actionTitle,
                onClick = onAction,
                icon = actionIcon,
                modifier = Modifier.padding(top = Spacing.xl),
            )
        }
    }
}

@Preview
@Composable
private fun EmptyStatePreview() {
    AppPreview {
        EmptyState(
            icon = AppIcon.DEVICES,
            title = "No setup yet",
            message = "Add your battle station, retro corner or handheld gear so other players can see how you play.",
            actionTitle = "Add your first setup",
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
