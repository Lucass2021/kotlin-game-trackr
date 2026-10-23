package com.lucasdias.gametrackr.feature.app.profile.editprofile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val TEXT_SIZE = 14.sp

@Composable
fun AvatarColorsRetry(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.edit_profile_colors_error),
            color = AppTextSecondary,
            style = AppType.body(TEXT_SIZE),
            modifier = Modifier.weight(1f),
        )
        TextButton(onClick = onRetry) {
            Text(
                text = stringResource(R.string.edit_profile_try_again),
                color = AppPrimary,
                style = AppType.label(TEXT_SIZE),
            )
        }
    }
}

@Preview
@Composable
private fun AvatarColorsRetryPreview() {
    AppPreview {
        AvatarColorsRetry(onRetry = {})
    }
}
