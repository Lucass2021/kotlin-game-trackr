package com.lucasdias.gametrackr.feature.app.notifications

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.NotificationFilter
import com.lucasdias.gametrackr.data.notifications.NotificationsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val repository: NotificationsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    private var listJob: Job? = null
    private val pendingFriendRequestIds = mutableSetOf<Int>()

    fun loadIfNeeded() {
        val state = _uiState.value
        if (state.currentPage > 0 || state.isLoading) return
        load()
    }

    fun load() {
        loadPage(1)
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.currentPage == 0 || state.currentPage >= state.lastPage || state.isLoading || state.isLoadingMore) return
        loadPage(state.currentPage + 1)
    }

    fun onFilterChange(filter: NotificationFilter) {
        if (filter == _uiState.value.filter) return
        _uiState.update { it.copy(filter = filter) }
        loadPage(1)
    }

    fun onMarkAllRead() {
        runAction(R.string.notifications_mark_all_read_error, { repository.markAllAsRead() }) {
            if (_uiState.value.filter == NotificationFilter.UNREAD) {
                showNoNotifications()
            } else {
                _uiState.update { state -> state.copy(items = state.items.map { it.copy(unread = false) }) }
            }
        }
    }

    fun onClearAll() {
        runAction(R.string.notifications_clear_error, { repository.clearNotifications() }) { showNoNotifications() }
    }

    fun onAcceptFriendRequest(id: Int) = respondToFriendRequest(id, accept = true)

    fun onDeclineFriendRequest(id: Int) = respondToFriendRequest(id, accept = false)

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun respondToFriendRequest(
        id: Int,
        accept: Boolean,
    ) {
        if (!pendingFriendRequestIds.add(id)) return
        runAction(
            R.string.notifications_friend_request_error,
            { repository.respondToFriendRequest(id, accept).also { pendingFriendRequestIds.remove(id) } },
        ) { removeAnsweredFriendRequest(id) }
    }

    private fun removeAnsweredFriendRequest(id: Int) {
        val state = _uiState.value
        if (state.currentPage < state.lastPage) {
            load()
        } else {
            _uiState.update { current -> current.copy(items = current.items.filterNot { it.id == id }) }
        }
    }

    private fun runAction(
        @StringRes errorMessage: Int,
        action: suspend () -> Result<Unit>,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            action()
                .onSuccess { onSuccess() }
                .onFailure { _uiState.update { it.copy(errorMessage = errorMessage) } }
        }
    }

    private fun showNoNotifications() {
        listJob?.cancel()
        _uiState.update {
            val page = maxOf(it.currentPage, 1)
            it.copy(
                items = emptyList(),
                currentPage = page,
                lastPage = page,
                isLoading = false,
                isLoadingMore = false,
                loadMoreFailed = false,
            )
        }
    }

    private fun loadPage(page: Int) {
        listJob?.cancel()
        val filter = _uiState.value.filter
        _uiState.update {
            if (page == 1) {
                it.copy(items = emptyList(), currentPage = 0, lastPage = 1, isLoading = true, isLoadingMore = false)
            } else {
                it.copy(isLoadingMore = true)
            }.copy(hasError = false, loadMoreFailed = false)
        }
        listJob =
            viewModelScope.launch {
                repository
                    .fetchNotifications(filter, page, NotificationsUiState.PAGE_SIZE)
                    .onSuccess { result ->
                        _uiState.update {
                            it.copy(
                                items = if (page == 1) result.items else it.items + result.items,
                                currentPage = result.currentPage,
                                lastPage = result.lastPage,
                                isLoading = false,
                                isLoadingMore = false,
                            )
                        }
                    }.onFailure {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoadingMore = false,
                                hasError = page == 1,
                                loadMoreFailed = page > 1,
                            )
                        }
                    }
            }
    }
}
