package com.lucasdias.gametrackr.feature.app.profile.editprofile.components

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

private val TITLE_SIZE = 18.sp

@Composable
fun DiscardChangesDialog(
    onDiscard: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppBackground,
        title = {
            Text(
                text = stringResource(R.string.edit_profile_discard_title),
                color = AppTextPrimary,
                style = AppType.headline(TITLE_SIZE),
            )
        },
        confirmButton = {
            TextButton(onClick = onDiscard) {
                Text(text = stringResource(R.string.edit_profile_discard_confirm), color = AppTertiary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.edit_profile_discard_cancel), color = AppTextSecondary)
            }
        },
    )
}

@Preview
@Composable
private fun DiscardChangesDialogPreview() {
    AppPreview {
        DiscardChangesDialog(onDiscard = {}, onDismiss = {})
    }
}
