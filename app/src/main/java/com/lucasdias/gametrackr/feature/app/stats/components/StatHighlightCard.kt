package com.lucasdias.gametrackr.feature.app.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.stats.StatHighlight

@Composable
fun StatHighlightCard(
    highlight: StatHighlight,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(18.dp)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .padding(vertical = 22.dp)
                .clearAndSetSemantics {},
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = stringResource(highlight.labelRes).uppercase(),
            color = AppTextSecondary,
            style = AppType.label(12.sp),
            letterSpacing = 1.6.sp,
        )

        Text(
            text = highlight.value,
            color = AppPrimary,
            style =
                AppType.headline(40.sp, FontWeight.ExtraBold).copy(
                    shadow = Shadow(color = AppPrimary.copy(alpha = 0.45f), offset = Offset.Zero, blurRadius = 32f),
                ),
        )

        Text(
            text = highlight.caption,
            color = AppTextSecondary,
            style = AppType.body(13.sp),
        )
    }
}
