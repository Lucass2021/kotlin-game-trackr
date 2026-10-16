package com.lucasdias.gametrackr.feature.app.profile.setup

import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.profile.FakeSetupRepository
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MySetupViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `load shows the setups from the repository`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = MySetupViewModel(FakeSetupRepository())

            viewModel.load()
            advanceUntilIdle()

            assertEquals(ProfileMockData.setups, viewModel.uiState.value.setups)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `load failure shows the error state`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = MySetupViewModel(FakeSetupRepository(failure = IllegalStateException()))

            viewModel.load()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.hasError)
            assertTrue(
                viewModel.uiState.value.setups
                    .isEmpty(),
            )
        }

    @Test
    fun `switching to a guest drops the list and a late load result does not bring it back`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = MySetupViewModel(FakeSetupRepository())

            viewModel.loadFor(USER_ID)
            runCurrent()
            viewModel.loadFor(null)
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.setups
                    .isEmpty(),
            )
            assertFalse(viewModel.uiState.value.isLoading)
            assertFalse(viewModel.uiState.value.hasError)
        }

    @Test
    fun `a new request for the same user keeps the list without reloading`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeSetupRepository()
            val viewModel = MySetupViewModel(repository)
            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            repository.failure = IllegalStateException()
            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            assertEquals(ProfileMockData.setups, viewModel.uiState.value.setups)
            assertFalse(viewModel.uiState.value.isLoading)
            assertFalse(viewModel.uiState.value.hasError)
        }

    @Test
    fun `a second load replaces the one in flight without showing an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeSetupRepository(failure = IllegalStateException())
            val viewModel = MySetupViewModel(repository)

            viewModel.load()
            runCurrent()
            repository.failure = null
            viewModel.load()
            advanceUntilIdle()

            assertEquals(ProfileMockData.setups, viewModel.uiState.value.setups)
            assertFalse(viewModel.uiState.value.hasError)
        }

    @Test
    fun `saved setups are replaced or appended and deleted setups are removed`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = MySetupViewModel(FakeSetupRepository())
            viewModel.load()
            advanceUntilIdle()
            val edited = ProfileMockData.setups[0].copy(title = EDITED_TITLE)
            val created = ProfileMockData.setups[0].copy(id = NEW_SETUP_ID)

            viewModel.onSetupSaved(edited)
            viewModel.onSetupSaved(created)
            viewModel.onSetupDeleted(ProfileMockData.setups[1].id)

            val ids =
                viewModel.uiState.value.setups
                    .map { it.id }
            assertEquals(listOf(ProfileMockData.setups[0].id, ProfileMockData.setups[2].id, NEW_SETUP_ID), ids)
            assertEquals(
                EDITED_TITLE,
                viewModel.uiState.value.setups
                    .first()
                    .title,
            )
        }

    private companion object {
        const val EDITED_TITLE = "Edited"
        const val NEW_SETUP_ID = 99
        const val USER_ID = 7
    }
}
