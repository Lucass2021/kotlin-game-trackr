package com.lucasdias.gametrackr.feature.app.community.createtopic

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.network.CommunityApi
import com.lucasdias.gametrackr.core.network.dto.CreatePostRequest
import com.lucasdias.gametrackr.core.network.dto.toDomain
import com.lucasdias.gametrackr.feature.app.community.Community
import com.lucasdias.gametrackr.feature.app.community.CommunityPost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateTopicViewModel(
    communityName: String,
    private val api: CommunityApi,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateTopicUiState())
    val uiState: StateFlow<CreateTopicUiState> = _uiState.asStateFlow()

    private var submitted = false

    val communities = mutableStateListOf<Community>()

    init {
        viewModelScope.launch {
            try {
                val response = api.getJoinedCommunities()
                communities.addAll(response.data.map { it.toDomain() })
            } catch (_: Exception) {
            }
            val locked =
                communityName
                    .takeIf { it.isNotBlank() }
                    ?.let { name -> communities.firstOrNull { it.name == name } }
            if (locked != null) {
                _uiState.update { it.copy(community = locked, isCommunityLocked = true) }
            }
        }
    }

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

    suspend fun submitPost(): CommunityPost? {
        submitted = true
        revalidate()
        val state = _uiState.value
        val community = state.community ?: return null
        if (!state.canSubmit) return null

        return try {
            val request =
                CreatePostRequest(
                    title = state.title.trim(),
                    description = state.body.trim(),
                    communityId = community.id,
                )
            api.createPost(request).post.toDomain()
        } catch (_: Exception) {
            null
        }
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
