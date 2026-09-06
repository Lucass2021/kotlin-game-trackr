package com.lucasdias.gametrackr.core.pagination

import com.lucasdias.gametrackr.core.network.dto.PaginatedResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PaginationStateTest {
    private fun page(
        current: Int,
        last: Int,
        total: Int = last * 20,
    ) = PaginatedResponse<Int>(
        data = emptyList(),
        currentPage = current,
        lastPage = last,
        perPage = 20,
        total = total,
    )

    @Test
    fun `starts ready to load the first page`() {
        val state = PaginationState<Int>()

        assertTrue(state.canLoadMore)
        assertEquals(0, state.currentPage)
        assertEquals(1, state.lastPage)
        assertTrue(state.items.isEmpty())
    }

    @Test
    fun `accumulates pages and copies the server meta`() {
        val state = PaginationState<Int>()

        state.append(page(current = 1, last = 3, total = 50), listOf(1, 2))
        state.append(page(current = 2, last = 3, total = 50), listOf(3, 4))

        assertEquals(listOf(1, 2, 3, 4), state.items.toList())
        assertEquals(2, state.currentPage)
        assertEquals(3, state.lastPage)
        assertEquals(50, state.total)
        assertTrue(state.canLoadMore)
    }

    @Test
    fun `stops once the last page arrives`() {
        val state = PaginationState<Int>()

        state.append(page(current = 3, last = 3), listOf(1))

        assertFalse(state.canLoadMore)
    }

    @Test
    fun `refuses to load more while a request is in flight`() {
        val state = PaginationState<Int>()
        state.append(page(current = 1, last = 5), listOf(1))

        state.setLoading(true)
        assertFalse(state.canLoadMore)

        state.setLoading(false)
        assertTrue(state.canLoadMore)
    }

    @Test
    fun `reset puts the state back to a pending first page`() {
        val state = PaginationState<Int>()
        state.append(page(current = 2, last = 4, total = 80), listOf(1, 2))

        state.reset()

        assertTrue(state.items.isEmpty())
        assertEquals(0, state.currentPage)
        assertEquals(1, state.lastPage)
        assertEquals(0, state.total)
        assertTrue(state.canLoadMore)
    }

    @Test
    fun `appending after a reset does not bring the old page back`() {
        val state = PaginationState<Int>()
        state.append(page(current = 1, last = 4), listOf(1, 2))

        state.reset()
        state.append(page(current = 1, last = 1), listOf(9))

        assertEquals(listOf(9), state.items.toList())
    }

    @Test
    fun `snapshot and restore round trip the whole state`() {
        val state = PaginationState<Int>()
        state.append(page(current = 2, last = 7, total = 140), listOf(1, 2, 3))

        val restored = PaginationState<Int>()
        restored.restore(state.snapshot())

        assertEquals(listOf(1, 2, 3), restored.items.toList())
        assertEquals(2, restored.currentPage)
        assertEquals(7, restored.lastPage)
        assertEquals(140, restored.total)
    }

    @Test
    fun `restore replaces whatever was there before`() {
        val state = PaginationState<Int>()
        state.append(page(current = 1, last = 2), listOf(1, 2))

        state.restore(PaginationSnapshot(listOf(9), currentPage = 1, lastPage = 1, total = 1))

        assertEquals(listOf(9), state.items.toList())
        assertFalse(state.canLoadMore)
    }

    @Test
    fun `the snapshot does not alias the live item list`() {
        val state = PaginationState<Int>()
        state.append(page(current = 1, last = 2), listOf(1, 2))

        val snapshot = state.snapshot()
        state.append(page(current = 2, last = 2), listOf(3))

        assertEquals(listOf(1, 2), snapshot.items)
    }
}
