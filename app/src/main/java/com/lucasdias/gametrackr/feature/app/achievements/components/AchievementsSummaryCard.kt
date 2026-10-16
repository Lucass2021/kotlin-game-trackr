package com.lucasdias.gametrackr.feature.app.achievements.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.AchievementsSummary
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val BORDER_WIDTH = 1.dp
private val BAR_HEIGHT = 10.dp
private val STAT_ICON_SIZE = 16.dp
private val COUNT_SIZE = 34.sp
private val TOTAL_SIZE = 15.sp
private val PERCENT_SIZE = 20.sp
private val STAT_VALUE_SIZE = 15.sp
private val STAT_LABEL_SIZE = 11.sp

@Composable
fun AchievementsSummaryCard(
    summary: AchievementsSummary,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(Radius.lg)
    val a11y = stringResource(R.string.achievements_summary_a11y, summary.unlockedCount, summary.total)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppOutline, shape)
                .padding(Spacing.lg)
                .semantics(mergeDescendants = true) { contentDescription = a11y },
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = summary.unlockedCount.toString(),
                color = AppPrimary,
                style = AppType.headline(COUNT_SIZE, FontWeight.ExtraBold),
            )
            Text(
                text = stringResource(R.string.achievements_summary_of, summary.total),
                color = AppTextSecondary,
                style = AppType.body(TOTAL_SIZE),
                modifier = Modifier.weight(1f),
            )
            Text(
                text = stringResource(R.string.achievements_summary_percent, summary.percent),
                color = AppTextPrimary,
                style = AppType.headline(PERCENT_SIZE),
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
                        .fillMaxWidth(summary.fraction)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(Brush.horizontalGradient(listOf(AppPrimary, AppSecondary))),
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.lg)) {
            MiniStat(
                icon = AppIcon.TROPHY,
                value = summary.platinumCount.toString(),
                label = stringResource(R.string.achievements_stat_platinums),
            )
            MiniStat(
                icon = AppIcon.SUCCESS,
                value = summary.completedCount.toString(),
                label = stringResource(R.string.achievements_stat_completed),
            )
            MiniStat(
                icon = AppIcon.BRAND,
                value = summary.gameCount.toString(),
                label = stringResource(R.string.achievements_stat_games),
            )
        }
    }
}

@Composable
private fun MiniStat(
    icon: AppIcon,
    value: String,
    label: String,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon.image(),
            contentDescription = null,
            tint = AppSecondary,
            modifier = Modifier.size(STAT_ICON_SIZE),
        )
        Column {
            Text(text = value, color = AppTextPrimary, style = AppType.label(STAT_VALUE_SIZE))
            Text(text = label, color = AppTextSecondary, style = AppType.body(STAT_LABEL_SIZE))
        }
    }
}

@Preview
@Composable
private fun AchievementsSummaryCardPreview() {
    AppPreview {
        AchievementsSummaryCard(
            summary = AchievementsSummary(unlockedCount = 42, total = 60, platinumCount = 2, completedCount = 3, gameCount = 6),
        )
    }
}
