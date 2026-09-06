package com.lucasdias.gametrackr.feature.auth.register

import android.content.Context
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.auth.GoogleAuth
import com.lucasdias.gametrackr.support.FakeAuthRepository
import com.lucasdias.gametrackr.support.MainDispatcherRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class RegisterViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val context = mockk<Context>(relaxed = true)

    private fun viewModel(repository: FakeAuthRepository = FakeAuthRepository()) =
        RegisterViewModel(
            authRepository = repository,
            googleAuth = GoogleAuth(repository),
            context = context,
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun TestScope.keepStateHot(viewModel: RegisterViewModel) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect { } }
    }

    @Test
    fun `hides every field error before the first submit`() =
        runTest {
            val viewModel = viewModel()
            keepStateHot(viewModel)

            viewModel.onNameChange("L")
            viewModel.onEmailChange("nope")

            with(viewModel.uiState.value) {
                assertNull(nameError)
                assertNull(emailError)
                assertNull(passwordError)
                assertNull(confirmPasswordError)
                assertNull(termsError)
            }
        }

    @Test
    fun `shows every field error once an empty form is submitted`() =
        runTest {
            val viewModel = viewModel()
            keepStateHot(viewModel)

            viewModel.onSubmit()

            with(viewModel.uiState.value) {
                assertEquals(R.string.validation_name_required, nameError)
                assertEquals(R.string.validation_email_required, emailError)
                assertEquals(R.string.validation_password_required, passwordError)
                assertEquals(R.string.validation_terms_required, termsError)
            }
        }

    @Test
    fun `rejects a name that is only whitespace or too short`() =
        runTest {
            val viewModel = viewModel()
            keepStateHot(viewModel)

            viewModel.onNameChange("   ")
            viewModel.onSubmit()
            assertEquals(R.string.validation_name_required, viewModel.uiState.value.nameError)

            viewModel.onNameChange("Lu")
            assertEquals(R.string.validation_name_too_short, viewModel.uiState.value.nameError)

            viewModel.onNameChange("Lucas")
            assertNull(viewModel.uiState.value.nameError)
        }

    @Test
    fun `requires both an at sign and a dot in the email`() =
        runTest {
            val viewModel = viewModel()
            keepStateHot(viewModel)

            viewModel.onEmailChange("lucas@example")
            viewModel.onSubmit()
            assertEquals(R.string.validation_email_invalid, viewModel.uiState.value.emailError)

            viewModel.onEmailChange("lucas.example.com")
            assertEquals(R.string.validation_email_invalid, viewModel.uiState.value.emailError)

            viewModel.onEmailChange("lucas@example.com")
            assertNull(viewModel.uiState.value.emailError)
        }

    @Test
    fun `requires six characters and a matching confirmation`() =
        runTest {
            val viewModel = viewModel()
            keepStateHot(viewModel)

            viewModel.onPasswordChange("12345")
            viewModel.onConfirmPasswordChange("54321")
            viewModel.onSubmit()

            assertEquals(R.string.validation_password_too_short, viewModel.uiState.value.passwordError)
            assertEquals(
                R.string.validation_passwords_dont_match,
                viewModel.uiState.value.confirmPasswordError,
            )

            viewModel.onPasswordChange("123456")
            viewModel.onConfirmPasswordChange("123456")
            assertNull(viewModel.uiState.value.passwordError)
            assertNull(viewModel.uiState.value.confirmPasswordError)
        }

    @Test
    fun `does not call the api while the form is invalid`() =
        runTest {
            val repository = FakeAuthRepository()
            val viewModel = viewModel(repository)
            keepStateHot(viewModel)

            viewModel.onNameChange("Lucas")
            viewModel.onSubmit()

            assertEquals(0, repository.registerCount)
        }

    @Test
    fun `registers with the trimmed name once every field is valid`() =
        runTest {
            val repository = FakeAuthRepository()
            val viewModel = viewModel(repository)
            keepStateHot(viewModel)

            viewModel.onNameChange("  Lucas Dias  ")
            viewModel.onEmailChange(" lucas@example.com ")
            viewModel.onPasswordChange("123456")
            viewModel.onConfirmPasswordChange("123456")
            viewModel.onToggleTerms()
            viewModel.onSubmit()

            assertEquals(1, repository.registerCount)
            assertEquals("Lucas Dias", repository.lastRegisteredName)
            assertEquals(true, viewModel.uiState.value.registered)
        }
}
