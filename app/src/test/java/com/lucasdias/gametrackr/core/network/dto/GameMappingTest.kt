package com.lucasdias.gametrackr.core.network.dto

import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.model.PlatformLabel
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.Instant
import java.time.ZoneId

class GameMappingTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

    private fun gameDto(raw: String) = json.decodeFromString<GameDto>(raw)

    @Test
    fun `decodes a game with every optional key omitted`() {
        val game = gameDto("""{"id":1,"name":"Elden Ring"}""").toDomain()

        assertEquals("", game.slug)
        assertNull(game.coverUrl)
        assertNull(game.releaseDate)
        assertNull(game.rating)
        assertEquals("TBA", game.year)
        assertEquals("Platform TBA", game.platformsLabel)
    }

    @Test
    fun `reads first release date as a unix timestamp not iso 8601`() {
        val game = gameDto("""{"id":1,"name":"Hades II","first_release_date":$MID_YEAR_2022}""").toDomain()

        assertEquals(
            Instant.ofEpochSecond(MID_YEAR_2022).atZone(ZoneId.systemDefault()).toLocalDate(),
            game.releaseDate,
        )
        assertEquals("2022", game.year)
    }

    @Test
    fun `prefers total rating over rating`() {
        assertEquals(
            91.5,
            gameDto("""{"id":1,"name":"A","total_rating":91.5,"rating":70.0}""").toDomain().rating!!,
            0.001,
        )
        assertEquals(70.0, gameDto("""{"id":1,"name":"A","rating":70.0}""").toDomain().rating!!, 0.001)
    }

    @Test
    fun `shortens the real igdb platform slugs`() {
        assertEquals("PS4", PlatformLabel.short("ps4--1", "PlayStation 4"))
        assertEquals("Switch 2", PlatformLabel.short("switch-2", "Nintendo Switch 2"))
        assertEquals("Xbox Series", PlatformLabel.short("series-x-s", "Xbox Series X|S"))
        assertEquals("PC", PlatformLabel.short("win", "PC (Microsoft Windows)"))
    }

    @Test
    fun `falls back to the name before the parenthesis for an unknown slug`() {
        assertEquals("Amiga", PlatformLabel.short("amiga", "Amiga (500)"))
        assertNull(PlatformLabel.short(null, null))
    }

    @Test
    fun `dedupes platform labels that collapse to the same name`() {
        val game =
            gameDto(
                """
                {"id":1,"name":"Halo","platforms":[
                  {"id":1,"name":"Xbox Series X|S","slug":"series-x-s"},
                  {"id":2,"name":"Xbox Series X","slug":"series-x"},
                  {"id":3,"name":"PC (Microsoft Windows)","slug":"win"}
                ]}
                """.trimIndent(),
            ).toDomain()

        assertEquals(listOf("Xbox Series", "PC"), game.platformNames)
    }

    @Test
    fun `summarises more than three platforms with a counter`() {
        val game = Game(id = 1, name = "A", platformNames = listOf("PS5", "PS4", "Switch", "PC", "Xbox One"))

        assertEquals("PS5, PS4, Switch +2", game.platformsLabel)
    }

    @Test
    fun `drops a platform the api sent without an id slug or name`() {
        assertNull(GamePlatformDto(id = null, slug = "ps5", name = "PlayStation 5").toDomain())
        assertNull(GamePlatformDto(id = 1, slug = null, name = "PlayStation 5").toDomain())
        assertNull(GamePlatformDto(id = 1, slug = "ps5", name = null).toDomain())
        assertEquals(
            com.lucasdias.gametrackr.core.model
                .GamePlatform(1, "ps5", "PlayStation 5"),
            GamePlatformDto(id = 1, slug = "ps5", name = "PlayStation 5").toDomain(),
        )
    }

    @Test
    fun `decodes the flat laravel pagination shape`() {
        val page =
            json.decodeFromString<PaginatedResponse<Int>>(
                """{"data":[7,8],"current_page":2,"last_page":5,"per_page":20,"total":100}""",
            )

        assertEquals(listOf(7, 8), page.data)
        assertEquals(2, page.currentPage)
        assertEquals(5, page.lastPage)
        assertEquals(100, page.total)
    }

    @Test
    fun `maps the nested igdb meta onto the same paginated model`() {
        val page =
            json
                .decodeFromString<PaginatedGamesResponse>(
                    """
                    {"message":"ok","data":[{"id":1,"name":"Elden Ring"}],
                     "meta":{"page":2,"per_page":20,"total":100,"last_page":5,"has_more":true}}
                    """.trimIndent(),
                ).toPaginated()

        assertEquals(2, page.currentPage)
        assertEquals(5, page.lastPage)
        assertEquals(20, page.perPage)
        assertEquals(100, page.total)
        assertEquals(listOf("Elden Ring"), page.data.map { it.name })
    }

    private companion object {
        const val MID_YEAR_2022 = 1_655_294_400L
    }
}
