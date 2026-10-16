package com.lucasdias.gametrackr.data.library

import com.lucasdias.gametrackr.core.model.LibraryEntry
import com.lucasdias.gametrackr.core.model.LibraryStats
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.core.pagination.fakePage
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeLibraryRepository(
    entries: List<LibraryEntry> = LibraryMockData.entries,
    private val stats: LibraryStats = LibraryMockData.stats,
    var failure: Throwable? = null,
) : LibraryRepository {
    private val entries = entries.toMutableList()

    var fetchCount = 0
        private set

    var setFavoriteCount = 0
        private set

    override suspend fun fetchLibrary(
        status: LibraryStatus?,
        page: Int,
        perPage: Int,
    ): Result<Page<LibraryEntry>> {
        fetchCount++
        return respond {
            val filtered = if (status == null) entries.toList() else entries.filter { it.status == status }
            fakePage(filtered, page, perPage)
        }
    }

    override suspend fun fetchLibraryStats(): Result<LibraryStats> = respond { stats }

    override suspend fun setFavorite(
        slug: String,
        isFavorite: Boolean,
    ): Result<Unit> {
        setFavoriteCount++
        return respond {
            val index = entries.indexOfFirst { it.slug == slug }
            if (index < 0) throw ApiError.NotFound
            entries[index] = entries[index].copy(isFavorite = isFavorite)
        }
    }

    fun isFavorite(slug: String): Boolean = entries.firstOrNull { it.slug == slug }?.isFavorite == true

    private suspend fun <T> respond(block: () -> T): Result<T> {
        val error = failure
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return error?.let { Result.failure(it) } ?: runCatching(block)
    }
}
