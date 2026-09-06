package com.lucasdias.gametrackr.core.pagination

import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.model.GamePlatform
import com.lucasdias.gametrackr.feature.app.search.SearchScope
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class FeedCacheTest {
    private val key = FeedKey(scope = SearchScope.ALL, search = null, platform = null)
    private val snapshot =
        PaginationSnapshot<Game>(items = emptyList(), currentPage = 2, lastPage = 5, total = 100)

    @Test
    fun `returns the snapshot while the entry is fresh`() {
        var now = 0L
        val cache = FeedCache { now }

        cache.store(key, snapshot)
        now = 4 * 60 * 1000L

        assertEquals(2, cache.snapshot(key)?.currentPage)
    }

    @Test
    fun `drops the entry once the five minute ttl passes`() {
        var now = 0L
        val cache = FeedCache { now }

        cache.store(key, snapshot)
        now = 5 * 60 * 1000L + 1

        assertNull(cache.snapshot(key))
    }

    @Test
    fun `keys entries by scope search and platform`() {
        val cache = FeedCache { 0L }
        cache.store(key, snapshot)

        assertNotNull(cache.snapshot(key))
        assertNull(cache.snapshot(key.copy(search = "elden")))
        assertNull(cache.snapshot(key.copy(scope = SearchScope.NEW_RELEASES)))
        assertNull(cache.snapshot(key.copy(platform = GamePlatform(id = 48, slug = "ps4--1", name = "PlayStation 4"))))
    }

    @Test
    fun `forgets an expired key instead of keeping it around`() {
        var now = 0L
        val cache = FeedCache { now }
        cache.store(key, snapshot)

        now = 6 * 60 * 1000L
        assertNull(cache.snapshot(key))

        now = 0L
        assertNull(cache.snapshot(key))
    }
}
