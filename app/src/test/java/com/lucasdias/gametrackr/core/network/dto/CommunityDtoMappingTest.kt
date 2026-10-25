package com.lucasdias.gametrackr.core.network.dto

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CommunityDtoMappingTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

    private fun dto(createdAt: String?) = CommunityDto(id = 1, title = "Soulsborne", slug = "soulsborne", authorId = 1, createdAt = createdAt)

    @Test
    fun `the created month comes from the api creation date`() {
        assertEquals("June 2026", dto("2026-06-25T09:27:09.000000Z").toDomain().createdMonth)
    }

    @Test
    fun `a missing or invalid creation date leaves the created month empty`() {
        assertNull(dto(null).toDomain().createdMonth)
        assertNull(dto("not a date").toDomain().createdMonth)
        assertNull(dto("2026-13-45T00:00:00.000000Z").toDomain().createdMonth)
    }

    @Test
    fun `the post author username comes from the api author`() {
        val body =
            """
            {"id":7,"title":"Builds","slug":"builds","community_id":1,"author_id":3,
            "author":{"id":3,"name":"Shadow Reaper","username":"shadowreaper","profile_color":"#10B981"}}
            """.trimIndent()

        val post = json.decodeFromString<PostDto>(body).toDomain()

        assertEquals("@Shadow Reaper", post.author)
        assertEquals("shadowreaper", post.authorUsername)
    }

    @Test
    fun `the member username comes from the api user`() {
        val body = """{"id":3,"name":"Shadow Reaper","username":"shadowreaper"}"""

        val member = json.decodeFromString<CommunityUserDto>(body).toDomain()

        assertEquals("shadowreaper", member.username)
    }
}
