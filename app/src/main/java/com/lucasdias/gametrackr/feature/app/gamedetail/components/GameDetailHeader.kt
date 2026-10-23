package com.lucasdias.gametrackr.feature.app.gamedetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.GameDetail
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.components.SecondaryButton
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.game.FakeGameRepository
import java.util.Locale

private val TITLE_SIZE = 30.sp
private val TITLE_LINE_HEIGHT = 36.sp
private const val RATING_FORMAT = "%.1f"

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameDetailHeader(
    game: GameDetail,
    isGuest: Boolean,
    onAddToLibrary: () -> Unit,
    modifier: Modifier = Modifier,
    isInLibrary: Boolean = false,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Text(
            text = game.title,
            color = AppTextPrimary,
            style = AppType.headline(TITLE_SIZE),
            lineHeight = TITLE_LINE_HEIGHT,
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            GameInfoChip(text = game.year)
            game.rating?.let { GameInfoChip(text = String.format(Locale.US, RATING_FORMAT, it), isRating = true) }
            game.platforms.forEach { GameInfoChip(text = it) }
        }

        if (game.genres.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                game.genres.forEach { GameGenreChip(text = it) }
            }
        }

        if (!isGuest) {
            if (isInLibrary) {
                SecondaryButton(text = stringResource(R.string.game_detail_in_library), onClick = onAddToLibrary)
            } else {
                PrimaryButton(
                    text = stringResource(R.string.game_detail_add_to_library),
                    onClick = onAddToLibrary,
                    icon = AppIcon.ADD_TO_LIBRARY,
                )
            }
        }
    }
}

@Preview
@Composable
private fun GameDetailHeaderPreview() {
    AppPreview {
        GameDetailHeader(game = FakeGameRepository.gameDetail, isGuest = false, onAddToLibrary = {})
    }
}

@Preview
@Composable
private fun GameDetailHeaderInLibraryPreview() {
    AppPreview {
        GameDetailHeader(game = FakeGameRepository.gameDetail, isGuest = false, onAddToLibrary = {}, isInLibrary = true)
    }
}

@Preview
@Composable
private fun GameDetailHeaderGuestPreview() {
    AppPreview {
        GameDetailHeader(game = FakeGameRepository.gameDetail, isGuest = true, onAddToLibrary = {})
    }
}
