package com.lucasdias.gametrackr.data.library

import com.lucasdias.gametrackr.core.model.LibraryEntry
import com.lucasdias.gametrackr.core.model.LibraryStats
import com.lucasdias.gametrackr.core.pagination.Page

interface LibraryRepository {
    suspend fun fetchLibrary(
        page: Int,
        perPage: Int,
    ): Result<Page<LibraryEntry>>

    suspend fun fetchLibraryStats(): Result<LibraryStats>
}
