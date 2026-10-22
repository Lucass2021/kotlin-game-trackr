package com.lucasdias.gametrackr.feature.app.community

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.CommunityFeedFilter
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.data.community.CommunityUpdates
import com.lucasdias.gametrackr.data.community.FakeCommunityFeedRepository
import com.lucasdias.gametrackr.data.community.FakeCommunityRepository
import com.lucasdias.gametrackr.data.community.PostUpdates
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CommunityFeedViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val posts = CommunityMockData.communityPosts
    private val manyPosts = List(CommunityFeedUiState.PAGE_SIZE + 5) { posts.first().copy(id = 1_000L + it) }

    private fun viewModel(repository: FakeCommunityRepository = FakeCommunityRepository()): CommunityFeedViewModel =
        CommunityFeedViewModel(FakeCommunityFeedRepository(repository), repository, PostUpdates(), CommunityUpdates())

    @Test
    fun `load shows the latest posts and the first community not joined`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(posts.map { it.id }, state.posts.map { it.id })
            assertEquals(CommunityMockData.all.first { !it.isJoined }.id, state.suggestedCommunity?.id)
            assertFalse(state.isLoading)
        }

    @Test
    fun `coming back to the feed does not reload it`() =
        runTest(mainDispatcherRule.dispatcher) {
            val feedRepository = FakeCommunityFeedRepository(FakeCommunityRepository())
            val viewModel = CommunityFeedViewModel(feedRepository, FakeCommunityRepository(), PostUpdates(), CommunityUpdates())

            viewModel.loadIfNeeded()
            advanceUntilIdle()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(1, feedRepository.fetchFeedCount)
        }

    @Test
    fun `refresh keeps the posts on screen until the new page arrives`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.refresh()
            runCurrent()

            val state = viewModel.uiState.value
            assertTrue(state.isRefreshing)
            assertEquals(posts.size, state.posts.size)
            assertFalse(state.isEmpty)
        }

    @Test
    fun `a refresh failure keeps the posts and shows a toast`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeCommunityRepository()
            val viewModel = viewModel(repository)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.failure = IllegalStateException()
            viewModel.refresh()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(posts.size, state.posts.size)
            assertFalse(state.hasError)
            assertEquals(R.string.community_refresh_error, state.toastMessage)
        }

    @Test
    fun `a load failure shows the error state and retry loads the posts`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeCommunityRepository(failure = IllegalStateException())
            val viewModel = viewModel(repository)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.hasError)

            repository.failure = null
            viewModel.load()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.hasError)
            assertEquals(posts.size, viewModel.uiState.value.posts.size)
        }

    @Test
    fun `a load more failure shows the retry row and pauses infinite scroll`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeCommunityRepository(posts = manyPosts)
            val viewModel = viewModel(repository)
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

            assertEquals(manyPosts.size, viewModel.uiState.value.posts.size)
        }

    @Test
    fun `top rated sorts the posts by likes and following shows the followed posts`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.onFilterChange(CommunityFeedFilter.TOP_RATED)
            advanceUntilIdle()

            assertEquals(
                posts.sortedByDescending { it.likes }.map { it.id },
                viewModel.uiState.value.posts
                    .map { it.id },
            )

            viewModel.onFilterChange(CommunityFeedFilter.FOLLOWING)
            advanceUntilIdle()

            assertEquals(CommunityFeedFilter.FOLLOWING, viewModel.uiState.value.filter)
            assertEquals(
                CommunityMockData.feed.map { it.id },
                viewModel.uiState.value.posts
                    .map { it.id },
            )
        }
}
