package com.lucasdias.gametrackr.feature.app.profile

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
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletStart
import com.lucasdias.gametrackr.feature.app.library.LibraryEntry
import com.lucasdias.gametrackr.feature.app.library.LibraryStatus

object ProfileMockData {
    val profile =
        Profile(
            name = "Lucas Dias",
            username = "@lucasdias",
            bio = "Backlog eterno, platina ocasional. RPGs de turno e speedruns de fim de semana.",
            joinedAt = "Joined March 2024",
            avatarStart = CoverVioletStart,
            avatarEnd = CoverVioletEnd,
            stats = ProfileStats(totalGames = 142, hours = 1248, platinum = 21),
        )

    val breakdown =
        listOf(
            StatusCount(LibraryStatus.PLAYING, 8),
            StatusCount(LibraryStatus.COMPLETED, 47),
            StatusCount(LibraryStatus.BACKLOG, 62),
            StatusCount(LibraryStatus.PLATINUM, 21),
            StatusCount(LibraryStatus.ABANDONED, 4),
        )

    val favorites =
        listOf(
            LibraryEntry("Elden Ring", LibraryStatus.PLATINUM, 5, 210, CoverEmeraldStart, CoverEmeraldEnd),
            LibraryEntry("Hollow Knight", LibraryStatus.COMPLETED, 5, 64, CoverIndigoStart, CoverIndigoEnd),
            LibraryEntry("Chrono Trigger", LibraryStatus.PLATINUM, 5, 38, CoverCrimsonStart, CoverCrimsonEnd),
            LibraryEntry("Outer Wilds", LibraryStatus.COMPLETED, 5, 27, CoverCyanStart, CoverCyanEnd),
        )

    val currentlyPlaying =
        listOf(
            LibraryEntry(
                "Cyberpunk 2077: Phantom Liberty",
                LibraryStatus.PLAYING,
                4,
                32,
                CoverVioletStart,
                CoverVioletEnd,
            ),
            LibraryEntry("Final Fantasy VII Rebirth", LibraryStatus.PLAYING, 5, 61, CoverAzureStart, CoverAzureEnd),
        )

    val activity =
        listOf(
            ActivityEvent(1, ActivityKind.PLATINUM, "Uncharted: Legacy of Thieves", timeAgo = "2d"),
            ActivityEvent(2, ActivityKind.RATED, "Starfield", detail = "4 stars", timeAgo = "4d"),
            ActivityEvent(3, ActivityKind.COMPLETED, "Hollow Knight", timeAgo = "1w"),
            ActivityEvent(4, ActivityKind.STARTED, "Final Fantasy VII Rebirth", timeAgo = "2w"),
            ActivityEvent(5, ActivityKind.ADDED, "Silksong", timeAgo = "3w"),
        )
}
