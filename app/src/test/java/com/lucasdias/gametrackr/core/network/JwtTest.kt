package com.lucasdias.gametrackr.core.network

import okio.ByteString.Companion.encodeUtf8
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class JwtTest {
    @Test
    fun `token with exp in the future is not expired`() {
        assertFalse(tokenWithPayload("""{"exp":${NOW_SECONDS + ONE_HOUR}}""").isJwtExpired(nowMillis = NOW_MILLIS))
    }

    @Test
    fun `token with exp in the past is expired`() {
        assertTrue(tokenWithPayload("""{"exp":${NOW_SECONDS - ONE_HOUR}}""").isJwtExpired(nowMillis = NOW_MILLIS))
    }

    @Test
    fun `token expiring inside the leeway is expired`() {
        assertTrue(tokenWithPayload("""{"exp":${NOW_SECONDS + INSIDE_LEEWAY}}""").isJwtExpired(nowMillis = NOW_MILLIS))
    }

    @Test
    fun `token without exp is not expired`() {
        assertFalse(tokenWithPayload("""{"sub":"1"}""").isJwtExpired(nowMillis = NOW_MILLIS))
    }

    @Test
    fun `malformed token is not expired`() {
        assertFalse("not-a-jwt".isJwtExpired(nowMillis = NOW_MILLIS))
    }

    private fun tokenWithPayload(json: String): String {
        val payload = json.encodeUtf8().base64Url().trimEnd('=')
        return "header.$payload.signature"
    }

    private companion object {
        const val NOW_SECONDS = 1_800_000_000L
        const val MILLIS_PER_SECOND = 1000L
        const val NOW_MILLIS = NOW_SECONDS * MILLIS_PER_SECOND
        const val ONE_HOUR = 3600L
        const val INSIDE_LEEWAY = 5L
    }
}
