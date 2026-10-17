package com.lucasdias.gametrackr.feature.app.community.communitydetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.app.community.CommunityDetailTab

private val DIVIDER_THICKNESS = 1.dp
private val INDICATOR_HEIGHT = 2.dp
private val LABEL_SIZE = 15.sp

@Composable
fun CommunityDetailTabs(
    selection: CommunityDetailTab,
    onSelect: (CommunityDetailTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth().padding(horizontal = Spacing.lg)) {
        HorizontalDivider(
            thickness = DIVIDER_THICKNESS,
            color = AppOutline,
            modifier = Modifier.align(Alignment.BottomStart),
        )
        Row {
            CommunityDetailTab.entries.forEach { tab ->
                TabButton(label = tab.label, isSelected = selection == tab, onClick = { onSelect(tab) })
            }
        }
    }
}

@Composable
private fun TabButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier =
            Modifier
                .width(IntrinsicSize.Max)
                .pressScale(interactionSource)
                .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            color = if (isSelected) AppPrimary else AppTextSecondary,
            style = AppType.label(LABEL_SIZE),
            modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm),
        )
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(INDICATOR_HEIGHT)
                    .background(if (isSelected) AppPrimary else Color.Transparent),
        )
    }
}

@Preview
@Composable
private fun CommunityDetailTabsPreview() {
    AppPreview {
        CommunityDetailTabs(selection = CommunityDetailTab.POSTS, onSelect = {})
    }
}
