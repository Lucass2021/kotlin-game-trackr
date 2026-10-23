package com.lucasdias.gametrackr.feature.app.gamedetail.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GameAboutLinesTest {
    @Test
    fun `short summary is fully shown without read more`() {
        assertEquals(Int.MAX_VALUE, GameAboutLines.maxLines(expanded = false, collapsible = false))
        assertFalse(GameAboutLines.showsToggle(collapsible = false))
    }

    @Test
    fun `long summary collapses with read more and expands fully`() {
        assertEquals(GameAboutLines.COLLAPSED, GameAboutLines.maxLines(expanded = false, collapsible = true))
        assertEquals(Int.MAX_VALUE, GameAboutLines.maxLines(expanded = true, collapsible = true))
        assertTrue(GameAboutLines.showsToggle(collapsible = true))
    }

    @Test
    fun `unmeasured summary is limited to the fully shown lines without read more`() {
        assertEquals(GameAboutLines.FULLY_SHOWN_MAX, GameAboutLines.maxLines(expanded = false, collapsible = null))
        assertFalse(GameAboutLines.showsToggle(collapsible = null))
    }

    @Test
    fun `expanded but unmeasured summary is limited to the fully shown lines`() {
        assertEquals(GameAboutLines.FULLY_SHOWN_MAX, GameAboutLines.maxLines(expanded = true, collapsible = null))
    }
}
