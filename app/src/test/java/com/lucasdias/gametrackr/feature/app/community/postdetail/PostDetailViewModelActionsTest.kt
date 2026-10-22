package com.lucasdias.gametrackr.feature.app.community.postdetail

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.findComment
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.community.CommunityMockData
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

class PostDetailViewModelActionsTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val post = CommunityMockData.communityPosts.first()
    private val thread = CommunityMockData.comments.first()
    private val reply = thread.replies.first()
    private val repository = FakeCommunityRepository()
    private val viewModel = PostDetailViewModel(repository, FakeCommunityFeedRepository(repository), PostUpdates(), post)

    private fun TestScope.loaded() {
        viewModel.loadIfNeeded()
        advanceUntilIdle()
    }

    @Test
    fun `a post like is shown right away and synced with the repository`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.onLike()

            assertEquals(!post.isLiked, viewModel.uiState.value.post.isLiked)

            advanceUntilIdle()

            assertEquals(!post.isLiked, repository.post(post.id)?.isLiked)
            assertEquals(repository.post(post.id)?.likes, viewModel.uiState.value.post.likes)
        }

    @Test
    fun `a post like failure restores the post and shows an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = IllegalStateException()

            viewModel.onLike()
            advanceUntilIdle()

            assertEquals(post.isLiked, viewModel.uiState.value.post.isLiked)
            assertEquals(post.likes, viewModel.uiState.value.post.likes)
            assertEquals(R.string.community_like_error, viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `a second like tap while one is pending is ignored`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.onLike()
            runCurrent()
            viewModel.onLike()
            advanceUntilIdle()

            assertEquals(1, repository.likeCount)
        }

    @Test
    fun `a reply like is saved in the repository`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            viewModel.onCommentLike(reply.id)
            advanceUntilIdle()

            assertEquals(1, repository.commentLikeCount)
            assertEquals(!reply.isLiked, repository.comment(reply.id)?.isLiked)
            assertEquals(
                !reply.isLiked,
                viewModel.uiState.value.comments
                    .findComment(reply.id)
                    ?.isLiked,
            )
        }

    @Test
    fun `a comment like failure restores the comment and shows an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            repository.failure = IllegalStateException()

            viewModel.onCommentLike(thread.id)
            advanceUntilIdle()

            assertEquals(
                thread,
                viewModel.uiState.value.comments
                    .findComment(thread.id),
            )
            assertEquals(R.string.community_like_error, viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `sending a comment appends it and clears the draft`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            viewModel.onDraftChange("  Nice  ")
            viewModel.onSend()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals("Nice", state.comments.last().content)
            assertEquals("", state.draft)
        }

    @Test
    fun `a reply to a reply goes to the root thread`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            viewModel.onReplyTap(reply)
            viewModel.onDraftChange("Agreed")
            viewModel.onSend()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(
                "Agreed",
                state.comments
                    .first()
                    .replies
                    .last()
                    .content,
            )
            assertNull(state.replyingTo)
        }

    @Test
    fun `a send failure keeps the draft and shows an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            repository.failure = IllegalStateException()

            viewModel.onReplyTap(thread)
            viewModel.onDraftChange("Keep me")
            viewModel.onSend()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals("Keep me", state.draft)
            assertEquals(thread, state.replyingTo)
            assertEquals(CommunityMockData.comments, state.comments)
            assertEquals(R.string.post_detail_comment_error, state.toastMessage)
        }

    @Test
    fun `a double tap on send sends once`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            viewModel.onDraftChange("Once")
            viewModel.onSend()
            viewModel.onSend()
            advanceUntilIdle()

            assertEquals(1, repository.sendCommentCount)
        }
}
