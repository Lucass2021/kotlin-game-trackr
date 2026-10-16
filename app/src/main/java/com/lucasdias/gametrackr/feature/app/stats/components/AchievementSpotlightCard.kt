package com.lucasdias.gametrackr.feature.app.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.AchievementSpotlight
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.CoverCyanStart
import com.lucasdias.gametrackr.core.ui.theme.CoverIndigoEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverIndigoStart
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.stats.StatsMockData

private val TROPHY_SIZE = 140.dp
private val TROPHY_OFFSET_X = 30.dp
private val TROPHY_OFFSET_Y = (-20).dp
private const val TROPHY_ALPHA = 0.12f
private const val OVERLAY_ALPHA = 0.45f
private const val MESSAGE_ALPHA = 0.85f
private val ACTION_HEIGHT = 44.dp
private val TITLE_SIZE = 24.sp
private val MESSAGE_SIZE = 15.sp
private val ACTION_TEXT_SIZE = 15.sp

@Composable
fun AchievementSpotlightCard(
    spotlight: AchievementSpotlight,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Radius.lg))
                .background(Brush.linearGradient(listOf(CoverIndigoStart, CoverCyanStart, CoverIndigoEnd)))
                .background(AppBackground.copy(alpha = OVERLAY_ALPHA)),
    ) {
        Icon(
            imageVector = AppIcon.TROPHY.image(),
            contentDescription = null,
            tint = AppPrimary.copy(alpha = TROPHY_ALPHA),
            modifier = Modifier.align(Alignment.TopEnd).offset(x = TROPHY_OFFSET_X, y = TROPHY_OFFSET_Y).size(TROPHY_SIZE),
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Text(
                text = spotlight.title,
                color = AppTextPrimary,
                style = AppType.headline(TITLE_SIZE, FontWeight.ExtraBold),
            )

            Text(
                text = spotlight.message,
                color = AppTextPrimary.copy(alpha = MESSAGE_ALPHA),
                style = AppType.body(MESSAGE_SIZE),
            )

            ActionPill(title = spotlight.actionTitle, onClick = onAction)
        }
    }
}

@Composable
private fun ActionPill(
    title: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier =
            Modifier
                .padding(top = Spacing.xxs)
                .pressScale(interactionSource)
                .clip(CircleShape)
                .background(AppPrimary)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = title,
                    role = Role.Button,
                    onClick = onClick,
                ).height(ACTION_HEIGHT)
                .padding(horizontal = Spacing.xl),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = title, color = AppOnPrimary, style = AppType.label(ACTION_TEXT_SIZE))
    }
}

@Preview
@Composable
private fun AchievementSpotlightCardPreview() {
    AppPreview {
        AchievementSpotlightCard(spotlight = StatsMockData.stats.spotlight, onAction = {})
    }
}
