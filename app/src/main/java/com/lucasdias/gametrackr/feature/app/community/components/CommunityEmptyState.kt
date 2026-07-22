package com.lucasdias.gametrackr.feature.app.community.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.anim.subtleBounce
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType

@Composable
fun CommunityEmptyState(
    icon: AppIcon,
    title: String,
    message: String,
    actionTitle: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Box(
            modifier =
                Modifier
                    .subtleBounce()
                    .clip(RoundedCornerShape(30.dp))
                    .background(AppSurfaceCard)
                    .size(120.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon.image(),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(56.dp),
            )
        }

        Text(
            text = title,
            color = AppTextPrimary,
            style = AppType.headline(24.sp, FontWeight.ExtraBold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 26.dp),
        )
        Text(
            text = message,
            color = AppTextSecondary,
            style = AppType.body(15.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp),
        )

        Text(
            text = actionTitle,
            color = AppOnPrimary,
            style = AppType.label(16.sp),
            modifier =
                Modifier
                    .padding(top = 28.dp)
                    .pressScale(interactionSource)
                    .clip(CircleShape)
                    .background(AppPrimary)
                    .clickable(interactionSource = interactionSource, indication = null, onClick = onAction)
                    .padding(horizontal = 28.dp, vertical = 15.dp),
        )
    }
}
