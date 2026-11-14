package com.lucasdias.gametrackr.feature.auth

import com.lucasdias.gametrackr.core.auth.AuthStatus
import com.lucasdias.gametrackr.core.auth.SessionManager
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.auth.FakeAuthRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AuthViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeAuthRepository()

    @Test
    fun `button and auto redirect complete the password reset once`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = AuthViewModel(repository, SessionManager())
            advanceUntilIdle()
            val gate = CompletableDeferred<Unit>()
            repository.gate = gate

            viewModel.completePasswordReset(onFailure = {})
            viewModel.completePasswordReset(onFailure = {})
            gate.complete(Unit)
            advanceUntilIdle()

            assertEquals(1, repository.completeResetCount)
        }

    @Test
    fun `a failed completion can be retried and reports the failure`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = AuthViewModel(repository, SessionManager())
            advanceUntilIdle()
            repository.failure = ApiError.Unauthorized
            var failures = 0

            viewModel.completePasswordReset(onFailure = { failures += 1 })
            advanceUntilIdle()
            viewModel.completePasswordReset(onFailure = { failures += 1 })
            advanceUntilIdle()

            assertEquals(2, repository.completeResetCount)
            assertEquals(2, failures)
        }

    @Test
    fun `a session check that fails without a 401 keeps the session`() =
        runTest(mainDispatcherRule.dispatcher) {
            val sessionManager = SessionManager()
            repository.hasStoredToken = true
            repository.validateFailure = ApiError.Server(HTTP_SERVER_ERROR)

            AuthViewModel(repository, sessionManager)
            advanceUntilIdle()

            assertEquals(AuthStatus.Authenticated(null), sessionManager.status.value)
        }

    @Test
    fun `a session check rejected with a 401 ends the session`() =
        runTest(mainDispatcherRule.dispatcher) {
            val sessionManager = SessionManager()
            repository.hasStoredToken = true
            repository.validateFailure = ApiError.Forbidden(REJECTED_MESSAGE)

            AuthViewModel(repository, sessionManager)
            advanceUntilIdle()

            assertEquals(AuthStatus.Unauthenticated, sessionManager.status.value)
        }

    @Test
    fun `a session check answered with a plain 401 ends the session`() =
        runTest(mainDispatcherRule.dispatcher) {
            val sessionManager = SessionManager()
            repository.hasStoredToken = true
            repository.validateFailure = ApiError.Unauthorized

            AuthViewModel(repository, sessionManager)
            advanceUntilIdle()

            assertEquals(AuthStatus.Unauthenticated, sessionManager.status.value)
        }

    private companion object {
        const val HTTP_SERVER_ERROR = 500
        const val REJECTED_MESSAGE = "The token has been blacklisted"
    }
}
