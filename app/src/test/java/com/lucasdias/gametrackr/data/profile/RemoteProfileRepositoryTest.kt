package com.lucasdias.gametrackr.data.profile

import com.lucasdias.gametrackr.core.model.Profile
import com.lucasdias.gametrackr.core.model.ProfileColor
import com.lucasdias.gametrackr.core.model.ProfileVisibility
import com.lucasdias.gametrackr.core.network.ProfileApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class RemoteProfileRepositoryTest {
    private val server = MockWebServer()
    private val json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    private lateinit var repository: RemoteProfileRepository

    @Before
    fun setUp() {
        server.start()
        val api =
            Retrofit
                .Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(ProfileApi::class.java)
        repository = RemoteProfileRepository(api, json)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `fetch colors maps the api colors to profile colors`() =
        runBlocking {
            server.enqueue(MockResponse().setResponseCode(HTTP_OK).setBody(COLORS_BODY))

            val colors = repository.fetchColors().getOrThrow()

            assertEquals(listOf(ProfileColor(key = COLOR_KEY, name = COLOR_NAME, hex = COLOR_HEX)), colors)
        }

    @Test
    fun `update profile returns the updated user`() =
        runBlocking {
            server.enqueue(MockResponse().setResponseCode(HTTP_OK).setBody(UPDATE_BODY))

            val user = updateProfile().getOrThrow()

            assertEquals(NAME, user.name)
            assertEquals(USERNAME, user.username)
            assertEquals(COLOR_HEX, user.profileColor)
            assertEquals(BIO, user.bio)
            assertEquals(ProfileVisibility.PRIVATE, user.visibility)
        }

    @Test
    fun `update profile sends bio and visibility`() =
        runBlocking {
            server.enqueue(MockResponse().setResponseCode(HTTP_OK).setBody(UPDATE_BODY))

            updateProfile()

            val body = server.takeRequest().body.readUtf8()
            assertTrue(body.contains(""""bio":"$BIO""""))
            assertTrue(body.contains(""""visibility":"private""""))
        }

    private suspend fun updateProfile() =
        repository.updateProfile(
            name = NAME,
            username = USERNAME,
            profileColor = COLOR_HEX,
            bio = BIO,
            visibility = ProfileVisibility.PRIVATE,
        )

    private companion object {
        const val HTTP_OK = 200
        const val COLOR_KEY = "purple"
        const val COLOR_NAME = "Purple"
        const val COLOR_HEX = "#8B5CF6"
        const val NAME = "Player One"
        const val USERNAME = "player_one"
        const val BIO = "Platinum hunter"
        const val COLORS_BODY = """{"message":"Profile colors fetched successfully.","data":[{"key":"$COLOR_KEY","name":"$COLOR_NAME","hex":"$COLOR_HEX"}]}"""
        const val UPDATE_BODY =
            """{"message":"Profile updated successfully.",""" +
                """"user":{"id":1,"name":"$NAME","username":"$USERNAME","email":"player@example.com","profile_color":"$COLOR_HEX",""" +
                """"bio":"$BIO","visibility":"private"}}"""
    }
}
