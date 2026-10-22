package com.lucasdias.gametrackr.feature.app.community.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType

private val TITLE_SIZE = 20.sp
private val BODY_SIZE = 15.sp

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
        title = { Text(text = title, color = AppTextPrimary, style = AppType.headline(TITLE_SIZE)) },
        text = { Text(text = message, color = AppTextSecondary, style = AppType.body(BODY_SIZE)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmLabel, color = AppTertiary, style = AppType.label(BODY_SIZE))
            }
        },
        dismissButton =
            dismissLabel?.let {
                {
                    TextButton(onClick = onDismiss) {
                        Text(text = it, color = AppTextSecondary, style = AppType.label(BODY_SIZE))
                    }
                }
            },
    )
}

@Preview
@Composable
private fun ConfirmDialogPreview() {
    AppPreview {
        ConfirmDialog(
            title = "Leave this community?",
            message = "You'll stop seeing its posts in your feed.",
            confirmLabel = "Leave",
            dismissLabel = "Cancel",
            onConfirm = {},
            onDismiss = {},
        )
    }
}
