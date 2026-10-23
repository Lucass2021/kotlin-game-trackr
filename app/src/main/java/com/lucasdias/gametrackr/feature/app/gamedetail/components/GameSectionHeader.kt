package com.lucasdias.gametrackr.feature.app.gamedetail.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType

private val TITLE_SIZE = 22.sp

@Composable
fun GameSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        color = AppTextPrimary,
        style = AppType.headline(TITLE_SIZE),
        modifier = modifier.fillMaxWidth(),
    )
}

@Preview
@Composable
private fun GameSectionHeaderPreview() {
    AppPreview {
        GameSectionHeader(title = "Screenshots")
    }
}
