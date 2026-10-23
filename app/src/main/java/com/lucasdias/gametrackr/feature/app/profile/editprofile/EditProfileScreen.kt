package com.lucasdias.gametrackr.feature.app.profile.editprofile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Profile
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.DiscardChangesDialog
import com.lucasdias.gametrackr.core.ui.components.FormHeader
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.text.asString
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import com.lucasdias.gametrackr.feature.app.profile.editprofile.components.EditProfileForm
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditProfileScreen(
    profile: Profile,
    onBack: () -> Unit,
    onSave: (Profile) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = koinViewModel { parametersOf(profile) },
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showDiscardConfirm by remember { mutableStateOf(false) }
    val currentOnBack by rememberUpdatedState(onBack)
    val currentOnSave by rememberUpdatedState(onSave)

    LaunchedEffect(state.savedProfile) {
        val saved = state.savedProfile ?: return@LaunchedEffect
        currentOnSave(saved)
        currentOnBack()
    }

    fun requestClose() {
        when {
            state.savedProfile != null -> onBack()
            state.isSaving -> Unit
            state.hasChanges -> showDiscardConfirm = true
            else -> onBack()
        }
    }

    BackHandler { requestClose() }

    EditProfileContent(
        state = state,
        actions =
            EditProfileActions(
                onBack = ::requestClose,
                onSave = viewModel::onSave,
                onNameChange = viewModel::onNameChange,
                onUsernameChange = viewModel::onUsernameChange,
                onBioChange = viewModel::onBioChange,
                onAvatarColorChange = viewModel::onAvatarColorChange,
                onVisibilityChange = viewModel::onVisibilityChange,
                onRetryColors = viewModel::loadColors,
                onToastShown = viewModel::onToastShown,
            ),
        modifier = modifier,
    )

    if (showDiscardConfirm) {
        DiscardChangesDialog(
            title = stringResource(R.string.edit_profile_discard_title),
            onDiscard = {
                showDiscardConfirm = false
                onBack()
            },
            onDismiss = { showDiscardConfirm = false },
        )
    }
}

@Composable
fun EditProfileContent(
    state: EditProfileUiState,
    actions: EditProfileActions,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize().background(AppBackground)) {
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            FormHeader(
                title = stringResource(R.string.edit_profile_title),
                actionLabel = stringResource(R.string.edit_profile_save),
                closeLabel = stringResource(R.string.edit_profile_back),
                savingLabel = stringResource(R.string.edit_profile_saving),
                isSaving = state.isSaving,
                onClose = actions.onBack,
                onAction = actions.onSave,
            )
            EditProfileForm(state = state, actions = actions)
        }

        Toast(
            message = state.toastMessage?.asString(),
            onDismiss = actions.onToastShown,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Preview
@Composable
private fun EditProfileContentPreview() {
    AppPreview {
        EditProfileContent(
            state = EditProfileUiState(original = ProfileMockData.profile, colors = ProfileMockData.colors),
            actions = EditProfileActions(),
        )
    }
}
