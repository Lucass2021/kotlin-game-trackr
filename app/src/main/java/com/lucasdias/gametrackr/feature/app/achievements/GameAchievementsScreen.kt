package com.lucasdias.gametrackr.feature.app.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Achievement
import com.lucasdias.gametrackr.core.model.GameAchievements
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.achievements.AchievementsMockData
import com.lucasdias.gametrackr.feature.app.achievements.components.AchievementRow
import com.lucasdias.gametrackr.feature.app.achievements.components.GameAchievementsHeader
import com.lucasdias.gametrackr.feature.app.stats.components.StatsSectionCard
import com.lucasdias.gametrackr.feature.app.stats.components.StatsTopBar
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun GameAchievementsScreen(
    gameId: Long,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameAchievementsViewModel = koinViewModel { parametersOf(gameId) },
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) { viewModel.loadIfNeeded() }

    GameAchievementsScreen(
        state = state,
        onBack = onBack,
        onRetry = viewModel::load,
        modifier = modifier,
    )
}

@Composable
private fun GameAchievementsScreen(
    state: GameAchievementsUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(AppBackground)) {
        StatsTopBar(
            title = stringResource(R.string.achievements_title),
            subtitle = state.game?.title,
            onBack = onBack,
        )

        val game = state.game
        when {
            game != null -> {
                GameAchievementsContent(
                    game = game,
                    unlocked = state.unlockedAchievements,
                    locked = state.lockedAchievements,
                )
            }

            state.hasError -> {
                EmptyState(
                    icon = AppIcon.INFO,
                    title = stringResource(R.string.achievements_load_error_title),
                    message = stringResource(R.string.achievements_load_error_message),
                    actionTitle = stringResource(R.string.achievements_retry),
                    onAction = onRetry,
                    modifier = Modifier.weight(1f),
                )
            }

            else -> {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppPrimary)
                }
            }
        }
    }
}

@Composable
private fun GameAchievementsContent(
    game: GameAchievements,
    unlocked: List<Achievement>,
    locked: List<Achievement>,
) {
    LazyColumn(
        contentPadding =
            PaddingValues(start = Spacing.lg, end = Spacing.lg, top = Spacing.sm, bottom = Spacing.xxl),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
    ) {
        item { GameAchievementsHeader(game = game) }

        if (unlocked.isNotEmpty()) {
            item {
                StatsSectionCard(
                    title = stringResource(R.string.achievements_section_unlocked),
                    icon = AppIcon.SUCCESS,
                ) {
                    AchievementList(unlocked)
                }
            }
        }

        if (locked.isNotEmpty()) {
            item {
                StatsSectionCard(
                    title = stringResource(R.string.achievements_section_locked),
                    icon = AppIcon.EYE_SLASH,
                ) {
                    AchievementList(locked)
                }
            }
        }
    }
}

@Composable
private fun AchievementList(achievements: List<Achievement>) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        achievements.forEach { AchievementRow(achievement = it) }
    }
}

@Preview
@Composable
private fun GameAchievementsScreenPreview() {
    AppPreview {
        GameAchievementsScreen(
            state = GameAchievementsUiState(game = AchievementsMockData.games[4]),
            onBack = {},
            onRetry = {},
        )
    }
}

@Preview
@Composable
private fun GameAchievementsScreenErrorPreview() {
    AppPreview {
        GameAchievementsScreen(
            state = GameAchievementsUiState(hasError = true),
            onBack = {},
            onRetry = {},
        )
    }
}
