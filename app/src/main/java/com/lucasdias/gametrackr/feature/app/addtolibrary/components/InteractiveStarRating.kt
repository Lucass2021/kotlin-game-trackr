package com.lucasdias.gametrackr.feature.app.addtolibrary.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val STAR_SIZE = 30.sp
private const val MAX_RATING = 5
private const val EMPTY_ALPHA = 0.5f
private const val FILLED_STAR = "★"
private const val EMPTY_STAR = "☆"
private const val PREVIEW_RATING = 4

@Composable
fun InteractiveStarRating(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        (1..MAX_RATING).forEach { index ->
            val filled = index <= rating
            val description = stringResource(R.string.add_library_rate_stars, index, MAX_RATING)
            Text(
                text = if (filled) FILLED_STAR else EMPTY_STAR,
                style = TextStyle(fontSize = STAR_SIZE),
                color = if (filled) AppSecondary else AppTextSecondary.copy(alpha = EMPTY_ALPHA),
                modifier =
                    Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            role = Role.Button,
                        ) { onRatingChange(if (rating == index) index - 1 else index) }
                        .semantics { contentDescription = description },
            )
        }
    }
}

@Preview
@Composable
private fun InteractiveStarRatingPreview() {
    AppPreview {
        InteractiveStarRating(rating = PREVIEW_RATING, onRatingChange = {})
    }
}
