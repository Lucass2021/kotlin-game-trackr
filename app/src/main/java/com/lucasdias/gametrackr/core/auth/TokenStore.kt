package com.lucasdias.gametrackr.core.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.authDataStore by preferencesDataStore(name = "auth")

class TokenStore(context: Context) {
    private val dataStore = context.authDataStore
    private val tokenKey = stringPreferencesKey("jwt_token")

    val tokenFlow: Flow<String?> = dataStore.data.map { it[tokenKey] }

    suspend fun get(): String? = tokenFlow.first()

    suspend fun save(token: String) {
        dataStore.edit { it[tokenKey] = token }
    }

    suspend fun clear() {
        dataStore.edit { it.remove(tokenKey) }
    }
}
