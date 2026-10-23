package com.lucasdias.gametrackr.feature.app.community.createcommunity

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
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.DiscardChangesDialog
import com.lucasdias.gametrackr.core.ui.components.FormHeader
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.text.asString
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.feature.app.community.createcommunity.components.CreateCommunityForm
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateCommunityScreen(
    onBack: () -> Unit,
    onCreated: (Community) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateCommunityViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showDiscardConfirm by remember { mutableStateOf(false) }
    val currentOnCreated by rememberUpdatedState(onCreated)

    LaunchedEffect(state.createdCommunity) {
        val created = state.createdCommunity ?: return@LaunchedEffect
        currentOnCreated(created)
    }

    fun requestClose() {
        when {
            state.createdCommunity != null -> onBack()
            state.isSubmitting -> Unit
            state.hasContent -> showDiscardConfirm = true
            else -> onBack()
        }
    }

    BackHandler { requestClose() }

    CreateCommunityContent(
        state = state,
        actions =
            CreateCommunityActions(
                onClose = ::requestClose,
                onSubmit = viewModel::onSubmit,
                onNameChange = viewModel::onNameChange,
                onDescriptionChange = viewModel::onDescriptionChange,
                onToastShown = viewModel::onToastShown,
            ),
        modifier = modifier,
    )

    if (showDiscardConfirm) {
        DiscardChangesDialog(
            title = stringResource(R.string.create_community_discard_title),
            onDiscard = {
                showDiscardConfirm = false
                onBack()
            },
            onDismiss = { showDiscardConfirm = false },
        )
    }
}

@Composable
fun CreateCommunityContent(
    state: CreateCommunityUiState,
    actions: CreateCommunityActions,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize().background(AppBackground)) {
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            FormHeader(
                title = stringResource(R.string.create_community_title),
                actionLabel = stringResource(R.string.create_community_submit),
                closeLabel = stringResource(R.string.community_action_close),
                savingLabel = stringResource(R.string.create_community_submitting),
                isSaving = state.isSubmitting,
                onClose = actions.onClose,
                onAction = actions.onSubmit,
                closeIcon = AppIcon.CLOSE,
            )
            CreateCommunityForm(state = state, actions = actions)
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
private fun CreateCommunityContentPreview() {
    AppPreview {
        CreateCommunityContent(state = CreateCommunityUiState(), actions = CreateCommunityActions())
    }
}
