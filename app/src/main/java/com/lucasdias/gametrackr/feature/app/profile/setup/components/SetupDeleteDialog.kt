package com.lucasdias.gametrackr.feature.app.profile.setup.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType

@Composable
fun SetupDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppBackground,
        title = {
            Text(
                text = stringResource(R.string.setup_delete_title),
                color = AppTextPrimary,
                style = AppType.headline(18.sp),
            )
        },
        text = {
            Text(
                text = stringResource(R.string.setup_delete_message),
                color = AppTextSecondary,
                style = AppType.body(15.sp),
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.setup_delete_confirm), color = AppTertiary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.setup_delete_cancel), color = AppTextSecondary)
            }
        },
    )
}

@Preview
@Composable
private fun SetupDeleteDialogPreview() {
    AppPreview {
        SetupDeleteDialog(onConfirm = {}, onDismiss = {})
    }
}
