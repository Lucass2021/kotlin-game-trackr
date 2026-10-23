package com.lucasdias.gametrackr.data.library

import com.lucasdias.gametrackr.core.model.LibraryEntry
import com.lucasdias.gametrackr.core.model.LibraryStats
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.model.NewLibraryEntry
import com.lucasdias.gametrackr.core.pagination.Page

interface LibraryRepository {
    suspend fun fetchLibrary(
        status: LibraryStatus?,
        page: Int,
        perPage: Int,
    ): Result<Page<LibraryEntry>>

    suspend fun fetchLibraryStats(): Result<LibraryStats>

    suspend fun setFavorite(
        slug: String,
        isFavorite: Boolean,
    ): Result<Unit>

    suspend fun addToLibrary(entry: NewLibraryEntry): Result<Unit>

    suspend fun isInLibrary(slug: String): Result<Boolean>
}
