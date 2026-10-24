package com.lucasdias.gametrackr.feature.app.community.communitydetail

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
import org.junit.Rule
import org.junit.Test

class CommunityDetailViewModelSyncTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val community = CommunityMockData.detailCommunity
    private val post = CommunityMockData.communityPosts.first()
    private val repository = FakeCommunityRepository(communities = listOf(community))
    private val postUpdates = PostUpdates()
    private val viewModel by lazy {
        CommunityDetailViewModel(repository, FakeCommunityFeedRepository(repository), postUpdates, CommunityUpdates(), community)
    }

    private fun TestScope.loaded() {
        viewModel.loadIfNeeded()
        advanceUntilIdle()
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

    @Test
    fun `a post created for this community is added to the top of its posts`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            val created = post.copy(id = CREATED_POST_ID, communityId = community.id)

            postUpdates.publishCreated(created)
            postUpdates.publishCreated(created.copy(id = OTHER_POST_ID, communityId = community.id + 1))
            advanceUntilIdle()

            val posts = viewModel.uiState.value.posts
            assertEquals(CREATED_POST_ID, posts.first().id)
            assertFalse(posts.any { it.id == OTHER_POST_ID })
        }

    private fun likedPost() =
        viewModel.uiState.value.posts
            .first { it.id == post.id }

    private companion object {
        const val CREATED_POST_ID = 9_001L
        const val OTHER_POST_ID = 9_002L
    }
}
