package com.lucasdias.gametrackr.feature.auth.forgotpassword

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.data.auth.FakeAuthRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class ForgotPasswordViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeAuthRepository()

    private fun viewModel(email: String = EMAIL) =
        ForgotPasswordViewModel(repository).apply {
            onEmailChange(email)
        }

    @Test
    fun `errors show only after the first submit`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel(email = "")
            assertNull(viewModel.uiState.value.emailError)

            viewModel.onSubmit()
            advanceUntilIdle()

            assertEquals(UiText.Resource(R.string.validation_email_required), viewModel.uiState.value.emailError)
            assertEquals(0, repository.forgotPasswordCount)
        }

    @Test
    fun `submit sends the trimmed email and hands it to the next step once`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel(email = "  $EMAIL  ")

            viewModel.onSubmit()
            advanceUntilIdle()

            assertEquals(EMAIL, repository.lastEmail)
            assertEquals(EMAIL, viewModel.uiState.value.sentToEmail)
            assertFalse(viewModel.uiState.value.isLoading)

            viewModel.onCodeSentHandled()
            assertNull(viewModel.uiState.value.sentToEmail)
        }

    @Test
    fun `send failure shows a toast and stays on the screen`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = ApiError.NotFound
            val viewModel = viewModel()

            viewModel.onSubmit()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(UiText.Resource(R.string.error_not_found), state.toastMessage)
            assertNull(state.sentToEmail)
            assertEquals(EMAIL, state.email)

            viewModel.onToastShown()
            assertNull(viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `double submit sends a single code request`() =
        runTest(mainDispatcherRule.dispatcher) {
            val gate = CompletableDeferred<Unit>()
            repository.gate = gate
            val viewModel = viewModel()

            viewModel.onSubmit()
            viewModel.onSubmit()
            gate.complete(Unit)
            advanceUntilIdle()

            assertEquals(1, repository.forgotPasswordCount)
        }

    @Test
    fun `a new submit after coming back sends a new code`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            viewModel.onSubmit()
            advanceUntilIdle()
            viewModel.onCodeSentHandled()

            viewModel.onSubmit()
            advanceUntilIdle()

            assertEquals(2, repository.forgotPasswordCount)
        }

    private companion object {
        const val EMAIL = "player@gametrackr.com"
    }
}
