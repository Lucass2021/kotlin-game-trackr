package com.lucasdias.gametrackr.feature.app.profile.setup

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.withResumed
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.SetupItem
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import com.lucasdias.gametrackr.feature.app.profile.setup.components.EditSetupHeader
import com.lucasdias.gametrackr.feature.app.profile.setup.components.SetupDeleteButton
import com.lucasdias.gametrackr.feature.app.profile.setup.components.SetupDeleteDialog
import com.lucasdias.gametrackr.feature.app.profile.setup.components.SetupFormField
import com.lucasdias.gametrackr.feature.app.profile.setup.components.SetupPhotoStrip
import com.lucasdias.gametrackr.feature.app.profile.setup.components.SetupTextField
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditSetupScreen(
    setup: SetupItem?,
    onBack: () -> Unit,
    onSaved: (SetupItem) -> Unit,
    onDeleted: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditSetupViewModel = koinViewModel { parametersOf(setup) },
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val close = { if (!state.isBusy) onBack() }

    BackHandler(onBack = close)
    LaunchedEffect(state.isDone) {
        if (state.isDone) lifecycle.withResumed { onBack() }
    }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val pickPhotos =
        rememberLauncherForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(EditSetupUiState.PHOTO_LIMIT),
        ) { picked -> viewModel.onPhotosPicked(picked.map { it.toString() }) }

    EditSetupContent(
        state = state,
        actions =
            EditSetupActions(
                onClose = close,
                onSave = {
                    focusManager.clearFocus()
                    viewModel.onSave(onSaved)
                },
                onAddPhotos = {
                    pickPhotos.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                onDelete = { showDeleteConfirm = true },
                onRemoveImage = viewModel::onRemoveImage,
                onRemoveNewPhoto = viewModel::onRemoveNewPhoto,
                onTitleChange = viewModel::onTitleChange,
                onDescriptionChange = viewModel::onDescriptionChange,
                onErrorShown = viewModel::onErrorShown,
            ),
        modifier = modifier,
    )

    if (showDeleteConfirm) {
        SetupDeleteDialog(
            onConfirm = {
                showDeleteConfirm = false
                viewModel.onDelete(onDeleted)
            },
            onDismiss = { showDeleteConfirm = false },
        )
    }
}

@Composable
fun EditSetupContent(
    state: EditSetupUiState,
    actions: EditSetupActions,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
        Column(modifier = Modifier.fillMaxSize()) {
            EditSetupHeader(isNew = state.isNew, isSaving = state.isSaving, onClose = actions.onClose, onSave = actions.onSave)
            Form(state = state, actions = actions)
        }

        Toast(
            message = state.errorMessage?.let { stringResource(it) },
            onDismiss = actions.onErrorShown,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Composable
private fun Form(
    state: EditSetupUiState,
    actions: EditSetupActions,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        SetupFormField(
            label = stringResource(R.string.setup_field_photos),
            counter = "${state.photoCount}/${EditSetupUiState.PHOTO_LIMIT}",
        ) {
            SetupPhotoStrip(
                images = state.images,
                newPhotos = state.newPhotos,
                canAddPhotos = state.remainingPhotoSlots > 0,
                onRemoveImage = actions.onRemoveImage,
                onRemoveNewPhoto = actions.onRemoveNewPhoto,
                onAdd = actions.onAddPhotos,
            )
        }

        SetupFormField(label = stringResource(R.string.setup_field_title), error = state.titleError) {
            SetupTextField(
                value = state.title,
                onValueChange = actions.onTitleChange,
                placeholder = stringResource(R.string.setup_title_placeholder),
            )
        }

        SetupFormField(label = stringResource(R.string.setup_field_description), error = state.descriptionError) {
            SetupTextField(
                value = state.description,
                onValueChange = actions.onDescriptionChange,
                placeholder = stringResource(R.string.setup_description_placeholder),
                singleLine = false,
            )
        }

        if (!state.isNew) {
            SetupDeleteButton(isDeleting = state.isDeleting, onClick = actions.onDelete)
        }
    }
}

@Preview
@Composable
private fun EditSetupContentPreview() {
    val setup = ProfileMockData.setups[0]
    AppPreview {
        EditSetupContent(
            state =
                EditSetupUiState(
                    isNew = false,
                    setupId = setup.id,
                    title = setup.title,
                    description = setup.description,
                    images = setup.images,
                ),
            actions = EditSetupActions(),
        )
    }
}
