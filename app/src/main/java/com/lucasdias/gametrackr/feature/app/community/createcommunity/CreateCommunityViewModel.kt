package com.lucasdias.gametrackr.feature.app.community.createcommunity

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.data.community.CommunityRepository
import com.lucasdias.gametrackr.feature.auth.toMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateCommunityViewModel(
    private val repository: CommunityRepository,
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
        val result = repository.createCommunity(title = state.handle, description = state.description.trim())
        _uiState.update { it.copy(isSubmitting = false) }
        return result
            .onFailure { error -> _uiState.update { it.copy(submitError = submitErrorFor(error)) } }
            .getOrNull()
            ?.copy(isJoined = true, members = FIRST_MEMBER_COUNT)
    }

    private fun submitErrorFor(error: Throwable): String =
        when (error) {
            is ApiError.Server -> context.getString(R.string.create_community_name_taken)
            else -> (error as? ApiError)?.toMessage(context) ?: context.getString(R.string.error_generic)
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

    private companion object {
        const val FIRST_MEMBER_COUNT = "1"
    }
}
