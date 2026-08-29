package com.lucasdias.gametrackr.feature.app.community.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dismissLabel: String? = null,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppSurfaceCard,
        title = { Text(text = title, color = AppTextPrimary, style = AppType.headline(20.sp)) },
        text = { Text(text = message, color = AppTextSecondary, style = AppType.body(15.sp)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmLabel, color = AppTertiary, style = AppType.label(15.sp))
            }
        },
        dismissButton =
            dismissLabel?.let {
                {
                    TextButton(onClick = onDismiss) {
                        Text(text = it, color = AppTextSecondary, style = AppType.label(15.sp))
                    }
                }
            },
    )
}
