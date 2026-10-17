package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.model.StatusCount
import com.lucasdias.gametrackr.core.model.barTint
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.profile.ProfileMockData

private val BORDER_WIDTH = 1.dp
private val LABEL_WIDTH = 84.dp
private val COUNT_WIDTH = 30.dp
private val BAR_HEIGHT = 7.dp
private val BAR_MIN_WIDTH = 6.dp
private val LABEL_SIZE = 14.sp

@Composable
fun ProfileBreakdownSection(
    breakdown: List<StatusCount>,
    onSelect: (LibraryStatus) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(Radius.lg)
    val maxCount = (breakdown.maxOfOrNull { it.count } ?: 0).coerceAtLeast(1)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppOutline, shape)
                .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        breakdown.forEach { item ->
            BreakdownRow(item = item, maxCount = maxCount, onSelect = onSelect)
        }
    }
}

@Composable
private fun BreakdownRow(
    item: StatusCount,
    maxCount: Int,
    onSelect: (LibraryStatus) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val label = stringResource(item.status.labelRes)

    Row(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = label,
                    role = Role.Button,
                    onClick = { onSelect(item.status) },
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        Text(
            text = label,
            color = AppTextPrimary,
            style = AppType.body(LABEL_SIZE),
            modifier = Modifier.width(LABEL_WIDTH),
        )

        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .height(BAR_HEIGHT)
                    .clip(CircleShape)
                    .background(AppOutline),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .fractionWidthAtLeast(item.count.toFloat() / maxCount, BAR_MIN_WIDTH)
                        .clip(CircleShape)
                        .background(item.status.barTint),
            )
        }

        Text(
            text = item.count.toString(),
            color = AppTextSecondary,
            style = AppType.label(LABEL_SIZE),
            textAlign = TextAlign.End,
            modifier = Modifier.width(COUNT_WIDTH),
        )
    }
}

private fun Modifier.fractionWidthAtLeast(
    fraction: Float,
    minimum: Dp,
) = layout { measurable, constraints ->
    val minPx = minimum.roundToPx()
    val target = (constraints.maxWidth * fraction).toInt().coerceIn(minPx, constraints.maxWidth)
    val placeable = measurable.measure(constraints.copy(minWidth = target, maxWidth = target))
    layout(placeable.width, placeable.height) { placeable.placeRelative(0, 0) }
}

@Preview
@Composable
private fun ProfileBreakdownSectionPreview() {
    AppPreview {
        ProfileBreakdownSection(breakdown = ProfileMockData.breakdown, onSelect = {})
    }
}
