package com.lucasdias.gametrackr.data.auth

import com.lucasdias.gametrackr.core.auth.FakeTokenStore
import com.lucasdias.gametrackr.core.auth.SessionManager
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.network.AuthApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class RemoteAuthRepositoryTest {
    private val server = MockWebServer()
    private val tokenStore = FakeTokenStore()
    private val json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    private lateinit var repository: RemoteAuthRepository

    @Before
    fun setUp() {
        server.start()
        val api =
            Retrofit
                .Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(AuthApi::class.java)
        repository = RemoteAuthRepository(api, tokenStore, SessionManager(), json)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `login with wrong password fails with invalid credentials`() =
        runBlocking {
            server.enqueue(MockResponse().setResponseCode(HTTP_UNAUTHORIZED).setBody(INVALID_CREDENTIALS_BODY))

            val result = repository.login(EMAIL, PASSWORD)

            assertEquals(ApiError.Unauthorized, result.exceptionOrNull())
            assertNull(tokenStore.token)
        }

    @Test
    fun `login success saves the token and returns the user`() =
        runBlocking {
            server.enqueue(MockResponse().setResponseCode(HTTP_OK).setBody(LOGIN_SUCCESS_BODY))

            val result = repository.login(EMAIL, PASSWORD)

            assertEquals(EMAIL, result.getOrNull()?.email)
            assertEquals(TOKEN, tokenStore.token)
        }

    private companion object {
        const val HTTP_OK = 200
        const val HTTP_UNAUTHORIZED = 401
        const val EMAIL = "player@gametrackr.dev"
        const val PASSWORD = "secret123"
        const val TOKEN = "header.payload.signature"
        const val INVALID_CREDENTIALS_BODY = """{"error":"Invalid credentials"}"""
        const val LOGIN_SUCCESS_BODY =
            """{"token":"$TOKEN","user":{"id":1,"name":"Player","email":"$EMAIL"},"message":"Logged in successfully"}"""
    }
}
