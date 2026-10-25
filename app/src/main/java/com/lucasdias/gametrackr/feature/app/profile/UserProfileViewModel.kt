package com.lucasdias.gametrackr.feature.app.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Profile
import com.lucasdias.gametrackr.core.model.UserOverview
import com.lucasdias.gametrackr.data.profile.ProfileOverviewRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val repository: ProfileOverviewRepository,
    private val username: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserProfileUiState())
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private var friendJob: Job? = null

    fun loadIfNeeded() {
        val state = _uiState.value
        if (state.overview != null || state.isLoading) return
        load()
    }

    fun load() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob =
            viewModelScope.launch {
                repository
                    .fetchUserOverview(username)
                    .onSuccess { result -> _uiState.update { it.showing(result) } }
                    .onFailure { _uiState.update { it.copy(isLoading = false, hasError = true) } }
            }
    }

    fun onFriendAction(isGuest: Boolean) {
        if (isGuest) {
            _uiState.update { it.copy(toastMessage = R.string.user_profile_guest_add_friend) }
            return
        }
        val isFriend = !_uiState.value.isFriend
        _uiState.update { it.copy(isFriend = isFriend) }
        friendJob?.cancel()
        friendJob =
            viewModelScope.launch {
                repository.setFriend(username, isFriend).onFailure {
                    _uiState.update { it.copy(isFriend = !isFriend, toastMessage = R.string.user_profile_friend_error) }
                }
            }
    }

    fun onMessage(isGuest: Boolean) {
        if (isGuest) _uiState.update { it.copy(toastMessage = R.string.user_profile_guest_message) }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    private fun UserProfileUiState.showing(result: UserOverview) =
        copy(
            profile =
                Profile(
                    name = result.name,
                    username = "@${result.username}",
                    bio = result.bio,
                    joinedAt = result.joinedAt,
                    avatarHex = result.avatarHex,
                ),
            overview = result.overview,
            isFriend = result.isFriend,
            isLoading = false,
        )
}
