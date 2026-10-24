package com.lucasdias.gametrackr.feature.auth.verifyresetcode

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.data.auth.FakeAuthRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class VerifyResetCodeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeAuthRepository()

    private fun viewModel() = VerifyResetCodeViewModel(repository, EMAIL)

    @Test
    fun `the view model runs the countdown and unlocks resend`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            runCurrent()
            assertEquals(30, viewModel.uiState.value.resendSeconds)

            advanceTimeBy(29_500)
            assertFalse(viewModel.uiState.value.canResend)

            advanceTimeBy(1_000)
            assertTrue(viewModel.uiState.value.canResend)
        }

    @Test
    fun `resend is ignored during the countdown`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            runCurrent()

            viewModel.onResend()
            runCurrent()

            assertEquals(0, repository.forgotPasswordCount)
            assertFalse(viewModel.uiState.value.canResend)
        }

    @Test
    fun `the same code is not submitted again`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = ApiError.BadRequest(INVALID_CODE)
            val viewModel = viewModel()
            viewModel.onCodeChange("123456")
            runCurrent()

            viewModel.onCodeChange("1234567")
            runCurrent()

            assertEquals(1, repository.verifyCount)
        }

    @Test
    fun `resend sends a new code and restarts the countdown`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            advanceUntilIdle()

            viewModel.onResend()
            runCurrent()

            assertEquals(1, repository.forgotPasswordCount)
            assertEquals(EMAIL, repository.lastEmail)
            assertEquals(30, viewModel.uiState.value.resendSeconds)
        }

    @Test
    fun `resend failure shows a toast and lets the user retry`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            advanceUntilIdle()
            repository.failure = ApiError.Network

            viewModel.onResend()
            runCurrent()

            val state = viewModel.uiState.value
            assertEquals(UiText.Resource(R.string.error_network), state.toastMessage)
            assertTrue(state.canResend)
        }

    @Test
    fun `typing the last digit verifies the digits only code once`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()

            viewModel.onCodeChange("12a3")
            assertEquals("123", viewModel.uiState.value.code)
            viewModel.onCodeChange("1234567")
            runCurrent()

            assertEquals("123456", viewModel.uiState.value.code)
            assertEquals(1, repository.verifyCount)
            assertEquals("123456", repository.lastCode)
            assertEquals("123456", viewModel.uiState.value.verifiedCode)

            viewModel.onVerifiedHandled()
            assertNull(viewModel.uiState.value.verifiedCode)
        }

    @Test
    fun `non ascii digits are ignored`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()

            viewModel.onCodeChange("\u0661\u0662\u0663456")

            assertEquals("456", viewModel.uiState.value.code)
        }

    @Test
    fun `confirming an incomplete code shows the length error`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            assertNull(viewModel.uiState.value.codeError)

            viewModel.onSubmit()
            assertEquals(UiText.Resource(R.string.validation_code_required), viewModel.uiState.value.codeError)

            viewModel.onCodeChange("123")
            assertEquals(UiText.Resource(R.string.validation_code_invalid, listOf(6)), viewModel.uiState.value.codeError)
            assertEquals(0, repository.verifyCount)
        }

    @Test
    fun `invalid code shows a toast and keeps the screen`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = ApiError.BadRequest(INVALID_CODE)
            val viewModel = viewModel()

            viewModel.onCodeChange("123456")
            runCurrent()

            val state = viewModel.uiState.value
            assertEquals(UiText.Raw(INVALID_CODE), state.toastMessage)
            assertNull(state.verifiedCode)
            assertFalse(state.isLoading)
        }

    @Test
    fun `double confirm sends a single verify request`() =
        runTest(mainDispatcherRule.dispatcher) {
            val gate = CompletableDeferred<Unit>()
            repository.gate = gate
            val viewModel = viewModel()
            viewModel.onCodeChange("123456")

            viewModel.onSubmit()
            gate.complete(Unit)
            runCurrent()

            assertEquals(1, repository.verifyCount)
        }

    private companion object {
        const val EMAIL = "player@gametrackr.com"
        const val INVALID_CODE = "Invalid code"
    }
}
