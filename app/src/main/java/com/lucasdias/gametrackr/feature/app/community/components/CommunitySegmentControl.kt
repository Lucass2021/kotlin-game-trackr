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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.app.community.CommunitySegment

private val BORDER_WIDTH = 1.dp
private val LABEL_SIZE = 15.sp

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
                .padding(horizontal = Spacing.lg)
                .clip(outerShape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppOutline, outerShape)
                .padding(Spacing.xxs),
    ) {
        CommunitySegment.entries.forEach { segment ->
            SegmentButton(
                label = stringResource(segment.labelRes),
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
        style = AppType.label(LABEL_SIZE),
        textAlign = TextAlign.Center,
        modifier =
            modifier
                .clip(shape)
                .background(if (isSelected) AppPrimary else Color.Transparent)
                .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
                .padding(vertical = Spacing.sm),
    )
}

@Preview
@Composable
private fun CommunitySegmentControlPreview() {
    AppPreview {
        CommunitySegmentControl(selection = CommunitySegment.MY_FEED, onSelect = {})
    }
}
