package com.lucasdias.gametrackr.feature.app.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.stats.StatBar

@Composable
fun StatBarList(
    bars: List<StatBar>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        bars.forEach { bar -> BarRow(bar) }
    }
}

@Composable
private fun BarRow(bar: StatBar) {
    Column(
        modifier = Modifier.fillMaxWidth().clearAndSetSemantics {},
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = bar.label.uppercase(),
                color = AppTextPrimary,
                style = AppType.label(12.sp),
                letterSpacing = 0.8.sp,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = bar.value.uppercase(),
                color = AppTextSecondary,
                style = AppType.label(12.sp),
                letterSpacing = 0.8.sp,
            )
        }

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(CircleShape)
                    .background(AppOutline),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(bar.fraction.coerceIn(0.03f, 1f))
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(bar.tint),
            )
        }
    }
}
