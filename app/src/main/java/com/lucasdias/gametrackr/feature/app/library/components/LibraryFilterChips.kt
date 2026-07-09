package com.lucasdias.gametrackr.feature.app.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.feature.app.library.LibraryStatus

@Composable
fun LibraryFilterChips(
    selection: LibraryStatus?,
    onSelect: (LibraryStatus?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp),
    ) {
        item {
            Chip(
                label = stringResource(R.string.library_filter_all),
                isSelected = selection == null,
                onClick = { onSelect(null) },
            )
        }
        items(LibraryStatus.entries.size) { index ->
            val status = LibraryStatus.entries[index]
            Chip(
                label = stringResource(status.labelRes),
                isSelected = selection == status,
                onClick = { onSelect(status) },
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
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
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
                    onClick = onClick,
                ).padding(horizontal = 18.dp, vertical = 9.dp),
    )
}
