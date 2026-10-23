package com.lucasdias.gametrackr.feature.app.community.createcommunity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.data.community.CommunityRepository
import com.lucasdias.gametrackr.feature.auth.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateCommunityViewModel(
    private val repository: CommunityRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateCommunityUiState())
    val uiState: StateFlow<CreateCommunityUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value) }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value) }
    }

    fun onSubmit() {
        val current = _uiState.value
        if (current.isSubmitting || current.createdCommunity != null) return
        _uiState.update { it.copy(isSubmitted = true) }
        if (!current.canSubmit) return

        _uiState.update { it.copy(isSubmitting = true, toastMessage = null) }
        viewModelScope.launch {
            repository
                .createCommunity(title = current.handle, description = current.trimmedDescription)
                .onSuccess { community -> _uiState.update { it.copy(isSubmitting = false, createdCommunity = community) } }
                .onFailure { error -> _uiState.update { it.copy(isSubmitting = false, toastMessage = toastFor(error)) } }
        }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    private fun toastFor(error: Throwable): UiText = if (error is ApiError.Server) UiText.Resource(R.string.create_community_name_taken) else error.toUiText()
}
