package com.lucasdias.gametrackr.feature.auth.resetpassword

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
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ResetPasswordViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeAuthRepository()

    private fun viewModel(
        password: String = PASSWORD,
        confirmPassword: String = PASSWORD,
    ) = ResetPasswordViewModel(repository, EMAIL, CODE).apply {
        onPasswordChange(password)
        onConfirmPasswordChange(confirmPassword)
    }

    @Test
    fun `errors show only after the first submit`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel(password = "", confirmPassword = "x")
            assertNull(viewModel.uiState.value.passwordError)

            viewModel.onSubmit()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(UiText.Resource(R.string.validation_password_required), state.passwordError)
            assertEquals(UiText.Resource(R.string.validation_passwords_dont_match), state.confirmPasswordError)
            assertEquals(0, repository.resetCount)
        }

    @Test
    fun `short password is rejected`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel(password = "abc", confirmPassword = "abc")

            viewModel.onSubmit()
            advanceUntilIdle()

            assertEquals(UiText.Resource(R.string.validation_password_too_short, listOf(6)), viewModel.uiState.value.passwordError)
            assertEquals(0, repository.resetCount)
        }

    @Test
    fun `submit sends the email code and new password and marks it as reset`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()

            viewModel.onSubmit()
            advanceUntilIdle()

            assertEquals(EMAIL, repository.lastEmail)
            assertEquals(CODE, repository.lastCode)
            assertEquals(PASSWORD, repository.lastPassword)
            assertTrue(viewModel.uiState.value.isReset)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `reset failure shows a toast and keeps the form`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = ApiError.BadRequest(INVALID_CODE)
            val viewModel = viewModel()

            viewModel.onSubmit()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(UiText.Raw(INVALID_CODE), state.toastMessage)
            assertFalse(state.isReset)
            assertEquals(PASSWORD, state.password)

            viewModel.onToastShown()
            assertNull(viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `double submit and submit after success send a single request`() =
        runTest(mainDispatcherRule.dispatcher) {
            val gate = CompletableDeferred<Unit>()
            repository.gate = gate
            val viewModel = viewModel()

            viewModel.onSubmit()
            viewModel.onSubmit()
            gate.complete(Unit)
            advanceUntilIdle()
            viewModel.onSubmit()
            advanceUntilIdle()

            assertEquals(1, repository.resetCount)
        }

    private companion object {
        const val EMAIL = "player@gametrackr.com"
        const val CODE = "123456"
        const val PASSWORD = "secret123"
        const val INVALID_CODE = "Invalid code"
    }
}
