package com.lucasdias.gametrackr.data.community

import com.lucasdias.gametrackr.core.network.ApiError
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FakeCommunityRepositoryTest {
    private val repository = FakeCommunityRepository()

    @Test
    fun `concurrent comments get distinct ids`() =
        runTest {
            val first = async { repository.addComment(POST_ID, "First").getOrThrow().id }
            val second = async { repository.addComment(POST_ID, "Second").getOrThrow().id }
            val ids = listOf(first.await(), second.await())

            assertEquals(ids.size, ids.toSet().size)
        }

    @Test
    fun `concurrent posts get distinct ids`() =
        runTest {
            val first = async { repository.createPost("First", "Body", COMMUNITY_ID).getOrThrow().id }
            val second = async { repository.createPost("Second", "Body", COMMUNITY_ID).getOrThrow().id }
            val ids = listOf(first.await(), second.await())

            assertEquals(ids.size, ids.toSet().size)
        }

    @Test
    fun `concurrent communities get distinct ids`() =
        runTest {
            val first = async { repository.createCommunity("First", "About").getOrThrow().id }
            val second = async { repository.createCommunity("Second", "About").getOrThrow().id }
            val ids = listOf(first.await(), second.await())

            assertEquals(ids.size, ids.toSet().size)
        }

    @Test
    fun `concurrent replies get distinct ids`() =
        runTest {
            val parentId = CommunityPostMockData.comments.first().id
            val first = async { repository.replyToComment(POST_ID, parentId, "First").getOrThrow().id }
            val second = async { repository.replyToComment(POST_ID, parentId, "Second").getOrThrow().id }
            val ids = listOf(first.await(), second.await())

            assertEquals(ids.size, ids.toSet().size)
        }

    @Test
    fun `replying to a missing comment fails with not found`() =
        runTest {
            val result = repository.replyToComment(POST_ID, MISSING_COMMENT_ID, "Reply")

            assertTrue(result.exceptionOrNull() is ApiError.NotFound)
        }

    private companion object {
        const val POST_ID = 1L
        const val COMMUNITY_ID = 1L
        const val MISSING_COMMENT_ID = 9999L
    }
}
