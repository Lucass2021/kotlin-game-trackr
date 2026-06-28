package com.lucasdias.gametrackr.feature.auth.register

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.auth.AuthRepository
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.feature.auth.toMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "RegisterViewModel"

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private var submitted = false

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value) }
        revalidate()
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value) }
        revalidate()
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value) }
        revalidate()
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value) }
        revalidate()
    }

    fun onToggleTerms() {
        _uiState.update { it.copy(acceptedTerms = !it.acceptedTerms) }
        revalidate()
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onGoogleSignUp() {
        Log.d(TAG, "Sign up with Google")
    }

    fun onSubmit() {
        submitted = true
        revalidate()
        val state = _uiState.value
        if (hasFieldErrors(state)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.register(
                name = state.name.trim(),
                email = state.email.trim(),
                password = state.password,
                passwordConfirmation = state.confirmPassword
            )
            _uiState.update { it.copy(isLoading = false) }
            result.onSuccess {
                _uiState.update { it.copy(registered = true) }
            }
            result.onFailure { error ->
                val message = (error as? ApiError)?.toMessage(context)
                    ?: context.getString(R.string.error_generic)
                _uiState.update { it.copy(errorMessage = message) }
            }
        }
    }

    private fun revalidate() {
        if (!submitted) return
        _uiState.update {
            it.copy(
                nameError = nameErrorFor(it.name),
                emailError = emailErrorFor(it.email),
                passwordError = passwordErrorFor(it.password),
                confirmPasswordError = confirmPasswordErrorFor(it.password, it.confirmPassword),
                termsError = termsErrorFor(it.acceptedTerms)
            )
        }
    }

    private fun hasFieldErrors(state: RegisterUiState): Boolean =
        state.nameError != null ||
            state.emailError != null ||
            state.passwordError != null ||
            state.confirmPasswordError != null ||
            state.termsError != null

    private fun nameErrorFor(name: String): Int? {
        val trimmed = name.trim()
        return when {
            trimmed.isEmpty() -> R.string.validation_name_required
            trimmed.length < 3 -> R.string.validation_name_too_short
            else -> null
        }
    }

    private fun emailErrorFor(email: String): Int? {
        val trimmed = email.trim()
        return when {
            trimmed.isEmpty() -> R.string.validation_email_required
            !trimmed.contains("@") || !trimmed.contains(".") -> R.string.validation_email_invalid
            else -> null
        }
    }

    private fun passwordErrorFor(password: String): Int? = when {
        password.isEmpty() -> R.string.validation_password_required
        password.length < 6 -> R.string.validation_password_too_short
        else -> null
    }

    private fun confirmPasswordErrorFor(password: String, confirm: String): Int? =
        if (confirm != password) R.string.validation_passwords_dont_match else null

    private fun termsErrorFor(accepted: Boolean): Int? =
        if (!accepted) R.string.validation_terms_required else null
}
