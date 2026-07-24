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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.profile.barTint
import com.lucasdias.gametrackr.feature.app.stats.StatusShare

@Composable
fun StatusShareBar(
    shares: List<StatusShare>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(modifier = Modifier.fillMaxWidth().height(26.dp).clip(CircleShape)) {
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
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.Top,
        ) {
            shares.forEach { share -> Legend(share) }
        }
    }
}

@Composable
private fun Legend(share: StatusShare) {
    Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        Box(
            modifier =
                Modifier
                    .padding(top = 4.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(share.status.barTint),
        )

        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(
                text = "${share.percent}%",
                color = AppTextPrimary,
                style = AppType.label(13.sp),
            )
            Text(
                text = stringResource(share.status.labelRes),
                color = AppTextSecondary,
                style = AppType.body(12.sp),
            )
        }
    }
}
