package com.lucasdias.gametrackr.feature.app.addtolibrary.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.app.addtolibrary.AddToLibraryActions
import com.lucasdias.gametrackr.feature.app.addtolibrary.AddToLibraryUiState

@Composable
fun AddToLibraryForm(
    state: AddToLibraryUiState,
    actions: AddToLibraryActions,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
    ) {
        AddToLibraryField(stringResource(R.string.add_library_status)) {
            StatusSelectGrid(selection = state.status, onSelect = actions.onStatusChange)
        }

        AddToLibraryField(stringResource(R.string.add_library_rating)) {
            InteractiveStarRating(rating = state.rating, onRatingChange = actions.onRatingChange)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            AddToLibraryField(stringResource(R.string.add_library_hours), modifier = Modifier.weight(1f)) {
                NumberField(
                    value = state.hours,
                    onValueChange = actions.onHoursChange,
                    placeholder = stringResource(R.string.add_library_hours_placeholder),
                )
            }
            state.platform?.let { platform ->
                AddToLibraryField(stringResource(R.string.add_library_platform), modifier = Modifier.weight(1f)) {
                    PlatformPicker(selection = platform, options = state.platforms, onSelect = actions.onPlatformChange)
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            AddToLibraryField(stringResource(R.string.add_library_started), modifier = Modifier.weight(1f)) {
                DateField(
                    dateMillis = state.startedAt,
                    onDateChange = actions.onStartedChange,
                    placeholder = stringResource(R.string.add_library_date_placeholder),
                )
            }
            AddToLibraryField(
                label = stringResource(R.string.add_library_finished),
                error = state.dateError?.let { stringResource(it) },
                modifier = Modifier.weight(1f),
            ) {
                DateField(
                    dateMillis = state.finishedAt,
                    onDateChange = actions.onFinishedChange,
                    placeholder = stringResource(R.string.add_library_date_placeholder),
                    isError = state.dateError != null,
                )
            }
        }

        AddToLibraryField(stringResource(R.string.add_library_review)) {
            ReviewField(
                value = state.review,
                onValueChange = actions.onReviewChange,
                placeholder = stringResource(R.string.add_library_review_placeholder),
            )
        }
    }
}

@Preview
@Composable
private fun AddToLibraryFormPreview() {
    AppPreview {
        AddToLibraryForm(state = AddToLibraryUiState(platforms = listOf("PS5", "PC"), platform = "PS5"), actions = AddToLibraryActions())
    }
}
