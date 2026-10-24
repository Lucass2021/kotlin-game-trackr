package com.lucasdias.gametrackr.feature.app.community.createtopic.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.FormField
import com.lucasdias.gametrackr.core.ui.components.FormTextField
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.createtopic.CreateTopicActions
import com.lucasdias.gametrackr.feature.app.community.createtopic.CreateTopicUiState

private const val TITLE_COUNTER_THRESHOLD = 20
private const val BODY_MAX_LINES = 12
private const val PICKER_Z_INDEX = 1f
private val BODY_MIN_HEIGHT = 160.dp

@Composable
fun CreateTopicForm(
    state: CreateTopicUiState,
    actions: CreateTopicActions,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.lg)
                .padding(top = Spacing.xs, bottom = Spacing.xxxl),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
    ) {
        FormField(
            label = stringResource(R.string.create_topic_field_community),
            error = state.communityError,
            modifier = Modifier.zIndex(PICKER_Z_INDEX),
        ) {
            CommunityPickerField(
                selection = state.community,
                options = state.communities,
                onSelect = actions.onCommunityChange,
                isLocked = state.isCommunityLocked,
                isLoading = state.isLoadingCommunities,
                isFailed = state.communitiesFailed,
                onRetry = actions.onRetryCommunities,
            )
        }

        FormField(
            label = stringResource(R.string.create_topic_field_title),
            error = state.titleError,
            counter = state.titleRemaining.takeIf { it <= TITLE_COUNTER_THRESHOLD },
        ) {
            FormTextField(
                value = state.title,
                onValueChange = actions.onTitleChange,
                placeholder = stringResource(R.string.create_topic_title_placeholder),
            )
        }

        FormField(label = stringResource(R.string.create_topic_field_body), error = state.bodyError) {
            FormTextField(
                value = state.body,
                onValueChange = actions.onBodyChange,
                placeholder = stringResource(R.string.create_topic_body_placeholder),
                singleLine = false,
                multiLineMinHeight = BODY_MIN_HEIGHT,
                multiLineMaxLines = BODY_MAX_LINES,
            )
        }
    }
}

@Preview
@Composable
private fun CreateTopicFormPreview() {
    AppPreview {
        CreateTopicForm(
            state = CreateTopicUiState(community = CommunityMockData.all.first(), title = "Best co-op games of the year?"),
            actions = CreateTopicActions(),
        )
    }
}

@Preview
@Composable
private fun CreateTopicFormErrorsPreview() {
    AppPreview {
        CreateTopicForm(state = CreateTopicUiState(isSubmitted = true), actions = CreateTopicActions())
    }
}
