package com.lucasdias.gametrackr.feature.app.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.StatusShare
import com.lucasdias.gametrackr.core.model.barTint
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.stats.StatsMockData

private val BAR_HEIGHT = 26.dp
private val DOT_SIZE = 8.dp
private val PERCENT_SIZE = 13.sp
private val LABEL_SIZE = 12.sp

@Composable
fun StatusShareBar(
    shares: List<StatusShare>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        Row(modifier = Modifier.fillMaxWidth().height(BAR_HEIGHT).clip(CircleShape)) {
            shares.forEach { share ->
                Box(
                    modifier =
                        Modifier
                            .weight(share.percent.toFloat())
                            .fillMaxHeight()
                            .background(share.status.barTint),
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.md),
            verticalAlignment = Alignment.Top,
        ) {
            shares.forEach { share -> Legend(share) }
        }
    }
}

@Composable
private fun Legend(share: StatusShare) {
    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        Box(
            modifier =
                Modifier
                    .padding(top = Spacing.xxs)
                    .size(DOT_SIZE)
                    .clip(CircleShape)
                    .background(share.status.barTint),
        )

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxxs)) {
            Text(
                text = "${share.percent}%",
                color = AppTextPrimary,
                style = AppType.label(PERCENT_SIZE),
            )
            Text(
                text = stringResource(share.status.labelRes),
                color = AppTextSecondary,
                style = AppType.body(LABEL_SIZE),
            )
        }
    }
}

@Preview
@Composable
private fun StatusShareBarPreview() {
    AppPreview {
        StatusShareBar(shares = StatsMockData.stats.statusShare)
    }
}
