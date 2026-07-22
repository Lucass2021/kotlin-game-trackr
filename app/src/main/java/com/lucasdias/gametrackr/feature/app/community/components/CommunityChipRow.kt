package com.lucasdias.gametrackr.feature.app.community.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType

@Composable
fun CommunityChipRow(
    titles: List<String>,
    selection: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement =
            androidx.compose.foundation.layout.Arrangement
                .spacedBy(10.dp),
    ) {
        items(titles) { title ->
            Chip(title = title, isSelected = selection == title, onClick = { onSelect(title) })
        }
    }
}

@Composable
private fun Chip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = CircleShape
    Text(
        text = title,
        color = if (isSelected) AppOnPrimary else AppTextPrimary,
        style = AppType.label(14.sp),
        modifier =
            Modifier
                .pressScale(interactionSource)
                .clip(shape)
                .background(if (isSelected) AppPrimary else Color.Transparent)
                .then(if (isSelected) Modifier else Modifier.border(1.dp, AppOutline, shape))
                .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
                .padding(horizontal = 18.dp, vertical = 9.dp),
    )
}
