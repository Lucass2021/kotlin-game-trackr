package com.lucasdias.gametrackr.data.auth

interface AccountRepository {
    suspend fun deleteAccount(): Result<Unit>
}
