package com.lucasdias.gametrackr.feature.app.addtolibrary

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.LibraryStatus

data class AddToLibraryUiState(
    val status: LibraryStatus = LibraryStatus.PLAYING,
    val rating: Int = 0,
    val hours: String = "",
    val platforms: List<String> = emptyList(),
    val platform: String? = null,
    val startedAt: Long? = null,
    val finishedAt: Long? = null,
    val review: String = "",
    val isSubmitted: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    @param:StringRes val toastMessage: Int? = null,
) {
    @get:StringRes
    val dateError: Int?
        get() {
            if (!isSubmitted || startedAt == null || finishedAt == null) return null
            return if (finishedAt < startedAt) R.string.add_library_date_error else null
        }
}
