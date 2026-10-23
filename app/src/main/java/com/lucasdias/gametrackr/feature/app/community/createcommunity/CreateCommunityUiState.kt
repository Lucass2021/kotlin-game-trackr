package com.lucasdias.gametrackr.feature.app.community.createcommunity

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.ui.text.UiText

data class CreateCommunityUiState(
    val name: String = "",
    val description: String = "",
    val isSubmitted: Boolean = false,
    val isSubmitting: Boolean = false,
    val createdCommunity: Community? = null,
    val toastMessage: UiText? = null,
) {
    val handle: String get() = name.filterNot { it.isWhitespace() }

    val trimmedDescription: String get() = description.trim()

    val isRenamed: Boolean get() = handle != name && handle.isNotEmpty()

    val hasContent: Boolean get() = handle.isNotEmpty() || trimmedDescription.isNotEmpty()

    val nameRemaining: Int get() = NAME_LIMIT - handle.length

    val nameError: UiText? get() = if (isSubmitted) nameErrorFor(handle) else null

    val descriptionError: UiText? get() = if (isSubmitted) descriptionErrorFor(trimmedDescription) else null

    val canSubmit: Boolean get() = nameErrorFor(handle) == null && descriptionErrorFor(trimmedDescription) == null

    companion object {
        const val NAME_MIN = 3
        const val NAME_LIMIT = 255
        const val DESCRIPTION_MIN = 10
        const val DESCRIPTION_LIMIT = 2000

        private fun nameErrorFor(handle: String): UiText? =
            when {
                handle.isEmpty() -> UiText.Resource(R.string.validation_community_name_required)
                handle.length < NAME_MIN -> UiText.Resource(R.string.validation_community_name_too_short)
                handle.length > NAME_LIMIT -> UiText.Resource(R.string.validation_community_name_too_long, listOf(NAME_LIMIT))
                else -> null
            }

        private fun descriptionErrorFor(description: String): UiText? =
            when {
                description.isEmpty() -> {
                    UiText.Resource(R.string.validation_community_description_required)
                }

                description.length < DESCRIPTION_MIN -> {
                    UiText.Resource(R.string.validation_community_description_too_short)
                }

                description.length > DESCRIPTION_LIMIT -> {
                    UiText.Resource(R.string.validation_community_description_too_long, listOf(DESCRIPTION_LIMIT))
                }

                else -> {
                    null
                }
            }
    }
}
