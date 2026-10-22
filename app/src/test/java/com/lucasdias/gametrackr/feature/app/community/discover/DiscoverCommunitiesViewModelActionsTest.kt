package com.lucasdias.gametrackr.feature.app.community.discover

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.data.community.CommunityUpdates
import com.lucasdias.gametrackr.data.community.DiscoverMockData
import com.lucasdias.gametrackr.data.community.FakeCommunityRepository
import com.lucasdias.gametrackr.data.community.FakeDiscoverCommunitiesRepository
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DiscoverCommunitiesViewModelActionsTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val remote = FakeCommunityRepository()
    private val repository = FakeDiscoverCommunitiesRepository(remote)
    private val communityUpdates = CommunityUpdates()
    private val viewModel by lazy { DiscoverCommunitiesViewModel(repository, communityUpdates) }
    private val notJoined = CommunityMockData.all.first { !it.isJoined }

    private fun TestScope.loaded() {
        viewModel.loadIfNeeded()
        advanceUntilIdle()
    }

    private fun community(id: Long) =
        viewModel.uiState.value.communities
            .first { it.id == id }

    @Test
    fun `joining saves it in the backend`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            viewModel.onJoinTap(community(notJoined.id))
            advanceUntilIdle()

            assertTrue(community(notJoined.id).isJoined)
            assertTrue(remote.isJoined(notJoined.id))
        }

    @Test
    fun `a join failure rolls back and shows an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            repository.failure = IllegalStateException()
            viewModel.onJoinTap(community(notJoined.id))
            advanceUntilIdle()

            assertFalse(community(notJoined.id).isJoined)
            assertEquals(R.string.community_join_error, viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `a double tap on join sends one request`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            val target = community(notJoined.id)
            viewModel.onJoinTap(target)
            viewModel.onJoinTap(target)
            advanceUntilIdle()

            assertEquals(1, repository.setJoinedCount)
            assertTrue(community(notJoined.id).isJoined)
        }

    @Test
    fun `leaving asks for confirmation and saves only after confirming`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            viewModel.onJoinTap(community(notJoined.id))
            advanceUntilIdle()

            viewModel.onJoinTap(community(notJoined.id))

            assertEquals(
                notJoined.id,
                viewModel.uiState.value.communityToLeave
                    ?.id,
            )
            assertTrue(community(notJoined.id).isJoined)

            viewModel.onLeaveConfirmed()
            advanceUntilIdle()

            assertNull(viewModel.uiState.value.communityToLeave)
            assertFalse(community(notJoined.id).isJoined)
            assertFalse(remote.isJoined(notJoined.id))
        }

    @Test
    fun `joining a category community does not touch the backend`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            val target = DiscoverMockData.categoryCommunities.first { !it.isJoined }
            viewModel.onCategoryChange(target.category)
            advanceUntilIdle()
            val mock = community(target.id)

            viewModel.onJoinTap(mock)
            advanceUntilIdle()

            assertTrue(community(mock.id).isJoined)
            assertFalse(remote.isJoined(mock.id))
        }

    @Test
    fun `a join made in another screen updates the list`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()

            communityUpdates.publish(notJoined.id, isJoined = true)
            advanceUntilIdle()

            assertTrue(community(notJoined.id).isJoined)
        }

    @Test
    fun `created and removed communities update the list`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            val created = notJoined.copy(id = 9_999L)

            viewModel.onCommunityCreated(created)
            assertEquals(
                created.id,
                viewModel.uiState.value.communities
                    .first()
                    .id,
            )

            viewModel.onCommunityRemoved(created.id)
            assertFalse(
                viewModel.uiState.value.communities
                    .any { it.id == created.id },
            )
        }

    @Test
    fun `a leave failure rolls back and shows an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            viewModel.onJoinTap(community(notJoined.id))
            advanceUntilIdle()

            repository.failure = IllegalStateException()
            viewModel.onJoinTap(community(notJoined.id))
            viewModel.onLeaveConfirmed()
            advanceUntilIdle()

            assertTrue(community(notJoined.id).isJoined)
            assertEquals(R.string.community_leave_error, viewModel.uiState.value.toastMessage)
        }

    @Test
    fun `dismissing the leave confirmation keeps the community joined`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            viewModel.onJoinTap(community(notJoined.id))
            advanceUntilIdle()

            viewModel.onJoinTap(community(notJoined.id))
            viewModel.onLeaveDismissed()
            advanceUntilIdle()

            assertNull(viewModel.uiState.value.communityToLeave)
            assertTrue(community(notJoined.id).isJoined)
            assertEquals(1, repository.setJoinedCount)
        }

    @Test
    fun `a created community is not added to a filtered list`() =
        runTest(mainDispatcherRule.dispatcher) {
            loaded()
            viewModel.onCategoryChange("RPG")
            advanceUntilIdle()

            viewModel.onCommunityCreated(notJoined.copy(id = 9_999L))

            assertFalse(
                viewModel.uiState.value.communities
                    .any { it.id == 9_999L },
            )
        }
}
