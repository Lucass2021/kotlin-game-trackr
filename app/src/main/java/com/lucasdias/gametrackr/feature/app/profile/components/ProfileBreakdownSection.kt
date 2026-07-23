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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.library.LibraryStatus
import com.lucasdias.gametrackr.feature.app.profile.StatusCount
import com.lucasdias.gametrackr.feature.app.profile.barTint

@Composable
fun ProfileBreakdownSection(
    breakdown: List<StatusCount>,
    onSelect: (LibraryStatus) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(18.dp)
    val maxCount = (breakdown.maxOfOrNull { it.count } ?: 0).coerceAtLeast(1)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
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
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = label,
            color = AppTextPrimary,
            style = AppType.body(14.sp),
            modifier = Modifier.width(84.dp),
        )

        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .height(7.dp)
                    .clip(CircleShape)
                    .background(AppOutline),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .fractionWidthAtLeast(item.count.toFloat() / maxCount, 6.dp)
                        .clip(CircleShape)
                        .background(item.status.barTint),
            )
        }

        Text(
            text = item.count.toString(),
            color = AppTextSecondary,
            style = AppType.label(14.sp),
            textAlign = TextAlign.End,
            modifier = Modifier.width(30.dp),
        )
    }
}

private fun Modifier.fractionWidthAtLeast(
    fraction: Float,
    minimum: androidx.compose.ui.unit.Dp,
) = layout { measurable, constraints ->
    val minPx = minimum.roundToPx()
    val target = (constraints.maxWidth * fraction).toInt().coerceIn(minPx, constraints.maxWidth)
    val placeable = measurable.measure(constraints.copy(minWidth = target, maxWidth = target))
    layout(placeable.width, placeable.height) { placeable.placeRelative(0, 0) }
}
