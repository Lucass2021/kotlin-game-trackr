package com.lucasdias.gametrackr.feature.app.achievements.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.core.model.AchievementsSummary
import com.lucasdias.gametrackr.core.model.GameAchievements
import com.lucasdias.gametrackr.core.pagination.InfiniteScrollEffect
import com.lucasdias.gametrackr.core.pagination.LoadMoreErrorRow
import com.lucasdias.gametrackr.core.pagination.LoadingMoreIndicator
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.achievements.AchievementsMockData

@Composable
fun AchievementsList(
    games: List<GameAchievements>,
    summary: AchievementsSummary?,
    canLoadMore: Boolean,
    isLoadingMore: Boolean,
    loadMoreFailed: Boolean,
    onLoadMore: () -> Unit,
    onGameClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    InfiniteScrollEffect(listState = listState, canLoadMore = canLoadMore, onLoadMore = onLoadMore)

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(start = Spacing.lg, end = Spacing.lg, top = Spacing.sm, bottom = Spacing.xxl),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        if (summary != null) {
            item { AchievementsSummaryCard(summary = summary, modifier = Modifier.padding(bottom = Spacing.xxs)) }
        }

        items(games, key = { it.id }) { game ->
            val interaction = remember { MutableInteractionSource() }
            GameAchievementsRow(
                game = game,
                modifier =
                    Modifier
                        .pressScale(interaction)
                        .clickable(
                            interactionSource = interaction,
                            indication = null,
                            onClickLabel = game.title,
                            role = Role.Button,
                            onClick = { onGameClick(game.id) },
                        ),
            )
        }

        if (isLoadingMore) {
            item { LoadingMoreIndicator() }
        }

        if (loadMoreFailed) {
            item { LoadMoreErrorRow(onRetry = onLoadMore) }
        }
    }
}

@Preview
@Composable
private fun AchievementsListPreview() {
    AppPreview {
        AchievementsList(
            games = AchievementsMockData.games,
            summary = null,
            canLoadMore = false,
            isLoadingMore = false,
            loadMoreFailed = false,
            onLoadMore = {},
            onGameClick = {},
        )
    }
}
