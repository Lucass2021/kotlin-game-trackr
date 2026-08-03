package com.lucasdias.gametrackr.feature.app.profile.editprofile

import androidx.lifecycle.ViewModel
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.feature.app.profile.Profile
import com.lucasdias.gametrackr.feature.app.profile.ProfileVisibility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditProfileViewModel(
    private val original: Profile,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(
            EditProfileUiState(
                name = original.name,
                username = original.username.removePrefix("@"),
                bio = original.bio,
                palette = AvatarPalette.matching(original.avatarStart, original.avatarEnd),
                visibility = original.visibility,
            ),
        )
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private var submitted = false

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value) }
        revalidate()
    }

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value) }
        revalidate()
    }

    fun onBioChange(value: String) {
        _uiState.update { it.copy(bio = value) }
        revalidate()
    }

    fun onPaletteChange(value: AvatarPalette) {
        _uiState.update { it.copy(palette = value) }
    }

    fun onVisibilityChange(value: ProfileVisibility) {
        _uiState.update { it.copy(visibility = value) }
    }

    fun hasChanges(): Boolean {
        val state = _uiState.value
        return state.name.trim() != original.name ||
            "@${state.normalizedUsername}" != original.username ||
            state.bio.trim() != original.bio ||
            state.palette != AvatarPalette.matching(original.avatarStart, original.avatarEnd) ||
            state.visibility != original.visibility
    }

    fun onSave(): Profile? {
        submitted = true
        revalidate()
        val state = _uiState.value
        if (!state.canSave) return null

        return original.copy(
            name = state.name.trim(),
            username = "@${state.normalizedUsername}",
            bio = state.bio.trim(),
            avatarStart = state.palette.start,
            avatarEnd = state.palette.end,
            visibility = state.visibility,
        )
    }

    private fun revalidate() {
        if (!submitted) return
        _uiState.update {
            it.copy(
                nameError = nameErrorFor(it.name),
                usernameError = usernameErrorFor(it.normalizedUsername),
                bioError = bioErrorFor(it.bio),
            )
        }
    }

    private fun nameErrorFor(name: String): Int? {
        val trimmed = name.trim()
        return when {
            trimmed.isEmpty() -> R.string.validation_name_required
            trimmed.length < 3 -> R.string.validation_name_too_short
            trimmed.length > EditProfileUiState.NAME_LIMIT -> R.string.validation_name_too_long
            else -> null
        }
    }

    private fun usernameErrorFor(username: String): Int? =
        when {
            username.isEmpty() -> R.string.validation_username_required
            username.length < 3 -> R.string.validation_username_too_short
            username.length > EditProfileUiState.USERNAME_LIMIT -> R.string.validation_username_too_long
            username.any { it !in EditProfileUiState.USERNAME_ALPHABET } -> R.string.validation_username_invalid
            else -> null
        }

    private fun bioErrorFor(bio: String): Int? = if (bio.trim().length > EditProfileUiState.BIO_LIMIT) R.string.validation_bio_too_long else null
}
