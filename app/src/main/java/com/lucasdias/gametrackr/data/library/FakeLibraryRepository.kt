package com.lucasdias.gametrackr.data.library

import com.lucasdias.gametrackr.core.model.LibraryEntry
import com.lucasdias.gametrackr.core.model.LibraryStats
import com.lucasdias.gametrackr.core.pagination.Page
import com.lucasdias.gametrackr.core.pagination.fakePage
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeLibraryRepository : LibraryRepository {
    override suspend fun fetchLibrary(
        page: Int,
        perPage: Int,
    ): Result<Page<LibraryEntry>> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(fakePage(LibraryMockData.entries, page, perPage))
    }

    override suspend fun fetchLibraryStats(): Result<LibraryStats> {
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return Result.success(LibraryMockData.stats)
    }
}
