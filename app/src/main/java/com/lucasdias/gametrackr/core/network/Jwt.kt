package com.lucasdias.gametrackr.core.network

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import okio.ByteString.Companion.decodeBase64

private const val EXPIRY_LEEWAY_SECONDS = 10L
private const val MILLIS_PER_SECOND = 1000L

fun String.isJwtExpired(
    leewaySeconds: Long = EXPIRY_LEEWAY_SECONDS,
    nowMillis: Long = System.currentTimeMillis(),
): Boolean {
    val expiry = jwtExpirySeconds() ?: return false
    return expiry - nowMillis / MILLIS_PER_SECOND <= leewaySeconds
}

private fun String.jwtExpirySeconds(): Long? =
    runCatching {
        val payload = split(".").getOrNull(1) ?: return null
        val decoded = payload.decodeBase64()?.utf8() ?: return null
        Json
            .parseToJsonElement(decoded)
            .jsonObject["exp"]
            ?.jsonPrimitive
            ?.long
    }.getOrNull()
