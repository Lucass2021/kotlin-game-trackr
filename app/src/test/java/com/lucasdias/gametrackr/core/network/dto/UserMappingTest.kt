package com.lucasdias.gametrackr.core.network.dto

import com.lucasdias.gametrackr.core.model.ProfileVisibility
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class UserMappingTest {
    @Test
    fun `maps bio, visibility and join date`() {
        val user =
            UserDto(
                id = ID,
                name = NAME,
                email = EMAIL,
                bio = BIO,
                visibility = "private",
                createdAt = CREATED_AT,
            ).toDomain()

        assertEquals(BIO, user.bio)
        assertEquals(ProfileVisibility.PRIVATE, user.visibility)
        assertEquals(LocalDate.of(2026, 6, 25), user.createdAt)
    }

    @Test
    fun `missing fields fall back to no bio, public and no join date`() {
        val user = UserDto(id = ID, name = NAME, email = EMAIL).toDomain()

        assertNull(user.bio)
        assertEquals(ProfileVisibility.PUBLIC, user.visibility)
        assertNull(user.createdAt)
    }

    private companion object {
        const val ID = 1
        const val NAME = "Player One"
        const val EMAIL = "player@example.com"
        const val BIO = "Platinum hunter"
        const val CREATED_AT = "2026-06-25T10:00:00.000000Z"
    }
}
