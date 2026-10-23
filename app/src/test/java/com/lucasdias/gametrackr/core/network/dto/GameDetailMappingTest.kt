package com.lucasdias.gametrackr.core.network.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameDetailMappingTest {
    @Test
    fun `maps the header, artwork, screenshots and specs`() {
        val game =
            GameDetailDto(
                id = ID,
                name = NAME,
                summary = SUMMARY,
                firstReleaseDate = MID_MARCH_2024,
                totalRating = TOTAL_RATING,
                rating = OTHER_RATING,
                cover = GameImageDto(url = COVER_URL),
                screenshots = listOf(GameImageDto(url = SCREENSHOT_URL), GameImageDto(url = COVER_URL)),
                platforms = listOf(GameDetailPlatformDto(name = "PlayStation 5", slug = "ps5", abbreviation = "PS5")),
                genres = listOf(GameNamedDto(name = "Action RPG")),
                themes = listOf(GameNamedDto(name = "Cyberpunk")),
                gameModes = listOf(GameNamedDto(name = "Single player"), GameNamedDto(name = "Co-operative")),
                involvedCompanies =
                    listOf(
                        GameInvolvedCompanyDto(company = GameCompanyDto(DEVELOPER), developer = true),
                        GameInvolvedCompanyDto(company = GameCompanyDto(PUBLISHER), publisher = true),
                    ),
                releaseDates = listOf(GameReleaseDateDto(human = RELEASED)),
            ).toDomain()

        assertEquals(NAME, game.title)
        assertEquals("2024", game.year)
        assertEquals(TOTAL_RATING / 10, game.rating)
        assertEquals(listOf("PS5"), game.platforms)
        assertEquals(listOf("Action RPG", "Cyberpunk"), game.genres)
        assertEquals(SCREENSHOT_URL, game.heroUrl)
        assertEquals(listOf(SCREENSHOT_URL, COVER_URL), game.screenshots.map { it.url })
        assertEquals(SUMMARY, game.about)
        assertEquals(
            listOf(
                "Developer" to DEVELOPER,
                "Publisher" to PUBLISHER,
                "Released" to RELEASED,
                "Modes" to "Single player, Co-operative",
            ),
            game.specs.map { it.label to it.value },
        )
    }

    @Test
    fun `missing fields fall back to TBA, the cover, the storyline and no specs`() {
        val game =
            GameDetailDto(
                id = ID,
                name = NAME,
                storyline = STORYLINE,
                cover = GameImageDto(url = COVER_URL),
            ).toDomain()

        assertEquals("TBA", game.year)
        assertNull(game.rating)
        assertEquals(COVER_URL, game.heroUrl)
        assertTrue(game.screenshots.isEmpty())
        assertEquals(STORYLINE, game.about)
        assertTrue(game.specs.isEmpty())
    }

    @Test
    fun `a game without any description shows the default text`() {
        val game = GameDetailDto(id = ID, name = NAME).toDomain()

        assertEquals("No description available yet.", game.about)
        assertNull(game.heroUrl)
    }

    private companion object {
        const val ID = 7
        const val NAME = "Neon Ascent"
        const val SUMMARY = "A cyberpunk climb."
        const val STORYLINE = "A long story."
        const val MID_MARCH_2024 = 1_710_417_600L
        const val TOTAL_RATING = 87.0
        const val OTHER_RATING = 50.0
        const val COVER_URL = "https://images.example.com/cover.jpg"
        const val SCREENSHOT_URL = "https://images.example.com/shot.jpg"
        const val DEVELOPER = "Void Interactive"
        const val PUBLISHER = "Nova Games"
        const val RELEASED = "Mar 14, 2024"
    }
}
