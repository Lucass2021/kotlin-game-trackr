package com.lucasdias.gametrackr.core.network

import android.util.Base64
import org.json.JSONObject

private const val EXPIRY_LEEWAY_SECONDS = 10L

fun String.isJwtExpired(leewaySeconds: Long = EXPIRY_LEEWAY_SECONDS): Boolean {
    val expiry = jwtExpirySeconds() ?: return false
    return expiry - System.currentTimeMillis() / 1000 <= leewaySeconds
}

private fun String.jwtExpirySeconds(): Long? =
    runCatching {
        val payload = split(".").getOrNull(1) ?: return null
        val decoded = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        JSONObject(String(decoded)).getLong("exp")
    }.getOrNull()
