package com.lucasdias.gametrackr.feature.app.gamedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.GameDetail
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.shareText
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.game.FakeGameRepository
import com.lucasdias.gametrackr.feature.app.addtolibrary.AddToLibrarySheet
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameAboutSection
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameDetailHeader
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameDetailHero
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameScreenshotsSection
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameSpecificationsSection
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private val BACK_BUTTON_SIZE = 44.dp
private val BACK_ICON_SIZE = 20.dp

@Composable
fun GameDetailScreen(
    onBack: () -> Unit,
    slug: String,
    isGuest: Boolean = false,
) {
    val viewModel: GameDetailViewModel = koinViewModel { parametersOf(slug) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) { viewModel.loadIfNeeded() }

    GameDetailScreen(
        state = state,
        isGuest = isGuest,
        onBack = onBack,
        onRetry = viewModel::load,
    )
}

@Composable
private fun GameDetailScreen(
    state: GameDetailUiState,
    isGuest: Boolean,
    onBack: () -> Unit = {},
    onRetry: () -> Unit = {},
) {
    val game = state.game
    if (game != null) {
        GameDetailContent(game = game, isGuest = isGuest, onBack = onBack)
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        if (state.hasError) {
            EmptyState(
                icon = AppIcon.INFO,
                title = stringResource(R.string.game_detail_load_error_title),
                message = stringResource(R.string.game_detail_load_error_message),
                actionTitle = stringResource(R.string.game_detail_retry),
                onAction = onRetry,
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            CircularProgressIndicator(color = AppPrimary, modifier = Modifier.align(Alignment.Center))
        }

        PlaceholderBackButton(onBack = onBack, modifier = Modifier.align(Alignment.TopStart))
    }
}

@Composable
private fun PlaceholderBackButton(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .statusBarsPadding()
                .padding(horizontal = Spacing.md, vertical = Spacing.xs)
                .size(BACK_BUTTON_SIZE)
                .clip(CircleShape)
                .clickable(onClick = onBack),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = AppIcon.BACK.image(),
            contentDescription = stringResource(R.string.game_detail_back),
            tint = AppTextPrimary,
            modifier = Modifier.size(BACK_ICON_SIZE),
        )
    }
}

@Composable
private fun GameDetailContent(
    game: GameDetail,
    isGuest: Boolean,
    onBack: () -> Unit,
) {
    var showAddToLibrary by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val shareMessage = stringResource(R.string.game_detail_share_message, game.title)

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(AppBackground)
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Spacing.xl),
    ) {
        GameDetailHero(game = game, onBack = onBack, onShare = { context.shareText(shareMessage) })

        GameDetailHeader(
            game = game,
            isGuest = isGuest,
            onAddToLibrary = { showAddToLibrary = true },
            modifier = Modifier.padding(horizontal = Spacing.lg),
        )

        if (game.screenshots.isNotEmpty()) {
            GameScreenshotsSection(screenshots = game.screenshots)
        }

        GameAboutSection(about = game.about)

        if (game.specs.isNotEmpty()) {
            GameSpecificationsSection(specs = game.specs, modifier = Modifier.padding(bottom = Spacing.xxl))
        }
    }

    if (showAddToLibrary) {
        AddToLibrarySheet(
            gameTitle = game.title,
            coverStart = game.coverStart,
            coverEnd = game.coverEnd,
            onDismiss = { showAddToLibrary = false },
        )
    }
}

@Preview
@Composable
private fun GameDetailScreenPreview() {
    AppPreview {
        GameDetailScreen(state = GameDetailUiState(game = FakeGameRepository.gameDetail), isGuest = false)
    }
}

@Preview
@Composable
private fun GameDetailScreenLoadingPreview() {
    AppPreview {
        GameDetailScreen(state = GameDetailUiState(isLoading = true), isGuest = false)
    }
}

@Preview
@Composable
private fun GameDetailScreenErrorPreview() {
    AppPreview {
        GameDetailScreen(state = GameDetailUiState(hasError = true), isGuest = false)
    }
}
