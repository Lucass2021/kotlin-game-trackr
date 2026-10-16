package com.lucasdias.gametrackr.feature.app.profile.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.SetupImageUpload
import com.lucasdias.gametrackr.core.model.SetupItem
import com.lucasdias.gametrackr.data.profile.SetupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditSetupViewModel(
    setup: SetupItem?,
    private val repository: SetupRepository,
    private val encoder: SetupPhotoEncoder,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(
            EditSetupUiState(
                isNew = setup == null,
                setupId = setup?.id,
                title = setup?.title.orEmpty(),
                description = setup?.description.orEmpty(),
                images = setup?.images.orEmpty(),
            ),
        )
    val uiState: StateFlow<EditSetupUiState> = _uiState.asStateFlow()

    private var submitted = false

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value) }
        revalidate()
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value) }
        revalidate()
    }

    fun onPhotosPicked(photos: List<String>) {
        _uiState.update { state ->
            val picked = photos.distinct().filterNot { it in state.newPhotos }
            state.copy(newPhotos = state.newPhotos + picked.take(state.remainingPhotoSlots))
        }
    }

    fun onRemoveImage(id: Int) {
        _uiState.update { state ->
            state.copy(
                images = state.images.filterNot { it.id == id },
                removedImageIds = state.removedImageIds + id,
            )
        }
    }

    fun onRemoveNewPhoto(photo: String) {
        _uiState.update { it.copy(newPhotos = it.newPhotos - photo) }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onSave(onSaved: (SetupItem) -> Unit) {
        submitted = true
        revalidate()
        val state = _uiState.value
        if (state.titleError != null || state.descriptionError != null || state.isBusy) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            val uploads = state.newPhotos.map { encoder.encode(it) }
            if (uploads.any { it == null }) {
                _uiState.update { it.copy(isSaving = false, errorMessage = R.string.setup_photo_error) }
                return@launch
            }

            save(state, uploads.filterNotNull())
                .onSuccess { saved ->
                    _uiState.update {
                        it.copy(
                            setupId = saved.id,
                            images = saved.images,
                            newPhotos = emptyList(),
                            removedImageIds = emptyList(),
                            isDone = true,
                        )
                    }
                    onSaved(saved)
                }.onFailure { _uiState.update { it.copy(errorMessage = R.string.setup_save_error) } }
            _uiState.update { it.copy(isSaving = false) }
        }
    }

    fun onDelete(onDeleted: (Int) -> Unit) {
        val state = _uiState.value
        val id = state.setupId ?: return
        if (state.isBusy) return

        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true, errorMessage = null) }
            repository
                .deleteSetup(id)
                .onSuccess {
                    _uiState.update { it.copy(isDone = true) }
                    onDeleted(id)
                }.onFailure { _uiState.update { it.copy(errorMessage = R.string.setup_delete_error) } }
            _uiState.update { it.copy(isDeleting = false) }
        }
    }

    private suspend fun save(
        state: EditSetupUiState,
        uploads: List<SetupImageUpload>,
    ): Result<SetupItem> {
        val title = state.title.trim()
        val description = state.description.trim()
        val id = state.setupId ?: return repository.createSetup(title, description, uploads)
        return repository.updateSetup(id, title, description, uploads, state.removedImageIds)
    }

    private fun revalidate() {
        if (!submitted) return
        _uiState.update {
            it.copy(
                titleError = titleErrorFor(it.title),
                descriptionError = descriptionErrorFor(it.description),
            )
        }
    }

    private fun titleErrorFor(title: String): Int? {
        val trimmed = title.trim()
        return when {
            trimmed.isEmpty() -> R.string.setup_title_required
            trimmed.length > EditSetupUiState.TITLE_LIMIT -> R.string.setup_title_too_long
            else -> null
        }
    }

    private fun descriptionErrorFor(description: String): Int? =
        if (description.trim().length > EditSetupUiState.DESCRIPTION_LIMIT) R.string.setup_description_too_long else null
}
