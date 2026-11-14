package com.lucasdias.gametrackr.core.network

import com.lucasdias.gametrackr.core.auth.AuthStatus
import com.lucasdias.gametrackr.core.auth.FakeTokenStore
import com.lucasdias.gametrackr.core.auth.SessionManager
import com.lucasdias.gametrackr.data.auth.RemoteAuthRepository
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class AuthenticatedClientTest {
    private val server = MockWebServer()
    private val tokenStore = FakeTokenStore()
    private val sessionManager = SessionManager()
    private val json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    private var refreshResponse = MockResponse()
    private var refreshCount = 0
    private lateinit var repository: RemoteAuthRepository

    @Before
    fun setUp() {
        server.dispatcher =
            object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse =
                    if (request.path.orEmpty().endsWith(REFRESH_PATH)) {
                        refreshCount += 1
                        refreshResponse
                    } else {
                        MockResponse().setResponseCode(HTTP_UNAUTHORIZED).setBody("{}")
                    }
            }
        server.start()
        val refreshApi = retrofit(OkHttpClient()).create(RefreshApi::class.java)
        val refresher = TokenRefresher(tokenStore, refreshApi, sessionManager)
        val client =
            OkHttpClient
                .Builder()
                .addInterceptor(AuthInterceptor(tokenStore, refresher))
                .authenticator(TokenAuthenticator(refresher))
                .build()
        repository = RemoteAuthRepository(retrofit(client).create(AuthApi::class.java), tokenStore, sessionManager, json)
        tokenStore.token = SPENT_TOKEN
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `a refresh server error reaches the caller and keeps the session`() =
        runBlocking {
            refreshResponse = MockResponse().setResponseCode(HTTP_SERVER_ERROR).setBody("{}")

            val error = repository.validate().exceptionOrNull()

            assertEquals(ApiError.Server(HTTP_SERVER_ERROR), error)
            assertEquals(1, refreshCount)
            assertEquals(SPENT_TOKEN, tokenStore.token)
            assertNotEquals(AuthStatus.Unauthenticated, sessionManager.status.value)
        }

    @Test
    fun `a rejected refresh reaches the caller as unauthorized and ends the session`() =
        runBlocking {
            refreshResponse = MockResponse().setResponseCode(HTTP_UNAUTHORIZED).setBody(REJECTED_BODY)

            val error = repository.validate().exceptionOrNull()

            assertEquals(ApiError.Unauthorized, error)
            assertEquals(AuthStatus.Unauthenticated, sessionManager.status.value)
        }

    private fun retrofit(client: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(server.url("/"))
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    private companion object {
        const val SPENT_TOKEN = "spent.token.value"
        const val REFRESH_PATH = "auth/refresh"
        const val HTTP_UNAUTHORIZED = 401
        const val HTTP_SERVER_ERROR = 500
        const val REJECTED_BODY = """{"error":"The token has been blacklisted"}"""
    }
}
