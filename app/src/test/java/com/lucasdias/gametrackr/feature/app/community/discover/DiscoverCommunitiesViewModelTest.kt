package com.lucasdias.gametrackr.feature.app.community.discover

import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.FakeLatency
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.data.community.CommunityUpdates
import com.lucasdias.gametrackr.data.community.DiscoverMockData
import com.lucasdias.gametrackr.data.community.FakeCommunityRepository
import com.lucasdias.gametrackr.data.community.FakeDiscoverCommunitiesRepository
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

class DiscoverCommunitiesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val manyCommunities =
        List(DiscoverCommunitiesUiState.PAGE_SIZE + 5) { CommunityMockData.all.first().copy(id = 500L + it) }
    private val remote = FakeCommunityRepository()
    private val repository = FakeDiscoverCommunitiesRepository(remote)
    private val viewModel by lazy { DiscoverCommunitiesViewModel(repository, CommunityUpdates()) }

    @Test
    fun `load shows the real communities`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(
                CommunityMockData.all.map { it.id },
                viewModel.uiState.value.communities
                    .map { it.id },
            )
        }

    @Test
    fun `the empty state waits for the first page`() =
        runTest(mainDispatcherRule.dispatcher) {
            val remote = FakeCommunityRepository(communities = emptyList())
            val viewModel = DiscoverCommunitiesViewModel(FakeDiscoverCommunitiesRepository(remote), CommunityUpdates())

            viewModel.loadIfNeeded()
            runCurrent()

            assertFalse(viewModel.uiState.value.isEmpty)

            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isEmpty)
        }

    @Test
    fun `coming back to discover does not reload it`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(1, repository.fetchCount)
        }

    @Test
    fun `a load failure shows the error state and retry loads the communities`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = IllegalStateException()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.hasError)

            repository.failure = null
            viewModel.load()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.hasError)
            assertTrue(
                viewModel.uiState.value.communities
                    .isNotEmpty(),
            )
        }

    @Test
    fun `a load more failure shows the retry row and pauses infinite scroll`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeDiscoverCommunitiesRepository(FakeCommunityRepository(communities = manyCommunities))
            val viewModel = DiscoverCommunitiesViewModel(repository, CommunityUpdates())
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.failure = IllegalStateException()
            viewModel.loadMore()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.loadMoreFailed)
            assertFalse(viewModel.uiState.value.canLoadMore)

            repository.failure = null
            viewModel.loadMore()
            advanceUntilIdle()

            assertEquals(manyCommunities.size, viewModel.uiState.value.communities.size)
        }

    @Test
    fun `typing a search asks the repository once after the pause`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.onQueryChange("r")
            viewModel.onQueryChange("rp")
            viewModel.onQueryChange("rpg")
            advanceUntilIdle()

            assertEquals(2, repository.fetchCount)
            assertEquals("rpg", repository.lastSearch)
        }

    @Test
    fun `a category shows the communities of that category`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.onCategoryChange("RPG")
            advanceUntilIdle()

            val expected = DiscoverMockData.categoryCommunities.filter { it.category == "RPG" }.map { it.id }
            assertEquals(
                expected,
                viewModel.uiState.value.communities
                    .map { it.id },
            )
        }

    @Test
    fun `a load more while a search is pending keeps the loaded search`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeDiscoverCommunitiesRepository(FakeCommunityRepository(communities = manyCommunities))
            val viewModel = DiscoverCommunitiesViewModel(repository, CommunityUpdates())
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.onQueryChange("rpg")
            viewModel.loadMore()
            runCurrent()

            assertNull(repository.lastSearch)
        }

    @Test
    fun `an older page does not replace a newer category`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceTimeBy(FakeLatency.RESPONSE_DELAY_MS / 2)

            viewModel.onCategoryChange("RPG")
            advanceTimeBy(FakeLatency.RESPONSE_DELAY_MS * 3 / 4)

            assertTrue(
                viewModel.uiState.value.communities
                    .isEmpty(),
            )
            assertTrue(viewModel.uiState.value.isLoading)

            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.communities
                    .all { it.category == "RPG" },
            )
        }
}
