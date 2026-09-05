package com.lucasdias.gametrackr.feature.app.profile.editprofile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.auth.SessionManager
import com.lucasdias.gametrackr.core.model.User
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.network.ProfileApi
import com.lucasdias.gametrackr.core.network.dto.UpdateProfileRequest
import com.lucasdias.gametrackr.core.network.dto.toDomain
import com.lucasdias.gametrackr.core.network.toApiError
import com.lucasdias.gametrackr.feature.app.profile.Profile
import com.lucasdias.gametrackr.feature.app.profile.ProfileVisibility
import com.lucasdias.gametrackr.feature.auth.toMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class EditProfileViewModel(
    private val original: Profile,
    private val api: ProfileApi,
    private val sessionManager: SessionManager,
    private val json: Json,
    private val context: Context,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(
            EditProfileUiState(
                name = original.name,
                username = original.username.removePrefix("@"),
                bio = original.bio,
                avatarHex = original.avatarHex,
                visibility = original.visibility,
            ),
        )
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private var submitted = false

    init {
        viewModelScope.launch {
            val colors =
                runCatching { api.getColors().data.map { it.toDomain() } }
                    .getOrDefault(emptyList())
            _uiState.update { it.copy(colors = colors) }
        }
    }

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

    fun onAvatarColorChange(value: String) {
        _uiState.update { it.copy(avatarHex = value) }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onVisibilityChange(value: ProfileVisibility) {
        _uiState.update { it.copy(visibility = value) }
    }

    fun hasChanges(): Boolean {
        val state = _uiState.value
        return state.name.trim() != original.name ||
            "@${state.normalizedUsername}" != original.username ||
            state.bio.trim() != original.bio ||
            state.avatarHex != original.avatarHex ||
            state.visibility != original.visibility
    }

    fun onSave(onSaved: (Profile) -> Unit) {
        submitted = true
        revalidate()
        val state = _uiState.value
        if (!state.canSave || state.isSaving) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            val result =
                runCatching {
                    api
                        .update(
                            UpdateProfileRequest(
                                name = state.name.trim(),
                                username = state.normalizedUsername,
                                profileColor = state.avatarHex,
                            ),
                        ).user
                        .toDomain()
                }
            _uiState.update { it.copy(isSaving = false) }

            result
                .onSuccess { user -> onSaved(updatedProfile(state, user)) }
                .onFailure { error -> _uiState.update { it.copy(errorMessage = messageFor(error)) } }
        }
    }

    private fun updatedProfile(
        state: EditProfileUiState,
        user: User,
    ): Profile {
        sessionManager.setAuthenticated(user)
        return original
            .copy(
                name = state.name.trim(),
                username = "@${state.normalizedUsername}",
                bio = state.bio.trim(),
                avatarHex = state.avatarHex,
                visibility = state.visibility,
            ).applying(user)
    }

    private fun messageFor(error: Throwable): String = error.toApiError(json).toMessage(context)

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
