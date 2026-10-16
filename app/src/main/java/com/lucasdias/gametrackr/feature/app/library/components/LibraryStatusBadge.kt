package com.lucasdias.gametrackr.feature.app.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val LABEL_SIZE = 11.sp
private val LABEL_LETTER_SPACING = 0.6.sp

@Composable
fun LibraryStatusBadge(
    status: LibraryStatus,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(status.labelRes).uppercase(),
        color = status.badgeForeground,
        fontSize = LABEL_SIZE,
        fontWeight = FontWeight.Bold,
        letterSpacing = LABEL_LETTER_SPACING,
        maxLines = 1,
        modifier =
            modifier
                .clip(CircleShape)
                .background(status.badgeBackground)
                .padding(horizontal = Spacing.xs, vertical = Spacing.xxs),
    )
}

@Preview
@Composable
private fun LibraryStatusBadgePreview() {
    AppPreview {
        LibraryStatusBadge(status = LibraryStatus.PLAYING)
    }
}
