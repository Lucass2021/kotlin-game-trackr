package com.lucasdias.gametrackr.feature.app.community.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.CommunitySegment

@Composable
fun CommunitySegmentControl(
    selection: CommunitySegment,
    onSelect: (CommunitySegment) -> Unit,
    modifier: Modifier = Modifier,
) {
    val outerShape = CircleShape
    Row(
        modifier =
            modifier
                .padding(horizontal = 20.dp)
                .clip(outerShape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, outerShape)
                .padding(4.dp),
    ) {
        CommunitySegment.entries.forEach { segment ->
            SegmentButton(
                label = segment.label,
                isSelected = selection == segment,
                onClick = { onSelect(segment) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun SegmentButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = CircleShape
    Text(
        text = label,
        color = if (isSelected) AppOnPrimary else AppTextSecondary,
        style = AppType.label(15.sp),
        textAlign = TextAlign.Center,
        modifier =
            modifier
                .clip(shape)
                .background(if (isSelected) AppPrimary else Color.Transparent)
                .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
                .padding(vertical = 11.dp),
    )
}
