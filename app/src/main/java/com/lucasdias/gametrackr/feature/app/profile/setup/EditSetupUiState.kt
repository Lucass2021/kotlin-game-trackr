package com.lucasdias.gametrackr.feature.app.profile.setup

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.core.model.SetupImage

data class EditSetupUiState(
    val isNew: Boolean = true,
    val setupId: Int? = null,
    val title: String = "",
    val description: String = "",
    val images: List<SetupImage> = emptyList(),
    val newPhotos: List<String> = emptyList(),
    val removedImageIds: List<Int> = emptyList(),
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isDone: Boolean = false,
    @param:StringRes val titleError: Int? = null,
    @param:StringRes val descriptionError: Int? = null,
    @param:StringRes val errorMessage: Int? = null,
) {
    val photoCount: Int get() = images.size + newPhotos.size

    val remainingPhotoSlots: Int get() = (PHOTO_LIMIT - photoCount).coerceAtLeast(0)

    val isBusy: Boolean get() = isSaving || isDeleting || isDone

    companion object {
        const val PHOTO_LIMIT = 10
        const val TITLE_LIMIT = 255
        const val DESCRIPTION_LIMIT = 5000
    }
}
