package com.lucasdias.gametrackr.core.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ProfileModelsTest {
    @Test
    fun `profile from a user maps every field`() {
        val user =
            User(
                id = 1,
                name = "Ada",
                email = "ada@example.com",
                username = "ada",
                profileColor = "#10B981",
                bio = "Speedruns",
                visibility = ProfileVisibility.PRIVATE,
                createdAt = LocalDate.of(2026, 1, 2),
            )

        val profile = Profile.from(user)

        assertEquals(
            Profile(
                name = "Ada",
                username = "@ada",
                bio = "Speedruns",
                joinedAt = LocalDate.of(2026, 1, 2),
                avatarHex = "#10B981",
                visibility = ProfileVisibility.PRIVATE,
            ),
            profile,
        )
    }

    @Test
    fun `profile from a user without optional fields shows no mock data`() {
        val user = User(id = 1, name = "Ada", email = "ada@example.com")

        val profile = Profile.from(user)

        assertEquals(
            Profile(
                name = "Ada",
                username = "",
                bio = "",
                joinedAt = null,
                avatarHex = Profile.DEFAULT_AVATAR_HEX,
                visibility = ProfileVisibility.PUBLIC,
            ),
            profile,
        )
    }
}
