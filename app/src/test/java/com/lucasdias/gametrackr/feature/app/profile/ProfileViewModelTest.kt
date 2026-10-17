package com.lucasdias.gametrackr.feature.app.profile

import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.profile.FakeProfileOverviewRepository
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeProfileOverviewRepository()
    private val viewModel = ProfileViewModel(repository)

    @Test
    fun `loading for a user shows the overview with the stats`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            val overview = viewModel.uiState.value.overview
            assertEquals(ProfileMockData.stats, overview?.stats)
            assertEquals(ProfileMockData.breakdown, overview?.breakdown)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `a guest does not load the overview`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadFor(null)
            advanceUntilIdle()

            assertEquals(0, repository.fetchCount)
            assertEquals(ProfileUiState(), viewModel.uiState.value)
        }

    @Test
    fun `load failure shows the error state instead of loading forever`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = IllegalStateException()

            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.hasError)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `retry after a failure shows the overview`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = IllegalStateException()
            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            repository.failure = null
            viewModel.load()
            advanceUntilIdle()

            assertEquals(
                ProfileMockData.stats,
                viewModel.uiState.value.overview
                    ?.stats,
            )
            assertFalse(viewModel.uiState.value.hasError)
        }

    @Test
    fun `coming back to the tab keeps the overview without fetching again`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            assertEquals(1, repository.fetchCount)
            assertEquals(
                ProfileMockData.stats,
                viewModel.uiState.value.overview
                    ?.stats,
            )
        }

    @Test
    fun `switching to another user loads the overview again`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            viewModel.loadFor(OTHER_USER_ID)
            advanceUntilIdle()

            assertEquals(2, repository.fetchCount)
            assertEquals(
                ProfileMockData.stats,
                viewModel.uiState.value.overview
                    ?.stats,
            )
        }

    @Test
    fun `switching to a guest drops the overview and a late result does not bring it back`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadFor(USER_ID)
            runCurrent()
            viewModel.loadFor(null)
            advanceUntilIdle()

            assertNull(viewModel.uiState.value.overview)
            assertEquals(ProfileUiState(), viewModel.uiState.value)
        }

    private companion object {
        const val USER_ID = 1
        const val OTHER_USER_ID = 2
    }
}
