package com.lucasdias.gametrackr.feature.app.addtolibrary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.GameDetail
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.game.FakeGameRepository
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.AddToLibraryForm
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.AddToLibraryHeader
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private const val SHEET_HEIGHT_FRACTION = 0.92f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToLibrarySheet(
    slug: String,
    game: GameDetail,
    onDismiss: () -> Unit,
    onSaved: () -> Unit,
) {
    val viewModel: AddToLibraryViewModel = koinViewModel(key = "add-to-library") { parametersOf(slug, game) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val isSaving by rememberUpdatedState(state.isSaving)
    val sheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { it != SheetValue.Hidden || !isSaving },
        )

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            viewModel.reset()
            onSaved()
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            if (!isSaving) {
                viewModel.reset()
                onDismiss()
            }
        },
        sheetState = sheetState,
        containerColor = AppBackground,
    ) {
        AddToLibraryContent(
            state = state,
            game = game,
            actions =
                AddToLibraryActions(
                    onStatusChange = viewModel::onStatusChange,
                    onRatingChange = viewModel::onRatingChange,
                    onHoursChange = viewModel::onHoursChange,
                    onPlatformChange = viewModel::onPlatformChange,
                    onStartedChange = viewModel::onStartedChange,
                    onFinishedChange = viewModel::onFinishedChange,
                    onReviewChange = viewModel::onReviewChange,
                    onSave = viewModel::onSave,
                    onToastShown = viewModel::onToastShown,
                ),
            modifier = Modifier.fillMaxHeight(SHEET_HEIGHT_FRACTION),
        )
    }
}

@Composable
private fun AddToLibraryContent(
    state: AddToLibraryUiState,
    game: GameDetail,
    actions: AddToLibraryActions,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Column {
            AddToLibraryHeader(
                gameTitle = game.title,
                coverStart = game.coverStart,
                coverEnd = game.coverEnd,
                modifier = Modifier.padding(start = Spacing.lg, end = Spacing.lg, top = Spacing.xs, bottom = Spacing.lg),
            )

            AddToLibraryForm(state = state, actions = actions, modifier = Modifier.weight(1f))

            PrimaryButton(
                text = stringResource(R.string.add_library_save),
                onClick = actions.onSave,
                isLoading = state.isSaving,
                icon = AppIcon.ADD_TO_LIBRARY,
                modifier = Modifier.padding(horizontal = Spacing.lg, vertical = Spacing.sm),
            )
        }

        Toast(
            message = state.toastMessage?.let { stringResource(it) },
            onDismiss = actions.onToastShown,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Preview
@Composable
private fun AddToLibraryContentPreview() {
    val game = FakeGameRepository.gameDetail
    AppPreview {
        AddToLibraryContent(
            state = AddToLibraryUiState(platforms = game.platforms, platform = game.platforms.first(), rating = 4),
            game = game,
            actions = AddToLibraryActions(),
        )
    }
}

@Preview
@Composable
private fun AddToLibraryContentErrorPreview() {
    val game = FakeGameRepository.gameDetail
    AppPreview {
        AddToLibraryContent(
            state =
                AddToLibraryUiState(
                    platforms = game.platforms,
                    platform = game.platforms.first(),
                    startedAt = PREVIEW_STARTED_AT,
                    finishedAt = PREVIEW_FINISHED_AT,
                    isSubmitted = true,
                ),
            game = game,
            actions = AddToLibraryActions(),
        )
    }
}

private const val PREVIEW_STARTED_AT = 1_710_417_600_000L
private const val PREVIEW_FINISHED_AT = 1_709_251_200_000L
