package com.lucasdias.gametrackr.core.model

import kotlin.math.abs

enum class CoverPalette {
    VIOLET,
    EMERALD,
    CRIMSON,
    INDIGO,
    AZURE,
    CYAN,
    PINE,
    ;

    val title: String get() = name.lowercase().replaceFirstChar { it.uppercase() }

    companion object {
        private val gradientOrder = listOf(EMERALD, CRIMSON, INDIGO, AZURE, VIOLET, CYAN, PINE)

        fun forId(id: Int): CoverPalette = entries[id.mod(entries.size)]

        fun forGradientId(id: Int): CoverPalette = gradientOrder[abs(id) % gradientOrder.size]
    }
}
