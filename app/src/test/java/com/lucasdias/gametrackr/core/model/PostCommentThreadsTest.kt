package com.lucasdias.gametrackr.core.model

import com.lucasdias.gametrackr.data.community.CommunityMockData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PostCommentThreadsTest {
    private val comments = CommunityMockData.comments
    private val thread = comments.first()
    private val reply = thread.replies.first()

    @Test
    fun `findComment finds threads and replies`() {
        assertEquals(thread, comments.findComment(thread.id))
        assertEquals(reply, comments.findComment(reply.id))
        assertNull(comments.findComment(-1))
    }

    @Test
    fun `replacingComment changes only the matching reply`() {
        val updated = comments.replacingComment(reply.id) { it.copy(likes = 99) }

        assertEquals(99, updated.findComment(reply.id)?.likes)
        assertEquals(thread.likes, updated.findComment(thread.id)?.likes)
        assertEquals(comments.drop(1), updated.drop(1))
    }

    @Test
    fun `addingReply to a reply appends it to the root thread`() {
        val created = reply.copy(id = 999, content = "New")

        val updated = comments.addingReply(reply.id, created)

        assertEquals(created, updated.first().replies.last())
        assertEquals(thread.replies.size + 1, updated.first().replies.size)
        assertEquals(comments.drop(1), updated.drop(1))
    }
}
