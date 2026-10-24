package com.lucasdias.gametrackr.feature.app.community.createtopic

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.ui.text.UiText

data class CreateTopicUiState(
    val community: Community? = null,
    val title: String = "",
    val body: String = "",
    val isCommunityLocked: Boolean = false,
    val communities: List<Community> = emptyList(),
    val isLoadingCommunities: Boolean = false,
    val communitiesFailed: Boolean = false,
    val isSubmitted: Boolean = false,
    val isSubmitting: Boolean = false,
    val createdPost: CommunityPost? = null,
    val toastMessage: UiText? = null,
) {
    val trimmedTitle: String get() = title.trim()

    val trimmedBody: String get() = body.trim()

    val hasContent: Boolean get() = trimmedTitle.isNotEmpty() || trimmedBody.isNotEmpty()

    val titleRemaining: Int get() = TITLE_LIMIT - trimmedTitle.length

    val communityError: UiText? get() = if (isSubmitted) communityErrorFor(community) else null

    val titleError: UiText? get() = if (isSubmitted) titleErrorFor(trimmedTitle) else null

    val bodyError: UiText? get() = if (isSubmitted) bodyErrorFor(trimmedBody) else null

    val canSubmit: Boolean
        get() = communityErrorFor(community) == null && titleErrorFor(trimmedTitle) == null && bodyErrorFor(trimmedBody) == null

    companion object {
        const val TITLE_MIN = 3
        const val TITLE_LIMIT = 120
        const val BODY_MIN = 10
        const val BODY_LIMIT = 2000

        private fun communityErrorFor(community: Community?): UiText? = if (community == null) UiText.Resource(R.string.validation_community_required) else null

        private fun titleErrorFor(title: String): UiText? =
            when {
                title.isEmpty() -> UiText.Resource(R.string.validation_topic_title_required)
                title.length < TITLE_MIN -> UiText.Resource(R.string.validation_topic_title_too_short)
                title.length > TITLE_LIMIT -> UiText.Resource(R.string.validation_topic_title_too_long, listOf(TITLE_LIMIT))
                else -> null
            }

        private fun bodyErrorFor(body: String): UiText? =
            when {
                body.isEmpty() -> UiText.Resource(R.string.validation_topic_body_required)
                body.length < BODY_MIN -> UiText.Resource(R.string.validation_topic_body_too_short)
                body.length > BODY_LIMIT -> UiText.Resource(R.string.validation_topic_body_too_long, listOf(BODY_LIMIT))
                else -> null
            }
    }
}
