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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.stats.YearCount

private val MaxBarHeight = 110.dp

@Composable
fun YearBarChart(
    years: List<YearCount>,
    modifier: Modifier = Modifier,
) {
    val maxCount = (years.maxOfOrNull { it.count } ?: 0).coerceAtLeast(1)

    Row(
        modifier = modifier.fillMaxWidth().height(150.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
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
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Bottom),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height((MaxBarHeight * year.count / maxCount).coerceAtLeast(12.dp))
                    .clip(RoundedCornerShape(6.dp))
                    .background(year.tint),
        )

        Text(
            text = year.year,
            color = if (isCurrent) AppTextPrimary else AppTextSecondary,
            style = AppType.label(12.sp, if (isCurrent) FontWeight.ExtraBold else FontWeight.Normal),
        )
    }
}
