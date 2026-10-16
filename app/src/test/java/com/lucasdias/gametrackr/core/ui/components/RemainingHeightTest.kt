package com.lucasdias.gametrackr.core.ui.components

import org.junit.Assert.assertEquals
import org.junit.Test

class RemainingHeightTest {
    @Test
    fun `remaining height is the viewport below the item minus the bottom padding`() {
        val remaining =
            remainingHeightPx(
                viewportEnd = VIEWPORT_END,
                afterContentPadding = BOTTOM_PADDING,
                itemOffset = ITEM_OFFSET,
                scrollOffset = 0,
            )

        assertEquals(VIEWPORT_END - BOTTOM_PADDING - ITEM_OFFSET, remaining)
    }

    @Test
    fun `scrolling does not grow the remaining height`() {
        val atTop = remainingHeightPx(VIEWPORT_END, BOTTOM_PADDING, ITEM_OFFSET, scrollOffset = 0)
        val scrolled =
            remainingHeightPx(VIEWPORT_END, BOTTOM_PADDING, ITEM_OFFSET - SCROLL_OFFSET, SCROLL_OFFSET)

        assertEquals(atTop, scrolled)
    }

    @Test
    fun `remaining height is never negative`() {
        val remaining = remainingHeightPx(VIEWPORT_END, BOTTOM_PADDING, itemOffset = VIEWPORT_END, scrollOffset = 0)

        assertEquals(0, remaining)
    }

    private companion object {
        const val VIEWPORT_END = 2000
        const val BOTTOM_PADDING = 96
        const val ITEM_OFFSET = 900
        const val SCROLL_OFFSET = 120
    }
}
