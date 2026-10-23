package com.lucasdias.gametrackr.feature.app.gamedetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.GameScreenshot
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.game.FakeGameRepository
import com.lucasdias.gametrackr.feature.app.home.components.GameCoverArt

private val THUMB_WIDTH = 232.dp
private val THUMB_HEIGHT = 132.dp

@Composable
fun GameScreenshotsSection(
    screenshots: List<GameScreenshot>,
    modifier: Modifier = Modifier,
) {
    var startIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    val openDescription = stringResource(R.string.game_detail_open_screenshot)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        GameSectionHeader(
            title = stringResource(R.string.game_detail_screenshots),
            modifier = Modifier.padding(horizontal = Spacing.lg),
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            contentPadding = PaddingValues(horizontal = Spacing.lg),
        ) {
            itemsIndexed(screenshots) { index, shot ->
                GameCoverArt(
                    start = shot.start,
                    end = shot.end,
                    url = shot.url,
                    width = THUMB_WIDTH,
                    height = THUMB_HEIGHT,
                    modifier =
                        Modifier
                            .clip(RoundedCornerShape(Radius.md))
                            .clickable(role = Role.Button) { startIndex = index }
                            .semantics { contentDescription = openDescription },
                )
            }
        }
    }

    startIndex?.let { index ->
        GameScreenshotPager(
            screenshots = screenshots,
            startIndex = index,
            onClose = { startIndex = null },
        )
    }
}

@Preview
@Composable
private fun GameScreenshotsSectionPreview() {
    AppPreview {
        GameScreenshotsSection(screenshots = FakeGameRepository.gameDetail.screenshots)
    }
}
