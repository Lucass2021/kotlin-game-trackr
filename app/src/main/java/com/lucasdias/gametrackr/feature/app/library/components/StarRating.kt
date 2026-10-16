package com.lucasdias.gametrackr.feature.app.library.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private const val MAX_RATING = 5
private const val EMPTY_STAR_ALPHA = 0.55f
private val DEFAULT_SIZE = 15.sp

@Composable
fun StarRating(
    rating: Int,
    modifier: Modifier = Modifier,
    size: TextUnit = DEFAULT_SIZE,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(Spacing.xxxs)) {
        repeat(MAX_RATING) { index ->
            val filled = index < rating
            Text(
                text = if (filled) "★" else "☆",
                style = TextStyle(fontSize = size),
                color = if (filled) AppSecondary else AppTextSecondary.copy(alpha = EMPTY_STAR_ALPHA),
            )
        }
    }
}

@Preview
@Composable
private fun StarRatingPreview() {
    AppPreview {
        StarRating(rating = 3)
    }
}
