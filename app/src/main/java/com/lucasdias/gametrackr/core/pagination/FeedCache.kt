package com.lucasdias.gametrackr.core.pagination

import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.model.GamePlatform
import com.lucasdias.gametrackr.feature.app.search.SearchScope

data class FeedKey(
    val scope: SearchScope,
    val search: String?,
    val platform: GamePlatform?,
)

class FeedCache(
    private val now: () -> Long = System::currentTimeMillis,
) {
    private data class Entry(
        val snapshot: PaginationSnapshot<Game>,
        val storedAt: Long,
    )

    private val entries = mutableMapOf<FeedKey, Entry>()

    fun snapshot(key: FeedKey): PaginationSnapshot<Game>? {
        val entry = entries[key] ?: return null
        if (now() - entry.storedAt >= TIME_TO_LIVE_MILLIS) {
            entries.remove(key)
            return null
        }
        return entry.snapshot
    }

    fun store(
        key: FeedKey,
        snapshot: PaginationSnapshot<Game>,
    ) {
        entries[key] = Entry(snapshot, now())
    }

    private companion object {
        const val TIME_TO_LIVE_MILLIS = 5 * 60 * 1000L
    }
}
