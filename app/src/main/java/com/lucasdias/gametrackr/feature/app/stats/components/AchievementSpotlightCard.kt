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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.lucasdias.gametrackr.feature.app.stats.AchievementSpotlight

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
                .clip(RoundedCornerShape(18.dp))
                .background(Brush.linearGradient(listOf(CoverIndigoStart, CoverCyanStart, CoverIndigoEnd)))
                .background(AppBackground.copy(alpha = 0.45f)),
    ) {
        Icon(
            imageVector = AppIcon.TROPHY.image(),
            contentDescription = null,
            tint = AppPrimary.copy(alpha = 0.12f),
            modifier = Modifier.align(Alignment.TopEnd).offset(x = 30.dp, y = (-20).dp).size(140.dp),
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = spotlight.title,
                color = AppTextPrimary,
                style = AppType.headline(24.sp, FontWeight.ExtraBold),
            )

            Text(
                text = spotlight.message,
                color = AppTextPrimary.copy(alpha = 0.85f),
                style = AppType.body(15.sp),
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
                .padding(top = 4.dp)
                .pressScale(interactionSource)
                .clip(CircleShape)
                .background(AppPrimary)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = title,
                    role = Role.Button,
                    onClick = onClick,
                ).height(44.dp)
                .padding(horizontal = 26.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = title, color = AppOnPrimary, style = AppType.label(15.sp))
    }
}
