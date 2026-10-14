package com.lucasdias.gametrackr.data.library

import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.feature.app.library.LibraryEntry
import com.lucasdias.gametrackr.feature.app.library.LibraryStats

interface LibraryRepository {
    suspend fun fetchLibrary(
        page: Int,
        perPage: Int,
    ): Result<Page<LibraryEntry>>

    suspend fun fetchLibraryStats(): Result<LibraryStats>
}
