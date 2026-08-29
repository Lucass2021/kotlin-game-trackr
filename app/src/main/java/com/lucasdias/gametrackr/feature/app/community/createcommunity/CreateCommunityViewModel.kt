package com.lucasdias.gametrackr.feature.app.community.createcommunity

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.network.CommunityApi
import com.lucasdias.gametrackr.core.network.dto.CreateCommunityRequest
import com.lucasdias.gametrackr.core.network.dto.toDomain
import com.lucasdias.gametrackr.core.network.toApiError
import com.lucasdias.gametrackr.feature.app.community.Community
import com.lucasdias.gametrackr.feature.auth.toMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

class CreateCommunityViewModel(
    private val api: CommunityApi,
    private val json: Json,
    private val context: Context,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateCommunityUiState())
    val uiState: StateFlow<CreateCommunityUiState> = _uiState.asStateFlow()

    private var submitted = false

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value) }
        revalidate()
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value) }
        revalidate()
    }

    suspend fun submit(): Community? {
        submitted = true
        revalidate()
        val state = _uiState.value
        if (!state.canSubmit) return null

        _uiState.update { it.copy(isSubmitting = true, submitError = null) }
        return try {
            val request =
                CreateCommunityRequest(
                    title = state.handle,
                    description = state.description.trim(),
                )
            api
                .createCommunity(request)
                .community
                .toDomain()
                .copy(isJoined = true, members = "1")
        } catch (throwable: Exception) {
            val message =
                when (val error = throwable.toApiError(json)) {
                    is ApiError.Server -> context.getString(R.string.create_community_name_taken)
                    else -> error.toMessage(context)
                }
            _uiState.update { it.copy(submitError = message) }
            null
        } finally {
            _uiState.update { it.copy(isSubmitting = false) }
        }
    }

    private fun revalidate() {
        if (!submitted) return
        _uiState.update {
            it.copy(
                nameError = nameErrorFor(it.name),
                descriptionError = descriptionErrorFor(it.description),
            )
        }
    }

    private fun nameErrorFor(name: String): Int? {
        val handle = name.filterNot { it.isWhitespace() }
        return when {
            handle.isEmpty() -> R.string.validation_community_name_required
            handle.length < 3 -> R.string.validation_community_name_too_short
            handle.length > CreateCommunityUiState.NAME_LIMIT -> R.string.validation_community_name_too_long
            else -> null
        }
    }

    private fun descriptionErrorFor(description: String): Int? {
        val trimmed = description.trim()
        return when {
            trimmed.isEmpty() -> {
                R.string.validation_community_description_required
            }

            trimmed.length < 10 -> {
                R.string.validation_community_description_too_short
            }

            trimmed.length > CreateCommunityUiState.DESCRIPTION_LIMIT -> {
                R.string.validation_community_description_too_long
            }

            else -> {
                null
            }
        }
    }
}
