package com.lucasdias.gametrackr.feature.auth.register

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.auth.FakeGoogleAuth
import com.lucasdias.gametrackr.core.auth.GoogleAuthState
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.data.auth.FakeAuthRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RegisterViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeAuthRepository()
    private val googleAuth = FakeGoogleAuth()

    private fun viewModel(
        name: String = NAME,
        email: String = EMAIL,
        password: String = PASSWORD,
        confirmPassword: String = PASSWORD,
        acceptTerms: Boolean = true,
    ) = RegisterViewModel(repository, googleAuth).apply {
        onNameChange(name)
        onEmailChange(email)
        onPasswordChange(password)
        onConfirmPasswordChange(confirmPassword)
        if (acceptTerms) onToggleTerms()
    }

    private fun TestScope.collect(viewModel: RegisterViewModel) {
        backgroundScope.launch { viewModel.uiState.collect {} }
    }

    @Test
    fun `errors show only after the first submit`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel(name = "", email = "", password = "", confirmPassword = "", acceptTerms = false)
            collect(viewModel)
            advanceUntilIdle()
            assertNull(viewModel.uiState.value.nameError)

            viewModel.onSubmit()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(UiText.Resource(R.string.validation_name_required), state.nameError)
            assertEquals(UiText.Resource(R.string.validation_email_required), state.emailError)
            assertEquals(UiText.Resource(R.string.validation_password_required), state.passwordError)
            assertNull(state.confirmPasswordError)
            assertEquals(UiText.Resource(R.string.validation_terms_required), state.termsError)
            assertEquals(0, repository.registerCount)
        }

    @Test
    fun `short name short password and mismatched confirmation are rejected`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel(name = " Al ", password = "abc", confirmPassword = "abd")
            collect(viewModel)

            viewModel.onSubmit()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(UiText.Resource(R.string.validation_name_too_short), state.nameError)
            assertEquals(UiText.Resource(R.string.validation_password_too_short, listOf(6)), state.passwordError)
            assertEquals(UiText.Resource(R.string.validation_passwords_dont_match), state.confirmPasswordError)
            assertNull(state.termsError)
            assertEquals(0, repository.registerCount)
        }

    @Test
    fun `accepting the terms after submit clears the terms error`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel(acceptTerms = false)
            collect(viewModel)
            viewModel.onSubmit()
            advanceUntilIdle()

            viewModel.onToggleTerms()
            advanceUntilIdle()

            assertNull(viewModel.uiState.value.termsError)
        }

    @Test
    fun `submit sends trimmed name and email and marks the account as registered`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel(name = "  $NAME  ", email = "  $EMAIL  ")
            collect(viewModel)

            viewModel.onSubmit()
            advanceUntilIdle()

            assertEquals(NAME, repository.lastName)
            assertEquals(EMAIL, repository.lastEmail)
            assertEquals(PASSWORD, repository.lastPassword)
            assertTrue(viewModel.uiState.value.isRegistered)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `register failure shows a toast and keeps the form`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = ApiError.Validation(mapOf("email" to listOf(EMAIL_TAKEN)), EMAIL_TAKEN)
            val viewModel = viewModel()
            collect(viewModel)

            viewModel.onSubmit()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(UiText.Raw(EMAIL_TAKEN), state.toastMessage)
            assertFalse(state.isRegistered)
            assertEquals(NAME, state.name)

            viewModel.onToastShown()
            advanceUntilIdle()
            assertNull(viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `double submit and submit after success send a single request`() =
        runTest(mainDispatcherRule.dispatcher) {
            val gate = CompletableDeferred<Unit>()
            repository.gate = gate
            val viewModel = viewModel()
            collect(viewModel)

            viewModel.onSubmit()
            viewModel.onSubmit()
            gate.complete(Unit)
            advanceUntilIdle()
            viewModel.onSubmit()
            viewModel.onSignUpWithGoogle()
            advanceUntilIdle()

            assertEquals(1, repository.registerCount)
            assertEquals(0, googleAuth.launchCount)
        }

    @Test
    fun `google is ignored while registering and repeat taps launch once`() =
        runTest(mainDispatcherRule.dispatcher) {
            val gate = CompletableDeferred<Unit>()
            repository.gate = gate
            val viewModel = viewModel()
            collect(viewModel)

            viewModel.onSubmit()
            viewModel.onSignUpWithGoogle()
            assertEquals(0, googleAuth.launchCount)
            repository.failure = ApiError.Network
            gate.complete(Unit)
            advanceUntilIdle()

            viewModel.onSignUpWithGoogle()
            viewModel.onSignUpWithGoogle()
            assertEquals(1, googleAuth.launchCount)
        }

    @Test
    fun `google failure shows a toast that clears the google state`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            collect(viewModel)

            googleAuth.state.value = GoogleAuthState.FAILED
            advanceUntilIdle()
            assertEquals(UiText.Resource(R.string.error_generic), viewModel.uiState.value.toastMessage)

            viewModel.onToastShown()
            advanceUntilIdle()
            assertEquals(GoogleAuthState.IDLE, googleAuth.state.value)
        }

    private companion object {
        const val NAME = "Player One"
        const val EMAIL = "player@gametrackr.com"
        const val PASSWORD = "secret123"
        const val EMAIL_TAKEN = "The email has already been taken."
    }
}
