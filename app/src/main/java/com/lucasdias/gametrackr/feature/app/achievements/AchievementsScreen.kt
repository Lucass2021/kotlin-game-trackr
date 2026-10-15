package com.lucasdias.gametrackr.feature.app.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.pagination.InfiniteScrollEffect
import com.lucasdias.gametrackr.core.pagination.LoadingMoreIndicator
import com.lucasdias.gametrackr.core.pagination.PaginationState
import com.lucasdias.gametrackr.core.ui.anim.subtleBounce
import com.lucasdias.gametrackr.core.ui.components.glow
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.data.achievements.AchievementsRepository
import com.lucasdias.gametrackr.feature.app.achievements.components.AchievementFilterChips
import com.lucasdias.gametrackr.feature.app.achievements.components.AchievementsSummaryCard
import com.lucasdias.gametrackr.feature.app.achievements.components.GameAchievementsRow
import com.lucasdias.gametrackr.feature.app.stats.components.StatsTopBar
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

private const val GAMES_PAGE_SIZE = 3

@Composable
fun AchievementsScreen(
    onBack: () -> Unit,
    onGameClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    ownerName: String? = null,
    repository: AchievementsRepository = koinInject(),
) {
    var filter by rememberSaveable { mutableStateOf(AchievementFilter.ALL) }
    val pagination = remember { PaginationState<GameAchievements>() }
    var summary by remember { mutableStateOf<AchievementsSummary?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun loadNextPage() {
        if (!pagination.canLoadMore) return
        pagination.setLoading(true)
        coroutineScope.launch {
            repository
                .fetchAchievementGames(page = pagination.currentPage + 1, perPage = GAMES_PAGE_SIZE)
                .onSuccess { pagination.append(it) }
            pagination.setLoading(false)
        }
    }

    LaunchedEffect(repository) {
        loadNextPage()
        summary = repository.fetchAchievementsSummary().getOrNull()
    }

    val visible = remember(pagination.items.size, filter) { pagination.items.filter(filter::matches) }

    Column(modifier = modifier.fillMaxSize().background(AppBackground)) {
        StatsTopBar(
            title = stringResource(R.string.achievements_title),
            subtitle = ownerName,
            onBack = onBack,
        )

        AchievementFilterChips(
            selection = filter,
            onSelect = { filter = it },
            modifier = Modifier.padding(bottom = 4.dp),
        )

        if (pagination.currentPage == 0) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppPrimary)
            }
            return@Column
        }

        if (visible.isEmpty()) {
            EmptyState()
            return@Column
        }

        val listState = rememberLazyListState()

        InfiniteScrollEffect(
            listState = listState,
            canLoadMore = pagination.canLoadMore,
            onLoadMore = ::loadNextPage,
        )

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            val loadedSummary = summary
            if (filter == AchievementFilter.ALL && loadedSummary != null) {
                item {
                    AchievementsSummaryCard(summary = loadedSummary, modifier = Modifier.padding(bottom = 6.dp))
                }
            }

            items(visible, key = { it.id }) { game ->
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

            if (pagination.isLoadingMore) {
                item { LoadingMoreIndicator() }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .subtleBounce()
                    .size(104.dp)
                    .glow(color = AppPrimary, cornerRadius = 26.dp, blurRadius = 34.dp, alpha = 0.35f)
                    .clip(RoundedCornerShape(26.dp))
                    .background(AppSurfaceCard),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcon.TROPHY.image(),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(48.dp),
            )
        }

        Text(
            text = stringResource(R.string.achievements_empty_title),
            color = AppTextPrimary,
            style = AppType.headline(20.sp),
            modifier = Modifier.padding(top = 24.dp),
        )

        Text(
            text = stringResource(R.string.achievements_empty_message),
            color = AppTextSecondary,
            style = AppType.body(15.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp),
        )
    }
}
