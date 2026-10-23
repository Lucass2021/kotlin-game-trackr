package com.lucasdias.gametrackr.feature.app.profile.editprofile.components

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
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.FormField
import com.lucasdias.gametrackr.core.ui.components.FormTextField
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import com.lucasdias.gametrackr.feature.app.profile.editprofile.EditProfileActions
import com.lucasdias.gametrackr.feature.app.profile.editprofile.EditProfileUiState

private const val BIO_COUNTER_THRESHOLD = 40

@Composable
fun EditProfileForm(
    state: EditProfileUiState,
    actions: EditProfileActions,
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
        EditProfilePreviewCard(name = state.previewName, username = state.previewUsername, avatarHex = state.avatarHex)

        FormField(label = stringResource(R.string.edit_profile_field_avatar)) {
            if (state.colorsFailed) {
                AvatarColorsRetry(onRetry = actions.onRetryColors)
            } else {
                AvatarColorPicker(colors = state.colors, selection = state.avatarHex, onSelect = actions.onAvatarColorChange)
            }
        }

        FormField(label = stringResource(R.string.edit_profile_field_name), error = state.nameError) {
            FormTextField(
                value = state.name,
                onValueChange = actions.onNameChange,
                placeholder = stringResource(R.string.edit_profile_name_placeholder),
            )
        }

        FormField(label = stringResource(R.string.edit_profile_field_username), error = state.usernameError) {
            FormTextField(
                value = state.username,
                onValueChange = actions.onUsernameChange,
                placeholder = stringResource(R.string.edit_profile_username_placeholder),
                prefix = EditProfileUiState.USERNAME_PREFIX,
                capitalization = KeyboardCapitalization.None,
            )
        }

        FormField(
            label = stringResource(R.string.edit_profile_field_bio),
            error = state.bioError,
            counter = state.bioRemaining.takeIf { it <= BIO_COUNTER_THRESHOLD },
        ) {
            FormTextField(
                value = state.bio,
                onValueChange = actions.onBioChange,
                placeholder = stringResource(R.string.edit_profile_bio_placeholder),
                singleLine = false,
            )
        }

        FormField(label = stringResource(R.string.edit_profile_field_visibility)) {
            VisibilitySelector(selection = state.visibility, onSelect = actions.onVisibilityChange)
        }
    }
}

@Preview
@Composable
private fun EditProfileFormPreview() {
    AppPreview {
        EditProfileForm(
            state = EditProfileUiState(original = ProfileMockData.profile, colors = ProfileMockData.colors),
            actions = EditProfileActions(),
        )
    }
}

@Preview
@Composable
private fun EditProfileFormColorsFailedPreview() {
    AppPreview {
        EditProfileForm(
            state = EditProfileUiState(original = ProfileMockData.profile, colorsFailed = true),
            actions = EditProfileActions(),
        )
    }
}
