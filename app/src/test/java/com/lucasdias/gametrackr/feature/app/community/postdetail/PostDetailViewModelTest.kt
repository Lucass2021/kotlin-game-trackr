package com.lucasdias.gametrackr.feature.app.community.postdetail

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.totalCount
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.data.community.FakeCommunityRepository
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PostDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val post = CommunityMockData.communityPosts.first()
    private val repository = FakeCommunityRepository()
    private val viewModel = PostDetailViewModel(repository, post)

    @Test
    fun `loading shows the comments and their real count`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(CommunityMockData.comments, state.comments)
            assertTrue(state.hasLoadedComments)
            assertEquals(CommunityMockData.comments.totalCount(), state.commentCount)
        }

    @Test
    fun `the comment count uses the post until the comments load`() =
        runTest(mainDispatcherRule.dispatcher) {
            assertEquals(post.comments, viewModel.uiState.value.commentCount)

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(CommunityMockData.comments.totalCount(), viewModel.uiState.value.commentCount)
        }

    @Test
    fun `a comments failure shows the error instead of an empty list`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = IllegalStateException()

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue(state.commentsFailed)
            assertFalse(state.hasLoadedComments)
            assertFalse(state.isLoadingComments)
        }

    @Test
    fun `retrying after a failure loads the comments`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = IllegalStateException()
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            repository.failure = null
            viewModel.loadComments()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.commentsFailed)
            assertEquals(CommunityMockData.comments, state.comments)
        }

    @Test
    fun `returning to the screen does not reload the comments`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()

            viewModel.loadIfNeeded()
            advanceUntilIdle()

            assertEquals(1, repository.fetchCommentsCount)
        }

    @Test
    fun `the sheet opens while the comments are still loading`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            runCurrent()

            viewModel.onCommentsOpen()

            val state = viewModel.uiState.value
            assertTrue(state.showComments)
            assertTrue(state.isLoadingComments)
        }

    @Test
    fun `a comment sent survives closing and reopening the sheet`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.loadIfNeeded()
            advanceUntilIdle()
            viewModel.onCommentsOpen()

            viewModel.onDraftChange("Great post")
            viewModel.onSend()
            advanceUntilIdle()
            viewModel.onCommentsDismiss()
            viewModel.onCommentsOpen()

            val state = viewModel.uiState.value
            assertEquals("Great post", state.comments.last().content)
            assertEquals(CommunityMockData.comments.size + 1, state.comments.size)
        }

    @Test
    fun `deleting asks for confirmation and closes after it is deleted`() =
        runTest(mainDispatcherRule.dispatcher) {
            viewModel.onDeleteRequest()

            assertTrue(viewModel.uiState.value.showDeleteConfirmation)

            viewModel.onDeleteConfirmed()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.showDeleteConfirmation)
            assertTrue(viewModel.uiState.value.isDeleted)
            assertNull(repository.post(post.id))
        }

    @Test
    fun `deleting a post that is already gone still closes`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = PostDetailViewModel(FakeCommunityRepository(posts = emptyList()), post)

            viewModel.onDeleteConfirmed()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isDeleted)
        }

    @Test
    fun `a delete failure shows an error and stays on the post`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = IllegalStateException()

            viewModel.onDeleteConfirmed()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isDeleted)
            assertEquals(R.string.post_detail_delete_error, viewModel.uiState.value.toastMessage)
        }
}
