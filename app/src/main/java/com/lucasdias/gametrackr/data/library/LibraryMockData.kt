package com.lucasdias.gametrackr.data.library

import com.lucasdias.gametrackr.core.model.CoverPalette
import com.lucasdias.gametrackr.core.model.LibraryEntry
import com.lucasdias.gametrackr.core.model.LibraryStats
import com.lucasdias.gametrackr.core.model.LibraryStatus

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
                cover = CoverPalette.VIOLET,
            ),
            LibraryEntry(
                title = "Starfield",
                slug = "starfield",
                status = LibraryStatus.COMPLETED,
                rating = 5,
                hours = 128,
                cover = CoverPalette.INDIGO,
            ),
            LibraryEntry(
                title = "Elden Ring",
                slug = "elden-ring",
                status = LibraryStatus.BACKLOG,
                rating = 0,
                hours = 0,
                cover = CoverPalette.CRIMSON,
            ),
            LibraryEntry(
                title = "Uncharted: Legacy of Thieves",
                slug = "uncharted-legacy-of-thieves-collection",
                status = LibraryStatus.PLATINUM,
                rating = 5,
                hours = 45,
                cover = CoverPalette.PINE,
            ),
            LibraryEntry(
                title = "Forza Motorsport",
                slug = "forza-motorsport--1",
                status = LibraryStatus.PLAYING,
                rating = 4,
                hours = 12,
                cover = CoverPalette.AZURE,
            ),
        )
}
