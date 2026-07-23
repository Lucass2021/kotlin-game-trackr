package com.lucasdias.gametrackr.feature.app.community.createtopic

import androidx.lifecycle.ViewModel
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletStart
import com.lucasdias.gametrackr.feature.app.community.Community
import com.lucasdias.gametrackr.feature.app.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.CommunityPost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateTopicViewModel(
    communityName: String,
) : ViewModel() {
    private val locked =
        communityName
            .takeIf { it.isNotBlank() }
            ?.let { name -> CommunityMockData.all.firstOrNull { it.name == name } }

    private val _uiState =
        MutableStateFlow(
            CreateTopicUiState(community = locked, isCommunityLocked = locked != null),
        )
    val uiState: StateFlow<CreateTopicUiState> = _uiState.asStateFlow()

    private var submitted = false

    val communities: List<Community> = CommunityMockData.all.filter { it.isJoined }

    fun onCommunityChange(value: Community) {
        _uiState.update { it.copy(community = value) }
        revalidate()
    }

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value) }
        revalidate()
    }

    fun onBodyChange(value: String) {
        _uiState.update { it.copy(body = value) }
        revalidate()
    }

    fun onSubmit(): CommunityPost? {
        submitted = true
        revalidate()
        val state = _uiState.value
        val community = state.community ?: return null
        if (!state.canSubmit) return null

        return CommunityPost(
            id = System.currentTimeMillis(),
            author = "You",
            timeAgo = "now",
            communityName = community.name,
            title = state.title.trim(),
            preview = state.body.trim(),
            likes = 0,
            comments = 0,
            hasMedia = false,
            avatarStart = CoverVioletStart,
            avatarEnd = CoverVioletEnd,
            mediaStart = CoverVioletStart,
            mediaEnd = CoverVioletEnd,
        )
    }

    private fun revalidate() {
        if (!submitted) return
        _uiState.update {
            it.copy(
                communityError = communityErrorFor(it.community),
                titleError = titleErrorFor(it.title),
                bodyError = bodyErrorFor(it.body),
            )
        }
    }

    private fun communityErrorFor(community: Community?): Int? = if (community == null) R.string.validation_community_required else null

    private fun titleErrorFor(title: String): Int? {
        val trimmed = title.trim()
        return when {
            trimmed.isEmpty() -> R.string.validation_topic_title_required
            trimmed.length < 3 -> R.string.validation_topic_title_too_short
            trimmed.length > CreateTopicUiState.TITLE_LIMIT -> R.string.validation_topic_title_too_long
            else -> null
        }
    }

    private fun bodyErrorFor(body: String): Int? {
        val trimmed = body.trim()
        return when {
            trimmed.isEmpty() -> R.string.validation_topic_body_required
            trimmed.length < 10 -> R.string.validation_topic_body_too_short
            trimmed.length > CreateTopicUiState.BODY_LIMIT -> R.string.validation_topic_body_too_long
            else -> null
        }
    }
}
