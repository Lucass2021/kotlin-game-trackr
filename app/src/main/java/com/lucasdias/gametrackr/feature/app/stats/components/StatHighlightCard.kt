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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.StatHighlight
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.stats.StatsMockData

private val BORDER_WIDTH = 1.dp
private val LABEL_SIZE = 12.sp
private val LABEL_LETTER_SPACING = 1.6.sp
private val VALUE_SIZE = 40.sp
private val CAPTION_SIZE = 13.sp
private const val GLOW_ALPHA = 0.45f
private const val GLOW_BLUR_RADIUS = 32f

@Composable
fun StatHighlightCard(
    highlight: StatHighlight,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(Radius.lg)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppOutline, shape)
                .padding(vertical = Spacing.lg)
                .clearAndSetSemantics {},
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
    ) {
        Text(
            text = stringResource(highlight.labelRes).uppercase(),
            color = AppTextSecondary,
            style = AppType.label(LABEL_SIZE),
            letterSpacing = LABEL_LETTER_SPACING,
        )

        Text(
            text = highlight.value,
            color = AppPrimary,
            style =
                AppType.headline(VALUE_SIZE, FontWeight.ExtraBold).copy(
                    shadow = Shadow(color = AppPrimary.copy(alpha = GLOW_ALPHA), offset = Offset.Zero, blurRadius = GLOW_BLUR_RADIUS),
                ),
        )

        Text(
            text = highlight.caption,
            color = AppTextSecondary,
            style = AppType.body(CAPTION_SIZE),
        )
    }
}

@Preview
@Composable
private fun StatHighlightCardPreview() {
    AppPreview {
        StatHighlightCard(highlight = StatsMockData.stats.highlights[0])
    }
}
