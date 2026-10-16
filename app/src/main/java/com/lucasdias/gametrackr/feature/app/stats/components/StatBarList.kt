package com.lucasdias.gametrackr.feature.app.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.StatBar
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.stats.StatsMockData

private val BAR_HEIGHT = 7.dp
private val MIN_BAR_WIDTH = 8.dp
private val LABEL_SIZE = 12.sp
private val LABEL_LETTER_SPACING = 0.8.sp

@Composable
fun StatBarList(
    bars: List<StatBar>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        bars.forEach { bar -> BarRow(bar) }
    }
}

@Composable
private fun BarRow(bar: StatBar) {
    Column(
        modifier = Modifier.fillMaxWidth().clearAndSetSemantics {},
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            Text(
                text = bar.label.uppercase(),
                color = AppTextPrimary,
                style = AppType.label(LABEL_SIZE),
                letterSpacing = LABEL_LETTER_SPACING,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = bar.value.uppercase(),
                color = AppTextSecondary,
                style = AppType.label(LABEL_SIZE),
                letterSpacing = LABEL_LETTER_SPACING,
            )
        }

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(BAR_HEIGHT)
                    .clip(CircleShape)
                    .background(AppOutline),
        ) {
            Box(
                modifier =
                    Modifier
                        .widthIn(min = MIN_BAR_WIDTH)
                        .fillMaxWidth(bar.fraction.coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(bar.tint),
            )
        }
    }
}

@Preview
@Composable
private fun StatBarListPreview() {
    AppPreview {
        StatBarList(bars = StatsMockData.stats.platforms)
    }
}
