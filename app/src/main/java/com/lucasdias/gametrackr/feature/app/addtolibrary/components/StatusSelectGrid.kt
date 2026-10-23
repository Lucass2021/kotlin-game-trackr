package com.lucasdias.gametrackr.feature.app.addtolibrary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.glow
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val CHIP_HEIGHT = 64.dp
private val BORDER_WIDTH = 1.dp
private val TEXT_SIZE = 14.sp
private const val COLUMNS = 3

@Composable
fun StatusSelectGrid(
    selection: LibraryStatus,
    onSelect: (LibraryStatus) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        LibraryStatus.entries.chunked(COLUMNS).forEach { rowStatuses ->
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                rowStatuses.forEach { status ->
                    StatusChip(
                        status = status,
                        isSelected = status == selection,
                        onClick = { onSelect(status) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusChip(
    status: LibraryStatus,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(Radius.md)
    Box(
        modifier =
            modifier
                .height(CHIP_HEIGHT)
                .then(if (isSelected) Modifier.glow(color = AppPrimary, cornerRadius = Radius.md) else Modifier)
                .clip(shape)
                .background(if (isSelected) AppPrimary else AppSurfaceCard)
                .border(BORDER_WIDTH, if (isSelected) Color.Transparent else AppOutline, shape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    role = Role.Button,
                    onClick = onClick,
                ).semantics { selected = isSelected },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(status.labelRes),
            color = if (isSelected) AppOnPrimary else AppTextPrimary,
            style = AppType.label(TEXT_SIZE),
        )
    }
}

@Preview
@Composable
private fun StatusSelectGridPreview() {
    AppPreview {
        StatusSelectGrid(selection = LibraryStatus.PLAYING, onSelect = {})
    }
}
