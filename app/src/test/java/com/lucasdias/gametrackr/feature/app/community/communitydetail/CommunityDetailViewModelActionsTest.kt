package com.lucasdias.gametrackr.feature.app.community.communitydetail

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.data.community.FakeCommunityRepository
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
    private val viewModel = CommunityDetailViewModel(repository, community)

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
    fun `a bookmark toggles only on screen`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            viewModel.onBookmark(post.id)

            assertTrue(likedPost().isBookmarked)
        }

    private fun likedPost() =
        viewModel.uiState.value.posts
            .first { it.id == post.id }
}
