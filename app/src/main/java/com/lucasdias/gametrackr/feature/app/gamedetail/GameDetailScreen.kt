package com.lucasdias.gametrackr.feature.app.gamedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.shareText
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.addtolibrary.AddToLibrarySheet
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameAboutSection
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameDetailHero
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameGenreChip
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameInfoChip
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameScreenshotsSection
import com.lucasdias.gametrackr.feature.app.gamedetail.components.GameSpecificationsSection
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale

@Composable
fun GameDetailScreen(
    onBack: () -> Unit,
    isGuest: Boolean = false,
    slug: String? = null,
) {
    if (slug == null) {
        GameDetailContent(game = GameDetailMockData.game, onBack = onBack, isGuest = isGuest)
        return
    }

    val viewModel: GameDetailViewModel = koinViewModel { parametersOf(slug) }
    val game by viewModel.game.collectAsStateWithLifecycle()
    val hasError by viewModel.hasError.collectAsStateWithLifecycle()

    game?.let {
        GameDetailContent(game = it, onBack = onBack, isGuest = isGuest)
    } ?: GameDetailPlaceholder(hasError = hasError, onBack = onBack)
}

@Composable
private fun GameDetailPlaceholder(
    hasError: Boolean,
    onBack: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize().background(AppBackground),
        contentAlignment = Alignment.Center,
    ) {
        if (hasError) {
            Text(
                text = stringResource(R.string.game_detail_load_error),
                color = AppTextSecondary,
                style = AppType.body(15.sp),
            )
        } else {
            CircularProgressIndicator(color = AppPrimary)
        }

        Box(
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .size(42.dp)
                    .clickable(onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcon.BACK.image(),
                contentDescription = stringResource(R.string.game_detail_back),
                tint = AppTextPrimary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GameDetailContent(
    game: GameDetail,
    onBack: () -> Unit,
    isGuest: Boolean,
) {
    var showAddToLibrary by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(AppBackground)
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        GameDetailHero(
            game = game,
            onBack = onBack,
            onShare = { context.shareText("I found ${game.title} on GameTrackr") },
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text(
                text = game.title,
                color = AppTextPrimary,
                style = AppType.headline(30.sp),
                lineHeight = 36.sp,
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                GameInfoChip(text = game.year)
                game.rating?.let { GameInfoChip(text = String.format(Locale.US, "%.1f", it), isRating = true) }
                game.platforms.forEach { GameInfoChip(text = it) }
            }

            if (game.genres.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    game.genres.forEach { GameGenreChip(text = it) }
                }
            }

            if (!isGuest) {
                PrimaryButton(
                    text = stringResource(R.string.game_detail_add_to_library),
                    onClick = { showAddToLibrary = true },
                    icon = AppIcon.ADD_TO_LIBRARY,
                )
            }
        }

        if (game.screenshots.isNotEmpty()) {
            GameScreenshotsSection(screenshots = game.screenshots)
        }

        GameAboutSection(about = game.about)

        if (game.specs.isNotEmpty()) {
            GameSpecificationsSection(
                specs = game.specs,
                modifier = Modifier.padding(bottom = 32.dp),
            )
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
