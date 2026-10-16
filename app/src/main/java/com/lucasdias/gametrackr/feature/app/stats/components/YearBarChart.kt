package com.lucasdias.gametrackr.feature.app.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.YearCount
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.stats.StatsMockData

private val CHART_HEIGHT = 150.dp
private val MAX_BAR_HEIGHT = 110.dp
private val MIN_BAR_HEIGHT = 12.dp
private val LABEL_SIZE = 12.sp

@Composable
fun YearBarChart(
    years: List<YearCount>,
    modifier: Modifier = Modifier,
) {
    val maxCount = (years.maxOfOrNull { it.count } ?: 0).coerceAtLeast(1)

    Row(
        modifier = modifier.fillMaxWidth().height(CHART_HEIGHT),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        verticalAlignment = Alignment.Bottom,
    ) {
        years.forEachIndexed { index, year ->
            YearColumn(
                year = year,
                maxCount = maxCount,
                isCurrent = index == years.lastIndex,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun YearColumn(
    year: YearCount,
    maxCount: Int,
    isCurrent: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.xs, Alignment.Bottom),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height((MAX_BAR_HEIGHT * year.count / maxCount).coerceAtLeast(MIN_BAR_HEIGHT))
                    .clip(RoundedCornerShape(Radius.sm))
                    .background(year.tint),
        )

        Text(
            text = year.year,
            color = if (isCurrent) AppTextPrimary else AppTextSecondary,
            style = AppType.label(LABEL_SIZE, if (isCurrent) FontWeight.ExtraBold else FontWeight.Normal),
        )
    }
}

@Preview
@Composable
private fun YearBarChartPreview() {
    AppPreview {
        YearBarChart(years = StatsMockData.stats.yearsCompleted)
    }
}
