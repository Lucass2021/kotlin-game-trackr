package com.lucasdias.gametrackr.feature.app.community.communitydetail

import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.data.community.FakeCommunityRepository
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CommunityDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val community = CommunityMockData.detailCommunity
    private val manyPosts =
        List(CommunityDetailUiState.PAGE_SIZE + 5) { CommunityMockData.communityPosts.first().copy(id = 1_000L + it) }

    @Test
    fun `load shows the posts and the members`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = CommunityDetailViewModel(FakeCommunityRepository(), community)

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(CommunityMockData.communityPosts.map { it.id }, state.posts.map { it.id })
            assertEquals(CommunityMockData.members, state.members)
            assertFalse(state.isLoading)
        }

    @Test
    fun `the empty state waits for the first page`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = CommunityDetailViewModel(FakeCommunityRepository(posts = emptyList()), community)

            viewModel.loadIfNeeded()
            runCurrent()

            assertFalse(viewModel.uiState.value.isEmpty)

            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isEmpty)
        }

    @Test
    fun `load failure shows the error state instead of loading forever`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = CommunityDetailViewModel(FakeCommunityRepository(failure = IllegalStateException()), community)

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.hasError)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `a retry after a load failure reloads posts and members`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeCommunityRepository(failure = IllegalStateException())
            val viewModel = CommunityDetailViewModel(repository, community)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.failure = null
            viewModel.load()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.hasError)
            assertFalse(state.membersFailed)
            assertTrue(state.posts.isNotEmpty())
            assertEquals(CommunityMockData.members, state.members)
            assertEquals(2, repository.fetchMembersCount)
        }

    @Test
    fun `a load more failure shows the retry row and pauses infinite scroll`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeCommunityRepository(posts = manyPosts)
            val viewModel = CommunityDetailViewModel(repository, community)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.failure = IllegalStateException()
            viewModel.loadMore()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.loadMoreFailed)
            assertFalse(viewModel.uiState.value.canLoadMore)
            assertEquals(CommunityDetailUiState.PAGE_SIZE, viewModel.uiState.value.posts.size)

            repository.failure = null
            viewModel.loadMore()
            advanceUntilIdle()

            assertEquals(manyPosts.size, viewModel.uiState.value.posts.size)
        }

    @Test
    fun `a members failure shows the members error and retry loads them`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeCommunityRepository(failure = IllegalStateException())
            val viewModel = CommunityDetailViewModel(repository, community)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.membersFailed)

            repository.failure = null
            viewModel.retryMembers()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.membersFailed)
            assertEquals(CommunityMockData.members, viewModel.uiState.value.members)
        }

    @Test
    fun `refresh reloads the first page and keeps the posts while loading`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeCommunityRepository()
            val viewModel = CommunityDetailViewModel(repository, community)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.refresh()
            runCurrent()

            assertTrue(viewModel.uiState.value.isRefreshing)
            assertTrue(
                viewModel.uiState.value.posts
                    .isNotEmpty(),
            )

            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isRefreshing)
            assertEquals(2, repository.fetchPostsCount)
        }

    @Test
    fun `returning to the screen keeps the posts without fetching again`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeCommunityRepository()
            val viewModel = CommunityDetailViewModel(repository, community)
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(1, repository.fetchPostsCount)
            assertEquals(1, repository.fetchMembersCount)
        }
}
