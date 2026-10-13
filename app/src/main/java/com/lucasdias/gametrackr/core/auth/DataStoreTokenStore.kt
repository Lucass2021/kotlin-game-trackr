package com.lucasdias.gametrackr.core.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.authDataStore by preferencesDataStore(name = "auth")

class DataStoreTokenStore(
    context: Context,
) : TokenStore {
    private val dataStore = context.authDataStore
    private val tokenKey = stringPreferencesKey("jwt_token")

    override suspend fun get(): String? = dataStore.data.map { it[tokenKey] }.first()

    override suspend fun save(token: String) {
        dataStore.edit { it[tokenKey] = token }
    }

    override suspend fun clear() {
        dataStore.edit { it.remove(tokenKey) }
    }
}
