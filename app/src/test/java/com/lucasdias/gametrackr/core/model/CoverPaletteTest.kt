package com.lucasdias.gametrackr.core.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CoverPaletteTest {
    private val gradientOrder =
        listOf(
            CoverPalette.EMERALD,
            CoverPalette.CRIMSON,
            CoverPalette.INDIGO,
            CoverPalette.AZURE,
            CoverPalette.VIOLET,
            CoverPalette.CYAN,
            CoverPalette.PINE,
        )

    @Test
    fun `gradient ids follow the gradient order`() {
        assertEquals(gradientOrder, (0 until gradientOrder.size).map(CoverPalette::forGradientId))
    }

    @Test
    fun `gradient ids wrap around`() {
        assertEquals(CoverPalette.EMERALD, CoverPalette.forGradientId(gradientOrder.size))
    }

    @Test
    fun `a negative gradient id uses its absolute value`() {
        assertEquals(CoverPalette.forGradientId(NEGATIVE_ID.unaryMinus()), CoverPalette.forGradientId(NEGATIVE_ID))
    }

    @Test
    fun `setup ids follow the case order and wrap for negative ids`() {
        assertEquals(CoverPalette.VIOLET, CoverPalette.forId(0))
        assertEquals(CoverPalette.PINE, CoverPalette.forId(-1))
    }

    private companion object {
        const val NEGATIVE_ID = -3
    }
}
