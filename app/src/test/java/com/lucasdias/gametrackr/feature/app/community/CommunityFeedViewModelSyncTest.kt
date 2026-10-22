package com.lucasdias.gametrackr.feature.app.community

import com.lucasdias.gametrackr.core.model.CommunityFeedFilter
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.data.community.CommunityUpdates
import com.lucasdias.gametrackr.data.community.FakeCommunityFeedRepository
import com.lucasdias.gametrackr.data.community.FakeCommunityRepository
import com.lucasdias.gametrackr.data.community.FakeDiscoverCommunitiesRepository
import com.lucasdias.gametrackr.data.community.PostUpdates
import com.lucasdias.gametrackr.feature.app.community.discover.DiscoverCommunitiesViewModel
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CommunityFeedViewModelSyncTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val manyPosts =
        List(CommunityFeedUiState.PAGE_SIZE + 5) { CommunityMockData.communityPosts.first().copy(id = 1_000L + it) }
    private val repository = FakeCommunityRepository()
    private val communityUpdates = CommunityUpdates()
    private val feed by lazy {
        CommunityFeedViewModel(FakeCommunityFeedRepository(repository), repository, PostUpdates(), communityUpdates)
    }
    private val discover by lazy { DiscoverCommunitiesViewModel(FakeDiscoverCommunitiesRepository(repository), communityUpdates) }

    private fun TestScope.loaded() {
        feed.loadIfNeeded()
        discover.loadIfNeeded()
        advanceUntilIdle()
    }

    private fun suggestion() = checkNotNull(feed.uiState.value.suggestedCommunity)

    @Test
    fun `joining in discover updates the suggested community`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            val community =
                discover.uiState.value.communities
                    .first { it.id == suggestion().id }

            discover.onJoinTap(community)
            advanceUntilIdle()

            assertTrue(suggestion().isJoined)
        }

    @Test
    fun `joining the suggested community updates discover`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            val id = suggestion().id

            feed.onJoinSuggested()
            advanceUntilIdle()

            assertTrue(
                discover.uiState.value.communities
                    .first { it.id == id }
                    .isJoined,
            )
        }

    @Test
    fun `tapping a suggestion already joined sends no request`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            feed.onJoinSuggested()
            advanceUntilIdle()

            repository.failure = IllegalStateException()
            feed.onJoinSuggested()
            advanceUntilIdle()

            assertTrue(suggestion().isJoined)
            assertNull(feed.uiState.value.toastMessage)
        }

    @Test
    fun `a load more during a refresh does not cancel the refresh`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeCommunityRepository(posts = manyPosts)
            val feed = CommunityFeedViewModel(FakeCommunityFeedRepository(repository), repository, PostUpdates(), communityUpdates)
            feed.loadIfNeeded()
            advanceUntilIdle()

            feed.refresh()
            runCurrent()
            feed.loadMore()
            advanceUntilIdle()

            val state = feed.uiState.value
            assertFalse(state.isRefreshing)
            assertEquals(1, state.currentPage)
            assertEquals(CommunityFeedUiState.PAGE_SIZE, state.posts.size)
        }

    @Test
    fun `changing the filter while a page loads shows only the new filter`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            feed.onFilterChange(CommunityFeedFilter.TOP_RATED)
            runCurrent()
            feed.onFilterChange(CommunityFeedFilter.FOLLOWING)
            advanceUntilIdle()

            assertEquals(
                CommunityMockData.feed.map { it.id },
                feed.uiState.value.posts
                    .map { it.id },
            )
        }
}
