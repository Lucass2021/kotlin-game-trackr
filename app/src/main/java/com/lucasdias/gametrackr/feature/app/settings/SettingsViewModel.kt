package com.lucasdias.gametrackr.feature.app.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.NotificationSetting
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.data.auth.AccountRepository
import com.lucasdias.gametrackr.data.notifications.NotificationSettingsRepository
import com.lucasdias.gametrackr.feature.auth.toUiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val notificationSettingsRepository: NotificationSettingsRepository,
    private val accountRepository: AccountRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private val toggleJobs = mutableMapOf<NotificationSetting, Job>()

    fun loadIfNeeded() {
        val state = _uiState.value
        if (state.notificationSettings != null || state.isLoading) return
        load()
    }

    fun load() {
        loadJob?.cancel()
        _uiState.update { it.copy(isLoading = true, hasError = false) }
        loadJob =
            viewModelScope.launch {
                notificationSettingsRepository
                    .fetchSettings()
                    .onSuccess { settings ->
                        _uiState.update { it.copy(notificationSettings = settings, isLoading = false) }
                    }.onFailure { _uiState.update { it.copy(isLoading = false, hasError = true) } }
            }
    }

    fun onNotificationToggle(
        setting: NotificationSetting,
        enabled: Boolean,
    ) {
        if (_uiState.value.notificationSettings == null) return
        setLocally(setting, enabled)
        toggleJobs[setting]?.cancel()
        toggleJobs[setting] =
            viewModelScope.launch {
                notificationSettingsRepository.updateSetting(setting, enabled).onFailure {
                    setLocally(setting, !enabled)
                    _uiState.update { it.copy(toastMessage = UiText.Resource(R.string.settings_notification_update_error)) }
                }
            }
    }

    fun onDeleteAccount() {
        val state = _uiState.value
        if (state.isDeleting || state.isDeleted) return
        _uiState.update { it.copy(isDeleting = true, toastMessage = null) }
        viewModelScope.launch {
            accountRepository
                .deleteAccount()
                .onSuccess { _uiState.update { it.copy(isDeleting = false, isDeleted = true) } }
                .onFailure { error -> _uiState.update { it.copy(isDeleting = false, toastMessage = error.toUiText()) } }
        }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    private fun setLocally(
        setting: NotificationSetting,
        enabled: Boolean,
    ) {
        _uiState.update { state ->
            state.copy(notificationSettings = state.notificationSettings?.with(setting, enabled))
        }
    }
}
