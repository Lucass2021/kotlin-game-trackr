package com.lucasdias.gametrackr.feature.app.library.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary

@Composable
fun StarRating(
    rating: Int,
    modifier: Modifier = Modifier,
    size: TextUnit = 15.sp,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        repeat(5) { index ->
            val filled = index < rating
            Text(
                text = if (filled) "★" else "☆",
                style = TextStyle(fontSize = size),
                color = if (filled) AppSecondary else AppTextSecondary.copy(alpha = 0.55f),
            )
        }
    }
}
