package com.lucasdias.gametrackr.feature.app.community.communitydetail

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.data.community.CommunityUpdates
import com.lucasdias.gametrackr.data.community.FakeCommunityFeedRepository
import com.lucasdias.gametrackr.data.community.FakeCommunityRepository
import com.lucasdias.gametrackr.data.community.PostUpdates
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

class CommunityDetailViewModelActionsTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val community = CommunityMockData.detailCommunity
    private val post = CommunityMockData.communityPosts.first()
    private val repository = FakeCommunityRepository(communities = listOf(community))
    private val feedRepository = FakeCommunityFeedRepository(repository)
    private val postUpdates = PostUpdates()
    private val viewModel by lazy { CommunityDetailViewModel(repository, feedRepository, postUpdates, CommunityUpdates(), community) }

    private fun TestScope.loaded() {
        viewModel.loadIfNeeded()
        advanceUntilIdle()
    }

    @Test
    fun `joining saves it in the repository`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            viewModel.onJoinTap()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isJoined)
            assertTrue(repository.isJoined(community.id))
        }

    @Test
    fun `a join failure rolls back and shows an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            repository.failure = IllegalStateException()
            viewModel.onJoinTap()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isJoined)
            assertEquals(R.string.community_join_error, viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `leaving asks for confirmation and saves only after confirming`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            viewModel.onJoinTap()
            advanceUntilIdle()

            viewModel.onJoinTap()

            assertTrue(viewModel.uiState.value.showLeaveConfirmation)
            assertTrue(viewModel.uiState.value.isJoined)

            viewModel.onLeaveConfirmed()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.showLeaveConfirmation)
            assertFalse(viewModel.uiState.value.isJoined)
            assertFalse(repository.isJoined(community.id))
        }

    @Test
    fun `a like is shown right away and synced with the repository`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            viewModel.onLike(post.id)

            assertTrue(likedPost().isLiked)
            assertEquals(post.likes + 1, likedPost().likes)

            advanceUntilIdle()

            assertEquals(repository.post(post.id)?.likes, likedPost().likes)
        }

    @Test
    fun `a like failure restores the previous like and shows an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            repository.failure = IllegalStateException()
            viewModel.onLike(post.id)
            advanceUntilIdle()

            assertFalse(likedPost().isLiked)
            assertEquals(post.likes, likedPost().likes)
            assertEquals(R.string.community_like_error, viewModel.uiState.value.toastMessage)

            viewModel.onToastShown()

            assertNull(viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `a second tap on a pending like is ignored`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            viewModel.onLike(post.id)
            runCurrent()
            viewModel.onLike(post.id)
            advanceUntilIdle()

            assertEquals(1, repository.likeCount)
            assertTrue(likedPost().isLiked)
        }

    @Test
    fun `deleting reports the deleted community`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            viewModel.onDeleteRequest()
            assertTrue(viewModel.uiState.value.showDeleteConfirmation)
            viewModel.onDeleteConfirmed()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isDeleted)
            assertEquals(1, repository.deleteCount)
        }

    @Test
    fun `a forbidden delete shows the owner message`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            repository.failure = ApiError.Forbidden("Forbidden")
            viewModel.onDeleteConfirmed()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isDeleted)
            assertEquals(R.string.community_delete_forbidden, viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `a bookmark is saved in the repository and a failure rolls back`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            viewModel.onBookmark(post.id)
            advanceUntilIdle()

            assertTrue(likedPost().isBookmarked)
            assertTrue(feedRepository.isBookmarked(post.id))

            feedRepository.failure = IllegalStateException()
            viewModel.onBookmark(post.id)
            advanceUntilIdle()

            assertTrue(likedPost().isBookmarked)
            assertEquals(R.string.community_bookmark_error, viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `a post changed in another screen is updated in the community posts`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            postUpdates.publish(post.copy(isLiked = !post.isLiked, likes = post.likes + 1))
            advanceUntilIdle()

            assertEquals(!post.isLiked, likedPost().isLiked)
            assertEquals(post.likes + 1, likedPost().likes)
        }

    private fun likedPost() =
        viewModel.uiState.value.posts
            .first { it.id == post.id }
}
