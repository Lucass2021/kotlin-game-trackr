package com.lucasdias.gametrackr.feature.app.addtolibrary.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val CARET_SIZE = 16.dp
private val TEXT_SIZE = 16.sp

@Composable
fun PlatformPicker(
    selection: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fieldBox()
                    .height(FIELD_HEIGHT)
                    .clickable(role = Role.Button) { expanded = true }
                    .padding(horizontal = Spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = selection, color = AppTextPrimary, style = AppType.body(TEXT_SIZE), modifier = Modifier.weight(1f))
            Icon(
                imageVector = AppIcon.CARET_DOWN.image(),
                contentDescription = null,
                tint = AppTextSecondary,
                modifier = Modifier.size(CARET_SIZE),
            )
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, containerColor = AppSurfaceCard) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option, color = if (option == selection) AppPrimary else AppTextPrimary) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun PlatformPickerPreview() {
    AppPreview {
        PlatformPicker(selection = "PS5", options = listOf("PS5", "PC"), onSelect = {})
    }
}
