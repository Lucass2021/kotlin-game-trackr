package com.lucasdias.gametrackr.feature.app.profile.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.core.auth.SessionManager
import com.lucasdias.gametrackr.core.model.Profile
import com.lucasdias.gametrackr.core.model.ProfileVisibility
import com.lucasdias.gametrackr.data.profile.ProfileRepository
import com.lucasdias.gametrackr.feature.auth.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    original: Profile,
    private val repository: ProfileRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditProfileUiState(original = original))
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        loadColorsIfNeeded()
    }

    fun loadColorsIfNeeded() {
        if (_uiState.value.colors.isNotEmpty()) return
        loadColors()
    }

    fun loadColors() {
        if (_uiState.value.isLoadingColors) return
        _uiState.update { it.copy(isLoadingColors = true, colorsFailed = false) }
        viewModelScope.launch {
            repository
                .fetchColors()
                .onSuccess { colors -> _uiState.update { it.copy(colors = colors, isLoadingColors = false) } }
                .onFailure { _uiState.update { it.copy(isLoadingColors = false, colorsFailed = true) } }
        }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value) }
    }

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value) }
    }

    fun onBioChange(value: String) {
        _uiState.update { it.copy(bio = value) }
    }

    fun onAvatarColorChange(value: String) {
        _uiState.update { it.copy(avatarHex = value) }
    }

    fun onVisibilityChange(value: ProfileVisibility) {
        _uiState.update { it.copy(visibility = value) }
    }

    fun onSave() {
        val current = _uiState.value
        if (current.isSaving || current.savedProfile != null) return
        _uiState.update { it.copy(isSubmitted = true) }
        if (!current.canSave) return

        _uiState.update { it.copy(isSaving = true, toastMessage = null) }
        viewModelScope.launch {
            repository
                .updateProfile(
                    name = current.trimmedName,
                    username = current.normalizedUsername,
                    profileColor = current.avatarHex,
                    bio = current.trimmedBio,
                    visibility = current.visibility,
                ).onSuccess { user ->
                    sessionManager.setAuthenticated(user)
                    _uiState.update { it.copy(isSaving = false, savedProfile = current.updatedProfile().applying(user)) }
                }.onFailure { error ->
                    _uiState.update { it.copy(isSaving = false, toastMessage = error.toUiText()) }
                }
        }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}
