package com.lucasdias.gametrackr.data.library

import com.lucasdias.gametrackr.core.model.LibraryEntry
import com.lucasdias.gametrackr.core.model.LibraryStats
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.ui.theme.CoverAzureEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverAzureStart
import com.lucasdias.gametrackr.core.ui.theme.CoverCrimsonEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverCrimsonStart
import com.lucasdias.gametrackr.core.ui.theme.CoverIndigoEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverIndigoStart
import com.lucasdias.gametrackr.core.ui.theme.CoverPineEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverPineStart
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletStart

object LibraryMockData {
    val stats = LibraryStats(totalGames = 142, platinum = 21)

    val entries =
        listOf(
            LibraryEntry(
                title = "Cyberpunk 2077: Phantom Liberty",
                slug = "cyberpunk-2077-phantom-liberty",
                status = LibraryStatus.PLAYING,
                rating = 4,
                hours = 32,
                coverStart = CoverVioletStart,
                coverEnd = CoverVioletEnd,
            ),
            LibraryEntry(
                title = "Starfield",
                slug = "starfield",
                status = LibraryStatus.COMPLETED,
                rating = 5,
                hours = 128,
                coverStart = CoverIndigoStart,
                coverEnd = CoverIndigoEnd,
            ),
            LibraryEntry(
                title = "Elden Ring",
                slug = "elden-ring",
                status = LibraryStatus.BACKLOG,
                rating = 0,
                hours = 0,
                coverStart = CoverCrimsonStart,
                coverEnd = CoverCrimsonEnd,
            ),
            LibraryEntry(
                title = "Uncharted: Legacy of Thieves",
                slug = "uncharted-legacy-of-thieves-collection",
                status = LibraryStatus.PLATINUM,
                rating = 5,
                hours = 45,
                coverStart = CoverPineStart,
                coverEnd = CoverPineEnd,
            ),
            LibraryEntry(
                title = "Forza Motorsport",
                slug = "forza-motorsport--1",
                status = LibraryStatus.PLAYING,
                rating = 4,
                hours = 12,
                coverStart = CoverAzureStart,
                coverEnd = CoverAzureEnd,
            ),
        )
}
