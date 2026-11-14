package com.lucasdias.gametrackr.core.network.dto

import com.lucasdias.gametrackr.core.model.CoverPalette
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

    @Test
    fun `the community palettes come from the api ids`() {
        val post =
            json
                .decodeFromString<PostDto>(
                    """{"id":7,"title":"Builds","slug":"builds","community_id":1,"author_id":3}""",
                ).toDomain()
        val member = json.decodeFromString<CommunityUserDto>("""{"id":3,"name":"Shadow Reaper"}""").toDomain()
        val comment =
            json
                .decodeFromString<CommentDto>(
                    """{"id":4,"content":"Nice","author_id":3,"post_id":7}""",
                ).toDomain()

        assertEquals(CoverPalette.CRIMSON, dto(null).toDomain().icon)
        assertEquals(CoverPalette.EMERALD, post.avatar)
        assertEquals(CoverPalette.AZURE, post.media)
        assertEquals(CoverPalette.AZURE, member.avatar)
        assertEquals(CoverPalette.VIOLET, comment.avatar)
    }
}
