package com.lucasdias.gametrackr.core.pagination

import org.junit.Assert.assertEquals
import org.junit.Test

class FakePageTest {
    @Test
    fun `fake page returns the requested slice with page metadata`() {
        val page = fakePage(ITEMS, page = 2, perPage = PER_PAGE)

        assertEquals(listOf("d", "e"), page.items)
        assertEquals(2, page.currentPage)
        assertEquals(2, page.lastPage)
        assertEquals(ITEMS.size, page.total)
    }

    @Test
    fun `fake page of an empty list is a single empty page`() {
        val page = fakePage(emptyList<String>(), page = 1, perPage = PER_PAGE)

        assertEquals(emptyList<String>(), page.items)
        assertEquals(1, page.lastPage)
        assertEquals(0, page.total)
    }

    private companion object {
        const val PER_PAGE = 3
        val ITEMS = listOf("a", "b", "c", "d", "e")
    }
}
