package com.lucasdias.gametrackr.core.auth

class FakeTokenStore : TokenStore {
    var token: String? = null

    override suspend fun get(): String? = token

    override suspend fun save(token: String) {
        this.token = token
    }

    override suspend fun clear() {
        token = null
    }
}
