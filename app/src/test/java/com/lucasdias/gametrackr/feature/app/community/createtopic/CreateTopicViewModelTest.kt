package com.lucasdias.gametrackr.feature.app.community.createtopic

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityPost
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.core.ui.text.UiText
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.data.community.FakeCommunityRepository
import com.lucasdias.gametrackr.data.community.PostUpdates
import kotlinx.coroutines.launch
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

class CreateTopicViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val community = CommunityMockData.all.first { it.isJoined }
    private val repository = FakeCommunityRepository()
    private val postUpdates = PostUpdates()

    private fun viewModel(
        locked: Community? = community,
        title: String = TITLE,
        body: String = BODY,
    ) = CreateTopicViewModel(locked, repository, postUpdates).apply {
        onTitleChange(title)
        onBodyChange(body)
    }

    private fun TestScope.createdPosts(): List<CommunityPost> {
        val posts = mutableListOf<CommunityPost>()
        backgroundScope.launch { postUpdates.createdPosts.collect { posts.add(it) } }
        runCurrent()
        return posts
    }

    @Test
    fun `errors show only after the first submit`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel(locked = null, title = "", body = "")
            assertNull(viewModel.uiState.value.titleError)

            viewModel.onSubmit()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(UiText.Resource(R.string.validation_community_required), state.communityError)
            assertEquals(UiText.Resource(R.string.validation_topic_title_required), state.titleError)
            assertEquals(UiText.Resource(R.string.validation_topic_body_required), state.bodyError)
            assertEquals(0, repository.createPostCount)
        }

    @Test
    fun `submit sends the trimmed post and publishes it`() =
        runTest(mainDispatcherRule.dispatcher) {
            val published = createdPosts()
            val viewModel = viewModel(title = "  $TITLE  ", body = "  $BODY  ")

            viewModel.onSubmit()
            advanceUntilIdle()
            runCurrent()

            val created = viewModel.uiState.value.createdPost

            assertEquals(TITLE, created?.title)
            assertEquals(BODY, created?.preview)
            assertEquals(community.id, created?.communityId)
            assertEquals(listOf(created), published)
            assertFalse(viewModel.uiState.value.isSubmitting)
        }

    @Test
    fun `a create failure shows a toast and keeps the form`() =
        runTest(mainDispatcherRule.dispatcher) {
            val published = createdPosts()
            repository.failure = ApiError.Network
            val viewModel = viewModel()

            viewModel.onSubmit()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(UiText.Resource(R.string.error_network), state.toastMessage)
            assertEquals(TITLE, state.title)
            assertNull(state.createdPost)
            assertTrue(published.isEmpty())
        }

    @Test
    fun `submit after a successful create sends no request`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            viewModel.onSubmit()
            advanceUntilIdle()

            viewModel.onSubmit()
            advanceUntilIdle()

            assertEquals(1, repository.createPostCount)
        }

    @Test
    fun `a second tap while posting sends no request`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()

            viewModel.onSubmit()
            viewModel.onSubmit()
            advanceUntilIdle()

            assertEquals(1, repository.createPostCount)
        }

    @Test
    fun `a locked community is selected right away without loading the joined list`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()

            viewModel.loadIfNeeded()

            val state = viewModel.uiState.value
            assertEquals(community, state.community)
            assertTrue(state.isCommunityLocked)
            assertFalse(state.isLoadingCommunities)
            viewModel.onCommunityChange(CommunityMockData.all.last())
            assertEquals(community, viewModel.uiState.value.community)
        }

    @Test
    fun `a joined communities failure shows an error and retry loads them`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = ApiError.Network
            val viewModel = viewModel(locked = null)

            viewModel.loadIfNeeded()
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.communitiesFailed)

            repository.failure = null
            viewModel.retryCommunities()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.communitiesFailed)
            assertTrue(state.communities.isNotEmpty())
            assertTrue(state.communities.all { it.isJoined })
        }

    @Test
    fun `title limit ignores surrounding whitespace and says at most`() =
        runTest(mainDispatcherRule.dispatcher) {
            val longest = "a".repeat(CreateTopicUiState.TITLE_LIMIT)
            assertTrue(viewModel(title = "  $longest  ").uiState.value.canSubmit)

            val viewModel = viewModel(title = longest + "a")
            viewModel.onSubmit()

            assertEquals(
                UiText.Resource(R.string.validation_topic_title_too_long, listOf(CreateTopicUiState.TITLE_LIMIT)),
                viewModel.uiState.value.titleError,
            )
            assertEquals(0, repository.createPostCount)
        }

    private companion object {
        const val TITLE = "Best co-op games of the year?"
        const val BODY = "Looking for something to play with friends this weekend."
    }
}
