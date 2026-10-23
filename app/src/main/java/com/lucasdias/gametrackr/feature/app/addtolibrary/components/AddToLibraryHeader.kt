package com.lucasdias.gametrackr.feature.app.addtolibrary.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletStart
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.app.home.components.GameCoverArt

private val COVER_SIZE = 56.dp
private val TITLE_SIZE = 22.sp
private val SUBTITLE_SIZE = 14.sp

@Composable
fun AddToLibraryHeader(
    gameTitle: String,
    coverStart: Color,
    coverEnd: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        GameCoverArt(start = coverStart, end = coverEnd, width = COVER_SIZE, height = COVER_SIZE)
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxxs)) {
            Text(
                text = stringResource(R.string.add_library_title),
                color = AppTextPrimary,
                style = AppType.headline(TITLE_SIZE),
            )
            Text(
                text = gameTitle,
                color = AppTextSecondary,
                style = AppType.body(SUBTITLE_SIZE),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
private fun AddToLibraryHeaderPreview() {
    AppPreview {
        AddToLibraryHeader(gameTitle = "Neon Ascent: Revival", coverStart = CoverVioletStart, coverEnd = CoverVioletEnd)
    }
}
