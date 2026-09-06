package com.lucasdias.gametrackr.feature.app.gamedetail

import com.lucasdias.gametrackr.core.network.dto.GameDetailDto
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GameDetailMappingTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

    private fun detail(raw: String) = json.decodeFromString<GameDetailDto>(raw).toDomain()

    @Test
    fun `prefers a screenshot over artwork and cover for the hero`() {
        val all =
            detail(
                """
                {"id":1,"name":"A","cover":{"url":"cover"},
                 "artworks":[{"url":"art"}],"screenshots":[{"url":"shot"}]}
                """.trimIndent(),
            )

        assertEquals("shot", all.heroUrl)
        assertEquals("art", detail("""{"id":1,"name":"A","cover":{"url":"cover"},"artworks":[{"url":"art"}]}""").heroUrl)
        assertEquals("cover", detail("""{"id":1,"name":"A","cover":{"url":"cover"}}""").heroUrl)
        assertNull(detail("""{"id":1,"name":"A"}""").heroUrl)
    }

    @Test
    fun `brings the zero to hundred rating down to the ten point scale`() {
        assertEquals(8.6, detail("""{"id":1,"name":"A","total_rating":86.0}""").rating!!, 0.001)
        assertEquals(9.0, detail("""{"id":1,"name":"A","aggregated_rating":90.0}""").rating!!, 0.001)
        assertEquals(7.0, detail("""{"id":1,"name":"A","rating":70.0}""").rating!!, 0.001)
    }

    @Test
    fun `leaves the rating null for an unreleased game`() {
        assertNull(detail("""{"id":1,"name":"A"}""").rating)
    }

    @Test
    fun `merges genres and themes into one chip list`() {
        val game =
            detail(
                """
                {"id":1,"name":"A","genres":[{"name":"RPG"},{"name":"Adventure"}],
                 "themes":[{"name":"Fantasy"},{"id":9}]}
                """.trimIndent(),
            )

        assertEquals(listOf("RPG", "Adventure", "Fantasy"), game.genres)
    }

    @Test
    fun `prefers the platform abbreviation when igdb sends one`() {
        val game =
            detail(
                """
                {"id":1,"name":"A","platforms":[
                  {"name":"PlayStation 5","slug":"ps5","abbreviation":"PS5"},
                  {"name":"Nintendo Switch 2","slug":"switch-2"}
                ]}
                """.trimIndent(),
            )

        assertEquals(listOf("PS5", "Switch 2"), game.platforms)
    }

    @Test
    fun `only lists the specifications igdb actually returned`() {
        val game =
            detail(
                """
                {"id":1,"name":"A","game_engines":[{"name":"Unreal"}],
                 "involved_companies":[{"company":{"name":"FromSoftware"},"developer":true,"publisher":false}],
                 "release_dates":[{"human":"Feb 25, 2022"}]}
                """.trimIndent(),
            )

        assertEquals(listOf("Developer", "Released", "Engine"), game.specs.map { it.label })
        assertEquals(listOf("FromSoftware", "Feb 25, 2022", "Unreal"), game.specs.map { it.value })
    }

    @Test
    fun `falls back to the summary chain for the about text`() {
        assertEquals("s", detail("""{"id":1,"name":"A","summary":"s","storyline":"l"}""").about)
        assertEquals("l", detail("""{"id":1,"name":"A","storyline":"l"}""").about)
        assertEquals("No description available yet.", detail("""{"id":1,"name":"A"}""").about)
    }

    @Test
    fun `shows tba when the game has no release date`() {
        assertEquals("TBA", detail("""{"id":1,"name":"A"}""").year)
        assertEquals("2022", detail("""{"id":1,"name":"A","first_release_date":1655294400}""").year)
    }
}
