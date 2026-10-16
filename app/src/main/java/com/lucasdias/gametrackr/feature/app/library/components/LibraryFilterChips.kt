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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val BORDER_WIDTH = 1.dp
private val LABEL_SIZE = 14.sp

@Composable
fun LibraryFilterChips(
    selection: LibraryStatus?,
    onSelect: (LibraryStatus?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = Spacing.lg),
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
        fontSize = LABEL_SIZE,
        fontWeight = FontWeight.SemiBold,
        modifier =
            Modifier
                .semantics { selected = isSelected }
                .padding(end = Spacing.xs)
                .pressScale(interactionSource)
                .clip(CircleShape)
                .background(if (isSelected) AppPrimary else Color.Transparent)
                .border(
                    width = BORDER_WIDTH,
                    color = if (isSelected) Color.Transparent else AppOutline,
                    shape = CircleShape,
                ).clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Tab,
                    onClick = onClick,
                ).padding(horizontal = Spacing.md, vertical = Spacing.xs),
    )
}

@Preview
@Composable
private fun LibraryFilterChipsPreview() {
    AppPreview {
        LibraryFilterChips(selection = LibraryStatus.PLAYING, onSelect = {})
    }
}
