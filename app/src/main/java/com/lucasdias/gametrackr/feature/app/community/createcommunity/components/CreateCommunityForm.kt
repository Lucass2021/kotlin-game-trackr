package com.lucasdias.gametrackr.feature.app.community.createcommunity.components

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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.FormField
import com.lucasdias.gametrackr.core.ui.components.FormTextField
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.app.community.createcommunity.CreateCommunityActions
import com.lucasdias.gametrackr.feature.app.community.createcommunity.CreateCommunityUiState

private const val NAME_COUNTER_THRESHOLD = 20
private const val DESCRIPTION_MAX_LINES = 12
private val DESCRIPTION_MIN_HEIGHT = 160.dp

@Composable
fun CreateCommunityForm(
    state: CreateCommunityUiState,
    actions: CreateCommunityActions,
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
            label = stringResource(R.string.create_community_field_name),
            error = state.nameError,
            hint =
                if (state.isRenamed) {
                    stringResource(R.string.create_community_name_renamed, state.handle)
                } else {
                    stringResource(R.string.create_community_name_hint)
                },
            counter = state.nameRemaining.takeIf { it <= NAME_COUNTER_THRESHOLD },
        ) {
            FormTextField(
                value = state.name,
                onValueChange = actions.onNameChange,
                placeholder = stringResource(R.string.create_community_name_placeholder),
                capitalization = KeyboardCapitalization.None,
            )
        }

        FormField(label = stringResource(R.string.create_community_field_description), error = state.descriptionError) {
            FormTextField(
                value = state.description,
                onValueChange = actions.onDescriptionChange,
                placeholder = stringResource(R.string.create_community_description_placeholder),
                singleLine = false,
                multiLineMinHeight = DESCRIPTION_MIN_HEIGHT,
                multiLineMaxLines = DESCRIPTION_MAX_LINES,
            )
        }
    }
}

@Preview
@Composable
private fun CreateCommunityFormPreview() {
    AppPreview {
        CreateCommunityForm(state = CreateCommunityUiState(name = "Retro Collectors"), actions = CreateCommunityActions())
    }
}

@Preview
@Composable
private fun CreateCommunityFormErrorsPreview() {
    AppPreview {
        CreateCommunityForm(state = CreateCommunityUiState(isSubmitted = true), actions = CreateCommunityActions())
    }
}
