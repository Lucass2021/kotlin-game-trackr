package com.lucasdias.gametrackr.core.network.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CommunityDtoMappingTest {
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
}
