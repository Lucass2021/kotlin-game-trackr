package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.format.abbreviated
import com.lucasdias.gametrackr.core.model.ProfileStats
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.profile.ProfileMockData

private val BORDER_WIDTH = 1.dp
private val FORWARD_ICON_SIZE = 14.dp
private val DIVIDER_HEIGHT = 34.dp
private val VALUE_SIZE = 19.sp
private val CAPTION_SIZE = 12.sp
private val LABEL_SIZE = 11.sp
private val LABEL_TRACKING = 0.8.sp

@Composable
fun ProfileStatsBar(
    stats: ProfileStats,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val shape = RoundedCornerShape(Radius.lg)
    val interactionSource = remember { MutableInteractionSource() }
    val viewStatsLabel = stringResource(R.string.stats_view_full)

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .then(if (onClick == null) Modifier else Modifier.pressScale(interactionSource))
                .clip(shape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppOutline, shape)
                .then(
                    if (onClick == null) {
                        Modifier
                    } else {
                        Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClickLabel = viewStatsLabel,
                            role = Role.Button,
                            onClick = onClick,
                        )
                    },
                ),
    ) {
        StatsRow(stats)

        if (onClick != null) {
            Icon(
                imageVector = AppIcon.FORWARD.image(),
                contentDescription = null,
                tint = AppTextSecondary,
                modifier = Modifier.align(Alignment.TopEnd).padding(Spacing.sm).size(FORWARD_ICON_SIZE),
            )
        }
    }
}

@Composable
private fun StatsRow(stats: ProfileStats) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = Spacing.md),
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
    Box(modifier = Modifier.width(BORDER_WIDTH).height(DIVIDER_HEIGHT).background(AppOutline))
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
        verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xxs), verticalAlignment = Alignment.Bottom) {
            Text(text = value, color = AppTextPrimary, style = AppType.headline(VALUE_SIZE))
            if (caption != null) {
                Text(
                    text = caption,
                    color = AppPrimary,
                    style = AppType.body(CAPTION_SIZE),
                    modifier = Modifier.padding(bottom = Spacing.xxxs),
                )
            }
        }

        Text(
            text = label.uppercase(),
            color = AppTextSecondary,
            style = AppType.label(LABEL_SIZE),
            letterSpacing = LABEL_TRACKING,
        )
    }
}

@Preview
@Composable
private fun ProfileStatsBarPreview() {
    AppPreview {
        ProfileStatsBar(stats = ProfileMockData.stats, onClick = {})
    }
}
