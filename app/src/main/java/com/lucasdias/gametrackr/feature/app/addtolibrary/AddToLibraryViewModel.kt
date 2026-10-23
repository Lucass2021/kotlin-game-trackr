package com.lucasdias.gametrackr.feature.app.addtolibrary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.GameDetail
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.model.NewLibraryEntry
import com.lucasdias.gametrackr.data.library.LibraryRepository
import com.lucasdias.gametrackr.data.library.LibraryUpdates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddToLibraryViewModel(
    private val repository: LibraryRepository,
    private val libraryUpdates: LibraryUpdates,
    private val slug: String,
    private val game: GameDetail,
) : ViewModel() {
    private val _uiState = MutableStateFlow(initialState())
    val uiState: StateFlow<AddToLibraryUiState> = _uiState.asStateFlow()

    fun onStatusChange(status: LibraryStatus) {
        _uiState.update { it.copy(status = status) }
    }

    fun onRatingChange(rating: Int) {
        _uiState.update { it.copy(rating = rating) }
    }

    fun onHoursChange(hours: String) {
        _uiState.update { it.copy(hours = hours.filter { it in '0'..'9' }.take(MAX_HOURS_LENGTH)) }
    }

    fun onPlatformChange(platform: String) {
        _uiState.update { it.copy(platform = platform) }
    }

    fun onStartedChange(startedAt: Long?) {
        _uiState.update { it.copy(startedAt = startedAt) }
    }

    fun onFinishedChange(finishedAt: Long?) {
        _uiState.update { it.copy(finishedAt = finishedAt) }
    }

    fun onReviewChange(review: String) {
        _uiState.update { it.copy(review = review) }
    }

    fun onSave() {
        if (_uiState.value.isSaving) return
        _uiState.update { it.copy(isSubmitted = true) }
        val state = _uiState.value
        if (state.dateError != null) return
        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            repository
                .addToLibrary(state.toEntry())
                .onSuccess {
                    libraryUpdates.publish(slug)
                    _uiState.update { it.copy(isSaving = false, isSaved = true) }
                }.onFailure {
                    _uiState.update { it.copy(isSaving = false, toastMessage = R.string.add_library_save_error) }
                }
        }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun reset() {
        if (_uiState.value.isSaving) return
        _uiState.value = initialState()
    }

    private fun initialState() = AddToLibraryUiState(platforms = game.platforms, platform = game.platforms.firstOrNull())

    private fun AddToLibraryUiState.toEntry() =
        NewLibraryEntry(
            title = game.title,
            slug = slug,
            status = status,
            rating = rating,
            hours = hours.toIntOrNull(),
            platform = platform,
            startedAt = startedAt,
            finishedAt = finishedAt,
            review = review.trim().ifEmpty { null },
            coverStart = game.coverStart,
            coverEnd = game.coverEnd,
        )

    private companion object {
        const val MAX_HOURS_LENGTH = 5
    }
}
