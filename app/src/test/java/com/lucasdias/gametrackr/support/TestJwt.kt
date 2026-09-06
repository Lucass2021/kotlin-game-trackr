package com.lucasdias.gametrackr.support

import java.util.Base64

object TestJwt {
    fun token(payload: String): String = "${base64Url(HEADER)}.${base64Url(payload)}.signature"

    fun tokenExpiringIn(seconds: Long): String = token("""{"exp":${System.currentTimeMillis() / 1000 + seconds}}""")

    fun base64Url(value: String): String =
        Base64
            .getUrlEncoder()
            .withoutPadding()
            .encodeToString(value.toByteArray())

    private const val HEADER = """{"alg":"HS256","typ":"JWT"}"""
}
