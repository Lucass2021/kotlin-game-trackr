package com.lucasdias.gametrackr.feature.app.achievements.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.achievements.AchievementFilter

@Composable
fun AchievementFilterChips(
    selection: AchievementFilter,
    onSelect: (AchievementFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp),
    ) {
        items(AchievementFilter.entries) { filter ->
            Chip(
                label = stringResource(filter.titleRes),
                isSelected = filter == selection,
                onClick = { onSelect(filter) },
            )
        }
    }
}

@Composable
private fun Chip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Text(
        text = label,
        color = if (isSelected) AppOnPrimary else AppTextPrimary,
        style = AppType.label(14.sp),
        modifier =
            Modifier
                .padding(end = 10.dp)
                .pressScale(interactionSource)
                .clip(CircleShape)
                .background(if (isSelected) AppPrimary else Color.Transparent)
                .border(
                    width = 1.dp,
                    color = if (isSelected) Color.Transparent else AppOutline,
                    shape = CircleShape,
                ).clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = label,
                    role = Role.RadioButton,
                    onClick = onClick,
                ).padding(horizontal = 18.dp, vertical = 9.dp),
    )
}
