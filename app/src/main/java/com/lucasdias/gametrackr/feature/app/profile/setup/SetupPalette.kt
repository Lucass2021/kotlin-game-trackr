package com.lucasdias.gametrackr.feature.app.profile.setup

import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.core.ui.theme.CoverAzureEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverAzureStart
import com.lucasdias.gametrackr.core.ui.theme.CoverCrimsonEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverCrimsonStart
import com.lucasdias.gametrackr.core.ui.theme.CoverCyanEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverCyanStart
import com.lucasdias.gametrackr.core.ui.theme.CoverEmeraldEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverEmeraldStart
import com.lucasdias.gametrackr.core.ui.theme.CoverIndigoEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverIndigoStart
import com.lucasdias.gametrackr.core.ui.theme.CoverPineEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverPineStart
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletStart

enum class SetupPalette(
    val start: Color,
    val end: Color,
) {
    VIOLET(CoverVioletStart, CoverVioletEnd),
    EMERALD(CoverEmeraldStart, CoverEmeraldEnd),
    CRIMSON(CoverCrimsonStart, CoverCrimsonEnd),
    INDIGO(CoverIndigoStart, CoverIndigoEnd),
    AZURE(CoverAzureStart, CoverAzureEnd),
    CYAN(CoverCyanStart, CoverCyanEnd),
    PINE(CoverPineStart, CoverPineEnd),
    ;

    val title: String get() = name.lowercase().replaceFirstChar { it.uppercase() }

    companion object {
        fun matching(
            start: Color,
            end: Color,
        ): SetupPalette = entries.firstOrNull { it.start == start && it.end == end } ?: VIOLET
    }
}
