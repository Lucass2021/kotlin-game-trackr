package com.lucasdias.gametrackr.feature.app.settings.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType

private val TITLE_SIZE = 18.sp
private val MESSAGE_SIZE = 15.sp

@Composable
fun DeleteAccountDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.settings_delete_dialog_title),
                color = AppTextPrimary,
                style = AppType.headline(TITLE_SIZE),
            )
        },
        text = {
            Text(
                text = stringResource(R.string.settings_delete_dialog_message),
                color = AppTextSecondary,
                style = AppType.body(MESSAGE_SIZE),
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.settings_delete_dialog_confirm), color = AppTertiary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.settings_delete_dialog_cancel), color = AppTextSecondary)
            }
        },
        containerColor = AppSurfaceCard,
    )
}

@Preview
@Composable
private fun DeleteAccountDialogPreview() {
    AppPreview {
        DeleteAccountDialog(onConfirm = {}, onDismiss = {})
    }
}
