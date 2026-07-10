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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(
    dateMillis: Long?,
    onDateChange: (Long?) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    var showDialog by remember { mutableStateOf(false) }
    val formatter =
        remember {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("UTC") }
        }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .fieldBox()
                .height(50.dp)
                .clickable { showDialog = true }
                .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = dateMillis?.let { formatter.format(it) } ?: placeholder,
            color = if (dateMillis == null) AppTextSecondary else AppTextPrimary,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = AppIcon.CALENDAR.image(),
            contentDescription = null,
            tint = AppTextSecondary,
            modifier = Modifier.size(18.dp),
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
                    Text("Done", color = AppPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDateChange(null)
                    showDialog = false
                }) {
                    Text("Clear", color = AppTextSecondary)
                }
            },
        ) {
            DatePicker(state = state, showModeToggle = false)
        }
    }
}
