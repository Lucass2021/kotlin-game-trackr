package com.lucasdias.gametrackr.core.network

import android.util.Base64
import com.lucasdias.gametrackr.support.TestJwt
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class JwtTest {
    @Before
    fun delegateAndroidBase64ToTheJvmDecoder() {
        mockkStatic(Base64::class)
        every { Base64.decode(any<String>(), any()) } answers {
            java.util.Base64
                .getUrlDecoder()
                .decode(firstArg<String>())
        }
    }

    @After
    fun releaseTheStaticMock() {
        unmockkStatic(Base64::class)
    }

    @Test
    fun `treats an already expired token as expired`() {
        assertTrue(TestJwt.tokenExpiringIn(-60).isJwtExpired())
    }

    @Test
    fun `treats a token with plenty of life left as valid`() {
        assertFalse(TestJwt.tokenExpiringIn(3600).isJwtExpired())
    }

    @Test
    fun `expires a token that dies inside the leeway`() {
        assertTrue(TestJwt.tokenExpiringIn(5).isJwtExpired())
        assertFalse(TestJwt.tokenExpiringIn(5).isJwtExpired(leewaySeconds = 0))
    }

    @Test
    fun `decodes a base64url payload without padding`() {
        val token = TestJwt.token("""{"exp":2000000000,"sub":"?>?","jti":"a?b"}""")
        val payload = token.split(".")[1]

        assertTrue(payload.contains("-"))
        assertTrue(payload.contains("_"))
        assertFalse(token.isJwtExpired())
    }

    @Test
    fun `reads a payload whose length is not a multiple of four`() {
        val token = TestJwt.token("""{"exp":2000000000,"a":12}""")

        assertTrue(token.split(".")[1].length % 4 != 0)
        assertFalse(token.isJwtExpired())
    }

    @Test
    fun `does not expire a token it cannot read`() {
        assertFalse("".isJwtExpired())
        assertFalse("not-a-jwt".isJwtExpired())
        assertFalse("only.two".isJwtExpired())
        assertFalse("a.!!!not base64!!!.c".isJwtExpired())
        assertFalse(TestJwt.token("not json").isJwtExpired())
        assertFalse(TestJwt.token("""{"sub":"lucas"}""").isJwtExpired())
    }
}
