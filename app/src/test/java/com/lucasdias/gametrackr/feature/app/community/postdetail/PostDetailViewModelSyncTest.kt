package com.lucasdias.gametrackr.feature.app.community.postdetail

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.data.community.CommunityUpdates
import com.lucasdias.gametrackr.data.community.FakeCommunityFeedRepository
import com.lucasdias.gametrackr.data.community.FakeCommunityRepository
import com.lucasdias.gametrackr.data.community.PostUpdates
import com.lucasdias.gametrackr.feature.app.community.CommunityFeedViewModel
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PostDetailViewModelSyncTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val post = CommunityMockData.communityPosts.first()
    private val repository = FakeCommunityRepository()
    private val feedRepository = FakeCommunityFeedRepository(repository)
    private val postUpdates = PostUpdates()
    private val feed by lazy { CommunityFeedViewModel(feedRepository, repository, postUpdates, CommunityUpdates()) }
    private val viewModel by lazy { PostDetailViewModel(repository, feedRepository, postUpdates, post) }

    private fun TestScope.feedLoaded() {
        feed.loadIfNeeded()
        advanceUntilIdle()
    }

    private fun feedPost() =
        feed.uiState.value.posts
            .first { it.id == post.id }

    @Test
    fun `a like in the post detail updates the feed`() =
        runTest(mainDispatcherRule.dispatcher) {
            feedLoaded()

            viewModel.onLike()
            advanceUntilIdle()

            assertEquals(!post.isLiked, feedPost().isLiked)
            assertEquals(viewModel.uiState.value.post.likes, feedPost().likes)
        }

    @Test
    fun `a bookmark in the post detail is saved and updates the feed`() =
        runTest(mainDispatcherRule.dispatcher) {
            feedLoaded()

            viewModel.onBookmark()
            advanceUntilIdle()

            assertTrue(feedRepository.isBookmarked(post.id))
            assertTrue(feedPost().isBookmarked)
        }

    @Test
    fun `a bookmark failure in the post detail rolls back and shows an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            feedLoaded()

            feedRepository.failure = IllegalStateException()
            viewModel.onBookmark()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.post.isBookmarked)
            assertFalse(feedPost().isBookmarked)
            assertEquals(R.string.community_bookmark_error, viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `tapping the community opens the post's own community`() =
        runTest(mainDispatcherRule.dispatcher) {
            val community = CommunityMockData.all.last()
            val viewModel = PostDetailViewModel(repository, feedRepository, postUpdates, post.copy(communityId = community.id))

            viewModel.onCommunityTap()
            advanceUntilIdle()

            assertEquals(
                community.id,
                viewModel.uiState.value.communityToOpen
                    ?.id,
            )

            viewModel.onCommunityOpened()

            assertNull(viewModel.uiState.value.communityToOpen)
        }

    @Test
    fun `a community that cannot be loaded shows an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = PostDetailViewModel(repository, feedRepository, postUpdates, post.copy(communityId = 9_999L))

            viewModel.onCommunityTap()
            advanceUntilIdle()

            assertNull(viewModel.uiState.value.communityToOpen)
            assertEquals(R.string.post_detail_community_error, viewModel.uiState.value.toastMessage)
        }
}
