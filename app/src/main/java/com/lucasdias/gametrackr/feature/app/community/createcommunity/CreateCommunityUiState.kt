package com.lucasdias.gametrackr.feature.app.community.createcommunity

import androidx.annotation.StringRes

data class CreateCommunityUiState(
    val name: String = "",
    val description: String = "",
    val isSubmitting: Boolean = false,
    @param:StringRes val nameError: Int? = null,
    @param:StringRes val descriptionError: Int? = null,
    val submitError: String? = null,
) {
    val handle: String get() = name.filterNot { it.isWhitespace() }

    val isRenamed: Boolean get() = handle != name && handle.isNotEmpty()

    val hasContent: Boolean get() = handle.isNotEmpty() || description.isNotBlank()

    val nameRemaining: Int get() = NAME_LIMIT - handle.length

    val canSubmit: Boolean
        get() =
            !isSubmitting &&
                handle.length in 3..NAME_LIMIT &&
                description.trim().length in 10..DESCRIPTION_LIMIT

    companion object {
        const val NAME_LIMIT = 255
        const val DESCRIPTION_LIMIT = 2000
    }
}
