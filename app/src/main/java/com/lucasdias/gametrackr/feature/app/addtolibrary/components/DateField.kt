package com.lucasdias.gametrackr.feature.app.addtolibrary.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private val ICON_SIZE = 18.dp
private val TEXT_SIZE = 16.sp
private const val DATE_PATTERN = "dd/MM/yyyy"
private const val UTC = "UTC"
private const val PREVIEW_DATE = 1_710_417_600_000L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(
    dateMillis: Long?,
    onDateChange: (Long?) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
) {
    var showDialog by remember { mutableStateOf(false) }
    val formatter = remember { SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone(UTC) } }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .fieldBox(isError)
                .height(FIELD_HEIGHT)
                .clickable(role = Role.Button) { showDialog = true }
                .padding(horizontal = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = dateMillis?.let { formatter.format(it) } ?: placeholder,
            color = if (dateMillis == null) AppTextSecondary else AppTextPrimary,
            style = AppType.body(TEXT_SIZE),
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = AppIcon.CALENDAR.image(),
            contentDescription = null,
            tint = AppTextSecondary,
            modifier = Modifier.size(ICON_SIZE),
        )
    }

    if (showDialog) {
        val state = rememberDatePickerState(initialSelectedDateMillis = dateMillis)
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    onDateChange(state.selectedDateMillis)
                    showDialog = false
                }) {
                    Text(stringResource(R.string.add_library_date_done), color = AppPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDateChange(null)
                    showDialog = false
                }) {
                    Text(stringResource(R.string.add_library_date_clear), color = AppTextSecondary)
                }
            },
        ) {
            DatePicker(state = state, showModeToggle = false)
        }
    }
}

@Preview
@Composable
private fun DateFieldPreview() {
    AppPreview {
        DateField(dateMillis = PREVIEW_DATE, onDateChange = {}, placeholder = "dd/mm/yyyy", isError = true)
    }
}
