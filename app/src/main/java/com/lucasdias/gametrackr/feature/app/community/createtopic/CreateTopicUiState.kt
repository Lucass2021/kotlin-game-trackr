package com.lucasdias.gametrackr.feature.app.community.createtopic

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.feature.app.community.Community

data class CreateTopicUiState(
    val community: Community? = null,
    val title: String = "",
    val body: String = "",
    val isCommunityLocked: Boolean = false,
    @param:StringRes val communityError: Int? = null,
    @param:StringRes val titleError: Int? = null,
    @param:StringRes val bodyError: Int? = null,
) {
    val hasContent: Boolean get() = title.isNotBlank() || body.isNotBlank()

    val titleRemaining: Int get() = TITLE_LIMIT - title.length

    val canSubmit: Boolean
        get() =
            community != null &&
                title.trim().length in 3..TITLE_LIMIT &&
                body.trim().length in 10..BODY_LIMIT

    companion object {
        const val TITLE_LIMIT = 120
        const val BODY_LIMIT = 2000
    }
}
