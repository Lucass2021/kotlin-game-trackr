package com.lucasdias.gametrackr.feature.app.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.game.HomeMockData
import com.lucasdias.gametrackr.feature.app.home.HomeFeed

@Composable
fun HomeFeedSection(
    title: String,
    feed: HomeFeed,
    onViewAll: () -> Unit,
    onRetry: () -> Unit,
    onGameClick: (Game) -> Unit,
    modifier: Modifier = Modifier,
    card: @Composable (Game) -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        HomeSectionHeader(title = title, onViewAll = onViewAll)
        when {
            feed.isWaiting -> {
                Box(
                    modifier = Modifier.fillMaxWidth().height(HOME_SECTION_HEIGHT),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = AppPrimary)
                }
            }

            feed.games.isEmpty() -> {
                HomeSectionRetry(
                    message =
                        stringResource(if (feed.hasError) R.string.home_feed_error else R.string.home_feed_empty, title),
                    onRetry = onRetry,
                )
            }

            else -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                    contentPadding = PaddingValues(horizontal = Spacing.lg),
                ) {
                    items(feed.games) { game ->
                        val interactionSource = remember { MutableInteractionSource() }
                        Box(
                            modifier =
                                Modifier
                                    .pressScale(interactionSource)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null,
                                        role = Role.Button,
                                        onClick = { onGameClick(game) },
                                    ),
                        ) {
                            card(game)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeFeedSectionPreview() {
    AppPreview {
        HomeFeedSection(
            title = "New Releases",
            feed = HomeFeed(games = listOf(HomeMockData.sampleGame), hasLoaded = true),
            onViewAll = {},
            onRetry = {},
            onGameClick = {},
        ) { game -> NewReleaseCard(game = game) }
    }
}
