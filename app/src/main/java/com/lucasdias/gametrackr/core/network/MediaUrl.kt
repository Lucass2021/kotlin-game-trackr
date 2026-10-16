package com.lucasdias.gametrackr.core.network

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

private val LOCAL_HOSTS = setOf("localhost", "127.0.0.1")

fun resolveMediaUrl(
    url: String,
    apiBaseUrl: String,
): String {
    val media = url.toHttpUrlOrNull() ?: return url
    val api = apiBaseUrl.toHttpUrlOrNull() ?: return url
    if (media.host !in LOCAL_HOSTS) return url
    return media
        .newBuilder()
        .scheme(api.scheme)
        .host(api.host)
        .port(api.port)
        .build()
        .toString()
}
