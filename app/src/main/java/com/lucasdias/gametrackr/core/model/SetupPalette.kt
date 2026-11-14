package com.lucasdias.gametrackr.core.model

enum class SetupPalette {
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
        fun forId(id: Int): SetupPalette = entries[id.mod(entries.size)]
    }
}
