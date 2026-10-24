package com.lucasdias.gametrackr.feature.app.community.createtopic

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
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.components.FormHeader
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.text.asString
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.createtopic.components.CreateTopicForm
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CreateTopicScreen(
    isGuest: Boolean,
    community: Community?,
    onBack: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateTopicViewModel = koinViewModel { if (community != null) parametersOf(community) else parametersOf() },
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showDiscardConfirm by remember { mutableStateOf(false) }
    val currentOnBack by rememberUpdatedState(onBack)

    LaunchedEffect(isGuest) {
        if (!isGuest) viewModel.loadIfNeeded()
    }

    LaunchedEffect(state.createdPost) {
        if (state.createdPost != null) currentOnBack()
    }

    fun requestClose() {
        when {
            state.createdPost != null -> onBack()
            state.isSubmitting -> Unit
            state.hasContent -> showDiscardConfirm = true
            else -> onBack()
        }
    }

    BackHandler { requestClose() }

    CreateTopicContent(
        state = state,
        isGuest = isGuest,
        actions =
            CreateTopicActions(
                onClose = ::requestClose,
                onSubmit = viewModel::onSubmit,
                onCommunityChange = viewModel::onCommunityChange,
                onTitleChange = viewModel::onTitleChange,
                onBodyChange = viewModel::onBodyChange,
                onRetryCommunities = viewModel::retryCommunities,
                onCreateAccount = onCreateAccount,
                onToastShown = viewModel::onToastShown,
            ),
        modifier = modifier,
    )

    if (showDiscardConfirm) {
        DiscardChangesDialog(
            title = stringResource(R.string.create_topic_discard_title),
            onDiscard = {
                showDiscardConfirm = false
                onBack()
            },
            onDismiss = { showDiscardConfirm = false },
        )
    }
}

@Composable
fun CreateTopicContent(
    state: CreateTopicUiState,
    isGuest: Boolean,
    actions: CreateTopicActions,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize().background(AppBackground)) {
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            FormHeader(
                title = stringResource(R.string.create_topic_title),
                actionLabel = stringResource(R.string.create_topic_submit),
                closeLabel = stringResource(R.string.community_action_close),
                savingLabel = stringResource(R.string.create_topic_submitting),
                isSaving = state.isSubmitting,
                onClose = actions.onClose,
                onAction = actions.onSubmit,
                closeIcon = AppIcon.CLOSE,
                isActionVisible = !isGuest,
            )

            if (isGuest) {
                EmptyState(
                    icon = AppIcon.COMMUNITY,
                    title = stringResource(R.string.create_topic_guest_title),
                    message = stringResource(R.string.create_topic_guest_message),
                    actionTitle = stringResource(R.string.create_topic_guest_action),
                    onAction = actions.onCreateAccount,
                    modifier = Modifier.weight(1f),
                )
            } else {
                CreateTopicForm(state = state, actions = actions)
            }
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
private fun CreateTopicContentPreview() {
    AppPreview {
        CreateTopicContent(
            state = CreateTopicUiState(community = CommunityMockData.all.first(), isCommunityLocked = true),
            isGuest = false,
            actions = CreateTopicActions(),
        )
    }
}

@Preview
@Composable
private fun CreateTopicContentGuestPreview() {
    AppPreview {
        CreateTopicContent(state = CreateTopicUiState(), isGuest = true, actions = CreateTopicActions())
    }
}
