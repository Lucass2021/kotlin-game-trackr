package com.lucasdias.gametrackr.core.ui.theme

import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.core.model.CoverPalette
import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.model.GameAchievements
import com.lucasdias.gametrackr.core.model.GameDetail
import com.lucasdias.gametrackr.core.model.GameScreenshot
import com.lucasdias.gametrackr.core.model.LibraryEntry
import com.lucasdias.gametrackr.core.model.NewLibraryEntry

val CoverPalette.start: Color
    get() =
        when (this) {
            CoverPalette.VIOLET -> CoverVioletStart
            CoverPalette.EMERALD -> CoverEmeraldStart
            CoverPalette.CRIMSON -> CoverCrimsonStart
            CoverPalette.INDIGO -> CoverIndigoStart
            CoverPalette.AZURE -> CoverAzureStart
            CoverPalette.CYAN -> CoverCyanStart
            CoverPalette.PINE -> CoverPineStart
        }

val CoverPalette.end: Color
    get() =
        when (this) {
            CoverPalette.VIOLET -> CoverVioletEnd
            CoverPalette.EMERALD -> CoverEmeraldEnd
            CoverPalette.CRIMSON -> CoverCrimsonEnd
            CoverPalette.INDIGO -> CoverIndigoEnd
            CoverPalette.AZURE -> CoverAzureEnd
            CoverPalette.CYAN -> CoverCyanEnd
            CoverPalette.PINE -> CoverPineEnd
        }

val Game.coverStart: Color get() = cover.start

val Game.coverEnd: Color get() = cover.end

val GameDetail.coverStart: Color get() = cover.start

val GameDetail.coverEnd: Color get() = cover.end

val GameScreenshot.start: Color get() = cover.start

val GameScreenshot.end: Color get() = cover.end

val LibraryEntry.coverStart: Color get() = cover.start

val LibraryEntry.coverEnd: Color get() = cover.end

val NewLibraryEntry.coverStart: Color get() = cover.start

val NewLibraryEntry.coverEnd: Color get() = cover.end

val GameAchievements.coverStart: Color get() = cover.start

val GameAchievements.coverEnd: Color get() = cover.end
