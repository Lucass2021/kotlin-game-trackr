package com.lucasdias.gametrackr.feature.auth.login

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

class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeAuthRepository()
    private val googleAuth = FakeGoogleAuth()

    private fun viewModel(
        email: String = EMAIL,
        password: String = PASSWORD,
    ) = LoginViewModel(repository, googleAuth).apply {
        onEmailChange(email)
        onPasswordChange(password)
    }

    private fun TestScope.collect(viewModel: LoginViewModel) {
        backgroundScope.launch { viewModel.uiState.collect {} }
    }

    @Test
    fun `errors show only after the first submit`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel(email = "", password = "")
            collect(viewModel)
            advanceUntilIdle()
            assertNull(viewModel.uiState.value.emailError)

            viewModel.onSubmit()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(UiText.Resource(R.string.validation_email_required), state.emailError)
            assertEquals(UiText.Resource(R.string.validation_password_required), state.passwordError)
            assertEquals(0, repository.loginCount)
        }

    @Test
    fun `invalid email and short password are rejected`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel(email = "player", password = "abc")
            collect(viewModel)

            viewModel.onSubmit()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(UiText.Resource(R.string.validation_email_invalid), state.emailError)
            assertEquals(UiText.Resource(R.string.validation_password_too_short, listOf(6)), state.passwordError)
            assertEquals(0, repository.loginCount)
        }

    @Test
    fun `fixing a field after submit clears its error`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel(password = "")
            collect(viewModel)
            viewModel.onSubmit()
            advanceUntilIdle()

            viewModel.onPasswordChange(PASSWORD)
            advanceUntilIdle()

            assertNull(viewModel.uiState.value.passwordError)
        }

    @Test
    fun `submit sends the trimmed email and the password`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel(email = "  $EMAIL  ")
            collect(viewModel)

            viewModel.onSubmit()
            advanceUntilIdle()

            assertEquals(EMAIL, repository.lastEmail)
            assertEquals(PASSWORD, repository.lastPassword)
            assertFalse(viewModel.uiState.value.isLoading)
            assertNull(viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `login failure shows a toast and keeps the fields`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = ApiError.Unauthorized
            val viewModel = viewModel()
            collect(viewModel)

            viewModel.onSubmit()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(UiText.Resource(R.string.error_invalid_credentials), state.toastMessage)
            assertEquals(EMAIL, state.email)
            assertEquals(PASSWORD, state.password)
            assertFalse(state.isLoading)

            viewModel.onToastShown()
            advanceUntilIdle()
            assertNull(viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `double submit sends a single login request`() =
        runTest(mainDispatcherRule.dispatcher) {
            val gate = CompletableDeferred<Unit>()
            repository.gate = gate
            val viewModel = viewModel()
            collect(viewModel)

            viewModel.onSubmit()
            viewModel.onSubmit()
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.isLoading)

            gate.complete(Unit)
            advanceUntilIdle()

            assertEquals(1, repository.loginCount)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `google is ignored while a login is in flight and login is ignored while google completes`() =
        runTest(mainDispatcherRule.dispatcher) {
            val gate = CompletableDeferred<Unit>()
            repository.gate = gate
            val viewModel = viewModel()
            collect(viewModel)

            viewModel.onSubmit()
            viewModel.onSignInWithGoogle()
            assertEquals(0, googleAuth.launchCount)

            gate.complete(Unit)
            advanceUntilIdle()
            googleAuth.state.value = GoogleAuthState.COMPLETING
            viewModel.onSubmit()
            advanceUntilIdle()

            assertEquals(1, repository.loginCount)
        }

    @Test
    fun `sign in with google launches and shows its states`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            collect(viewModel)

            viewModel.onSignInWithGoogle()
            viewModel.onSignInWithGoogle()
            assertEquals(1, googleAuth.launchCount)

            googleAuth.state.value = GoogleAuthState.COMPLETING
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.isLoading)

            googleAuth.state.value = GoogleAuthState.FAILED
            advanceUntilIdle()
            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(UiText.Resource(R.string.error_generic), viewModel.uiState.value.toastMessage)

            viewModel.onToastShown()
            advanceUntilIdle()
            assertEquals(GoogleAuthState.IDLE, googleAuth.state.value)
            assertNull(viewModel.uiState.value.toastMessage)
        }

    private companion object {
        const val EMAIL = "player@gametrackr.com"
        const val PASSWORD = "secret123"
    }
}
