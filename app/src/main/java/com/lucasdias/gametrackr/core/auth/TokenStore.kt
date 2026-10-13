package com.lucasdias.gametrackr.core.auth

interface TokenStore {
    suspend fun get(): String?

    suspend fun save(token: String)

    suspend fun clear()
}
