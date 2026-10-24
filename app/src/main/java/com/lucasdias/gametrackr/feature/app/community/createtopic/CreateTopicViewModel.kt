package com.lucasdias.gametrackr.feature.app.community.createtopic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.data.community.CommunityRepository
import com.lucasdias.gametrackr.data.community.PostUpdates
import com.lucasdias.gametrackr.feature.auth.toUiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateTopicViewModel(
    community: Community?,
    private val repository: CommunityRepository,
    private val postUpdates: PostUpdates,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateTopicUiState(community = community, isCommunityLocked = community != null))
    val uiState: StateFlow<CreateTopicUiState> = _uiState.asStateFlow()

    private var communitiesJob: Job? = null

    fun loadIfNeeded() {
        val state = _uiState.value
        if (state.isCommunityLocked || state.isLoadingCommunities || state.communities.isNotEmpty()) return
        loadCommunities()
    }

    fun retryCommunities() = loadCommunities()

    fun onCommunityChange(value: Community) {
        if (_uiState.value.isCommunityLocked) return
        _uiState.update { it.copy(community = value) }
    }

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value) }
    }

    fun onBodyChange(value: String) {
        _uiState.update { it.copy(body = value) }
    }

    fun onSubmit() {
        val current = _uiState.value
        if (current.isSubmitting || current.createdPost != null) return
        _uiState.update { it.copy(isSubmitted = true) }
        val community = current.community
        if (!current.canSubmit || community == null) return

        _uiState.update { it.copy(isSubmitting = true, toastMessage = null) }
        viewModelScope.launch {
            repository
                .createPost(title = current.trimmedTitle, description = current.trimmedBody, communityId = community.id)
                .onSuccess { post ->
                    postUpdates.publishCreated(post)
                    _uiState.update { it.copy(isSubmitting = false, createdPost = post) }
                }.onFailure { error -> _uiState.update { it.copy(isSubmitting = false, toastMessage = error.toUiText()) } }
        }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    private fun loadCommunities() {
        communitiesJob?.cancel()
        _uiState.update { it.copy(isLoadingCommunities = true, communitiesFailed = false) }
        communitiesJob =
            viewModelScope.launch {
                repository
                    .fetchJoinedCommunities()
                    .onSuccess { communities -> _uiState.update { it.copy(communities = communities, isLoadingCommunities = false) } }
                    .onFailure { _uiState.update { it.copy(isLoadingCommunities = false, communitiesFailed = true) } }
            }
    }
}
