package com.lucasdias.gametrackr.feature.app.community

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.data.community.CommunityUpdates
import com.lucasdias.gametrackr.data.community.FakeCommunityFeedRepository
import com.lucasdias.gametrackr.data.community.FakeCommunityRepository
import com.lucasdias.gametrackr.data.community.PostUpdates
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CommunityFeedViewModelActionsTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val post = CommunityMockData.communityPosts.first()
    private val repository = FakeCommunityRepository()
    private val feedRepository = FakeCommunityFeedRepository(repository)
    private val postUpdates = PostUpdates()
    private val viewModel by lazy { CommunityFeedViewModel(feedRepository, repository, postUpdates, CommunityUpdates()) }

    private fun TestScope.loaded() {
        viewModel.loadIfNeeded()
        advanceUntilIdle()
    }

    private fun feedPost() =
        viewModel.uiState.value.posts
            .first { it.id == post.id }

    @Test
    fun `a like is saved and shared with the other screens`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            val listener = CommunityFeedViewModel(feedRepository, repository, postUpdates, CommunityUpdates())
            advanceUntilIdle()
            listener.onPostCreated(post)

            viewModel.onLike(post.id)
            advanceUntilIdle()

            assertEquals(!post.isLiked, feedPost().isLiked)
            assertEquals(!post.isLiked, repository.post(post.id)?.isLiked)
            assertEquals(
                !post.isLiked,
                listener.uiState.value.posts
                    .first()
                    .isLiked,
            )
        }

    @Test
    fun `a like failure rolls back and shows an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            repository.failure = IllegalStateException()
            viewModel.onLike(post.id)
            advanceUntilIdle()

            assertEquals(post.isLiked, feedPost().isLiked)
            assertEquals(post.likes, feedPost().likes)
            assertEquals(R.string.community_like_error, viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `a double tap on like sends one request`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            viewModel.onLike(post.id)
            viewModel.onLike(post.id)
            advanceUntilIdle()

            assertEquals(1, repository.likeCount)
            assertEquals(!post.isLiked, feedPost().isLiked)
        }

    @Test
    fun `a bookmark is saved and survives a reload`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            viewModel.onBookmark(post.id)
            advanceUntilIdle()
            viewModel.refresh()
            advanceUntilIdle()

            assertTrue(feedRepository.isBookmarked(post.id))
            assertTrue(feedPost().isBookmarked)
        }

    @Test
    fun `a bookmark failure rolls back and shows an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            feedRepository.failure = IllegalStateException()
            viewModel.onBookmark(post.id)
            advanceUntilIdle()

            assertFalse(feedPost().isBookmarked)
            assertEquals(R.string.community_bookmark_error, viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `a post changed in another screen is updated in the feed`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            postUpdates.publish(post.copy(isLiked = !post.isLiked, likes = post.likes + 1, isBookmarked = true))
            advanceUntilIdle()

            assertEquals(!post.isLiked, feedPost().isLiked)
            assertEquals(post.likes + 1, feedPost().likes)
            assertTrue(feedPost().isBookmarked)
        }

    @Test
    fun `created, deleted and removed community posts update the feed`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            val created = post.copy(id = 9_999L)

            postUpdates.publishCreated(created)
            advanceUntilIdle()
            assertEquals(
                created.id,
                viewModel.uiState.value.posts
                    .first()
                    .id,
            )

            viewModel.onPostDeleted(created.id)
            assertFalse(
                viewModel.uiState.value.posts
                    .any { it.id == created.id },
            )

            val communityId = 77L
            viewModel.onPostCreated(post.copy(id = 9_998L, communityId = communityId))
            viewModel.onCommunityRemoved(communityId)
            assertFalse(
                viewModel.uiState.value.posts
                    .any { it.communityId == communityId },
            )
        }

    @Test
    fun `joining the suggested community saves it and a failure rolls back`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            val suggestion = checkNotNull(viewModel.uiState.value.suggestedCommunity)

            viewModel.onJoinSuggested()
            advanceUntilIdle()

            assertTrue(repository.isJoined(suggestion.id))
            assertTrue(
                viewModel.uiState.value.suggestedCommunity
                    ?.isJoined == true,
            )

            val failing = FakeCommunityRepository()
            val failingViewModel = CommunityFeedViewModel(FakeCommunityFeedRepository(failing), failing, PostUpdates(), CommunityUpdates())
            failingViewModel.loadIfNeeded()
            advanceUntilIdle()
            failing.failure = IllegalStateException()
            failingViewModel.onJoinSuggested()
            advanceUntilIdle()

            assertFalse(
                failingViewModel.uiState.value.suggestedCommunity
                    ?.isJoined == true,
            )
            assertEquals(R.string.community_join_error, failingViewModel.uiState.value.toastMessage)
        }

    @Test
    fun `the toast clears after it is shown`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            feedRepository.failure = IllegalStateException()
            viewModel.onBookmark(post.id)
            advanceUntilIdle()

            viewModel.onToastShown()

            assertNull(viewModel.uiState.value.toastMessage)
        }
}
