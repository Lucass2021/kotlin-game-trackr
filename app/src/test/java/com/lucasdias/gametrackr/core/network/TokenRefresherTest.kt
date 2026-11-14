package com.lucasdias.gametrackr.core.network

import com.lucasdias.gametrackr.core.auth.AuthStatus
import com.lucasdias.gametrackr.core.auth.FakeTokenStore
import com.lucasdias.gametrackr.core.auth.SessionManager
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class TokenRefresherTest {
    private val server = MockWebServer()
    private val tokenStore = FakeTokenStore()
    private val sessionManager = SessionManager()
    private lateinit var refresher: TokenRefresher

    @Before
    fun setUp() {
        server.start()
        val json = Json { ignoreUnknownKeys = true }
        val api =
            Retrofit
                .Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(RefreshApi::class.java)
        refresher = TokenRefresher(tokenStore, api, sessionManager)
        tokenStore.token = SPENT_TOKEN
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `refresh network failure keeps the session and surfaces the failure`() {
        server.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START))

        assertThrows(RefreshFailedException::class.java) { refresher.refresh(SPENT_TOKEN) }
        assertSessionKept()
    }

    @Test
    fun `refresh server error keeps the session and surfaces the status`() {
        server.enqueue(MockResponse().setResponseCode(HTTP_SERVER_ERROR).setBody("{}"))

        val error = assertThrows(RefreshFailedException::class.java) { refresher.refresh(SPENT_TOKEN) }
        assertEquals(HTTP_SERVER_ERROR, error.statusCode)
        assertSessionKept()
    }

    @Test
    fun `refresh rate limit keeps the session`() {
        server.enqueue(MockResponse().setResponseCode(HTTP_TOO_MANY_REQUESTS).setBody("{}"))

        assertThrows(RefreshFailedException::class.java) { refresher.refresh(SPENT_TOKEN) }
        assertSessionKept()
    }

    @Test
    fun `successful refresh without a token keeps the session`() {
        server.enqueue(MockResponse().setBody("{}"))

        assertThrows(RefreshFailedException::class.java) { refresher.refresh(SPENT_TOKEN) }
        assertSessionKept()
    }

    @Test
    fun `rejected refresh ends the session`() {
        server.enqueue(MockResponse().setResponseCode(HTTP_UNAUTHORIZED).setBody(REJECTED_BODY))

        assertNull(refresher.refresh(SPENT_TOKEN))
        assertNull(tokenStore.token)
        assertEquals(AuthStatus.Unauthenticated, sessionManager.status.value)
    }

    @Test
    fun `successful refresh saves and returns the new token`() {
        server.enqueue(MockResponse().setBody("""{"token":"$REFRESHED_TOKEN"}"""))

        assertEquals(REFRESHED_TOKEN, refresher.refresh(SPENT_TOKEN))
        assertEquals(REFRESHED_TOKEN, tokenStore.token)
        assertEquals("Bearer $SPENT_TOKEN", server.takeRequest().getHeader("Authorization"))
    }

    @Test
    fun `token already refreshed by another request is not refreshed again`() {
        tokenStore.token = REFRESHED_TOKEN

        assertEquals(REFRESHED_TOKEN, refresher.refresh(SPENT_TOKEN))
        assertEquals(0, server.requestCount)
    }

    private fun assertSessionKept() {
        assertEquals(SPENT_TOKEN, tokenStore.token)
        assertNotEquals(AuthStatus.Unauthenticated, sessionManager.status.value)
    }

    private companion object {
        const val SPENT_TOKEN = "spent.token.value"
        const val REFRESHED_TOKEN = "refreshed.token.value"
        const val HTTP_UNAUTHORIZED = 401
        const val HTTP_TOO_MANY_REQUESTS = 429
        const val HTTP_SERVER_ERROR = 500
        const val REJECTED_BODY = """{"error":"The token has been blacklisted"}"""
    }
}
