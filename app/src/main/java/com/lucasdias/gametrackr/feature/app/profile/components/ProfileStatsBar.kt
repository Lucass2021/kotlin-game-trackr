package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.format.abbreviated
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.profile.ProfileStats

@Composable
fun ProfileStatsBar(
    stats: ProfileStats,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .padding(vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Stat(
            value = stats.totalGames.toString(),
            label = stringResource(R.string.profile_stat_games),
            modifier = Modifier.weight(1f),
        )
        Divider()
        Stat(
            value = stats.hours.abbreviated(),
            label = stringResource(R.string.profile_stat_hours),
            modifier = Modifier.weight(1f),
        )
        Divider()
        Stat(
            value = stats.platinum.toString(),
            label = stringResource(R.string.profile_stat_platinum),
            caption = "${stats.platinumRate}%",
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun Divider() {
    Box(modifier = Modifier.width(1.dp).height(34.dp).background(AppOutline))
}

@Composable
private fun Stat(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    caption: String? = null,
) {
    Column(
        modifier = modifier.clearAndSetSemantics {},
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.Bottom) {
            Text(text = value, color = AppTextPrimary, style = AppType.headline(19.sp))
            if (caption != null) {
                Text(
                    text = caption,
                    color = AppPrimary,
                    style = AppType.body(12.sp),
                    modifier = Modifier.padding(bottom = 2.dp),
                )
            }
        }

        Text(
            text = label.uppercase(),
            color = AppTextSecondary,
            style = AppType.label(11.sp),
            letterSpacing = 0.8.sp,
        )
    }
}
