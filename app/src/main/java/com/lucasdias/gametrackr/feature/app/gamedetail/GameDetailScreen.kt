package com.lucasdias.gametrackr.feature.app.gamedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.GameDetail
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.components.ToastStyle
import com.lucasdias.gametrackr.core.ui.shareText
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.game.FakeGameRepository
import com.lucasdias.gametrackr.feature.app.addtolibrary.AddToLibrarySheet
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameAboutSection
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameDetailHeader
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameDetailHero
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameDetailPlaceholder
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameScreenshotsSection
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameSpecificationsSection
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun GameDetailScreen(
    onBack: () -> Unit,
    slug: String,
    isGuest: Boolean = false,
) {
    val viewModel: GameDetailViewModel = koinViewModel { parametersOf(slug) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddToLibrary by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(viewModel) { viewModel.loadIfNeeded() }

    GameDetailScreen(
        state = state,
        isGuest = isGuest,
        onBack = onBack,
        onRetry = viewModel::load,
        onAddToLibrary = { showAddToLibrary = true },
        onToastShown = viewModel::onToastShown,
    )

    val game = state.game
    if (showAddToLibrary && game != null) {
        AddToLibrarySheet(
            slug = slug,
            game = game,
            onDismiss = { showAddToLibrary = false },
            onSaved = {
                showAddToLibrary = false
                viewModel.onAddedToLibrary()
            },
        )
    }
}

@Composable
private fun GameDetailScreen(
    state: GameDetailUiState,
    isGuest: Boolean,
    onBack: () -> Unit = {},
    onRetry: () -> Unit = {},
    onAddToLibrary: () -> Unit = {},
    onToastShown: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        val game = state.game
        if (game != null) {
            GameDetailContent(
                game = game,
                isGuest = isGuest,
                isInLibrary = state.isInLibrary,
                onBack = onBack,
                onAddToLibrary = onAddToLibrary,
            )
        } else {
            GameDetailPlaceholder(hasError = state.hasError, onBack = onBack, onRetry = onRetry)
        }

        Toast(
            message = state.toastMessage?.let { stringResource(it) },
            onDismiss = onToastShown,
            style = ToastStyle.Success,
            modifier = Modifier.align(Alignment.TopCenter).statusBarsPadding(),
        )
    }
}

@Composable
private fun GameDetailContent(
    game: GameDetail,
    isGuest: Boolean,
    isInLibrary: Boolean,
    onBack: () -> Unit,
    onAddToLibrary: () -> Unit,
) {
    val context = LocalContext.current
    val shareMessage = stringResource(R.string.game_detail_share_message, game.title)

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Spacing.xl),
    ) {
        GameDetailHero(game = game, onBack = onBack, onShare = { context.shareText(shareMessage) })

        GameDetailHeader(
            game = game,
            isGuest = isGuest,
            onAddToLibrary = onAddToLibrary,
            isInLibrary = isInLibrary,
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
private fun GameDetailScreenToastPreview() {
    AppPreview {
        GameDetailScreen(
            state = GameDetailUiState(game = FakeGameRepository.gameDetail, toastMessage = R.string.add_library_saved),
            isGuest = false,
        )
    }
}
