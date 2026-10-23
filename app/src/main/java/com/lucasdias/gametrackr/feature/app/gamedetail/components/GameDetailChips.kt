package com.lucasdias.gametrackr.feature.app.gamedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val CHIP_TEXT_SIZE = 13.sp
private val BORDER_WIDTH = 1.dp
private const val STAR = "★"
private const val GENRE_FILL_ALPHA = 0.12f
private const val GENRE_BORDER_ALPHA = 0.45f

@Composable
fun GameInfoChip(
    text: String,
    modifier: Modifier = Modifier,
    isRating: Boolean = false,
) {
    val shape = RoundedCornerShape(Radius.sm)
    val color = if (isRating) AppPrimary else AppTextSecondary
    Row(
        modifier =
            modifier
                .clip(shape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppOutline, shape)
                .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isRating) {
            Text(text = STAR, color = AppPrimary, fontSize = CHIP_TEXT_SIZE)
        }
        Text(text = text, color = color, style = AppType.label(CHIP_TEXT_SIZE))
    }
}

@Composable
fun GameGenreChip(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = AppPrimary,
        style = AppType.label(CHIP_TEXT_SIZE),
        modifier =
            modifier
                .clip(CircleShape)
                .background(AppPrimary.copy(alpha = GENRE_FILL_ALPHA))
                .border(BORDER_WIDTH, AppPrimary.copy(alpha = GENRE_BORDER_ALPHA), CircleShape)
                .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
    )
}

@Preview
@Composable
private fun GameDetailChipsPreview() {
    AppPreview {
        Row(
            modifier = Modifier.padding(Spacing.md),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            GameInfoChip(text = "2024")
            GameInfoChip(text = "4.8", isRating = true)
            GameGenreChip(text = "Action RPG")
        }
    }
}
