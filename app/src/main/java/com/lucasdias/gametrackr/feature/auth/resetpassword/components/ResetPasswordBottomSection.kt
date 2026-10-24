package com.lucasdias.gametrackr.feature.auth.resetpassword.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton

@Composable
fun ResetPasswordBottomSection(
    isLoading: Boolean,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrimaryButton(
        text = stringResource(R.string.reset_save),
        onClick = onSave,
        isLoading = isLoading,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun ResetPasswordBottomSectionPreview() {
    AppPreview {
        ResetPasswordBottomSection(isLoading = false, onSave = {})
    }
}
