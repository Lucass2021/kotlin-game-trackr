package com.lucasdias.gametrackr.feature.app.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

val HOME_SECTION_HEIGHT = 240.dp
private val TEXT_SIZE = 14.sp

@Composable
fun HomeSectionRetry(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().height(HOME_SECTION_HEIGHT).padding(horizontal = Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            color = AppTextSecondary,
            style = AppType.body(TEXT_SIZE),
            textAlign = TextAlign.Center,
        )
        TextButton(onClick = onRetry) {
            Text(
                text = stringResource(R.string.home_try_again),
                color = AppPrimary,
                style = AppType.label(TEXT_SIZE),
            )
        }
    }
}

@Preview
@Composable
private fun HomeSectionRetryPreview() {
    AppPreview {
        HomeSectionRetry(message = "Couldn't load New Releases.", onRetry = {})
    }
}
