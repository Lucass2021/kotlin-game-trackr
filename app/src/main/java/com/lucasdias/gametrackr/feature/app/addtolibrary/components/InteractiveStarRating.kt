package com.lucasdias.gametrackr.feature.app.addtolibrary.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary

@Composable
fun InteractiveStarRating(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        (1..5).forEach { index ->
            val filled = index <= rating
            val interaction = remember { MutableInteractionSource() }
            Text(
                text = if (filled) "★" else "☆",
                style = TextStyle(fontSize = 30.sp),
                color = if (filled) AppSecondary else AppTextSecondary.copy(alpha = 0.5f),
                modifier =
                    Modifier.clickable(
                        interactionSource = interaction,
                        indication = null,
                    ) { onRatingChange(if (rating == index) index - 1 else index) },
            )
        }
    }
}
