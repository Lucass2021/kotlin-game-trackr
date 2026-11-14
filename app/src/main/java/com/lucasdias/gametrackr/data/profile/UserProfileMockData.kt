package com.lucasdias.gametrackr.data.profile

import com.lucasdias.gametrackr.core.model.ActivityEvent
import com.lucasdias.gametrackr.core.model.ActivityKind
import com.lucasdias.gametrackr.core.model.CoverPalette
import com.lucasdias.gametrackr.core.model.LibraryEntry
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.model.ProfileOverview
import com.lucasdias.gametrackr.core.model.ProfileStats
import com.lucasdias.gametrackr.core.model.StatusCount
import java.time.LocalDate

object UserProfileMockData {
    const val USERNAME = "avareyes"

    const val AVATAR_HEX = "#0077B6"

    val names =
        mapOf(
            USERNAME to "Ava Reyes",
            "shadowreaper" to "ShadowReaper",
            "pixelwitcher" to "PixelWitcher",
            "retrogamer" to "RetroGamer",
            "chrono_runner" to "chrono_runner",
            "master_of_none" to "master_of_none",
            "questmaster" to "QuestMaster",
            "voidwalker" to "VoidWalker",
            "neonsamurai" to "NeonSamurai",
        )

    val stats = ProfileStats(totalGames = 87, hours = 934, platinum = 12)

    const val BIO = "Co-op enjoyer and lore hunter. Always down for a raid night or a slow-burn RPG."

    val joinedAt: LocalDate = LocalDate.of(2024, 1, 15)

    val breakdown =
        listOf(
            StatusCount(LibraryStatus.PLAYING, 5),
            StatusCount(LibraryStatus.COMPLETED, 31),
            StatusCount(LibraryStatus.BACKLOG, 39),
            StatusCount(LibraryStatus.PLATINUM, 12),
            StatusCount(LibraryStatus.ABANDONED, 3),
        )

    val favorites =
        listOf(
            LibraryEntry("The Witcher 3", "the-witcher-3-wild-hunt", LibraryStatus.PLATINUM, 5, 168, CoverPalette.PINE),
            LibraryEntry("Bloodborne", "bloodborne", LibraryStatus.COMPLETED, 5, 72, CoverPalette.CRIMSON),
            LibraryEntry("Celeste", "celeste", LibraryStatus.PLATINUM, 5, 24, CoverPalette.AZURE),
            LibraryEntry("Disco Elysium", "disco-elysium", LibraryStatus.COMPLETED, 5, 41, CoverPalette.INDIGO),
        )

    val currentlyPlaying =
        listOf(
            LibraryEntry("Baldur's Gate 3", "baldurs-gate-iii", LibraryStatus.PLAYING, 5, 96, CoverPalette.EMERALD),
            LibraryEntry("Helldivers 2", "helldivers-2", LibraryStatus.PLAYING, 4, 44, CoverPalette.CYAN),
        )

    val activity =
        listOf(
            ActivityEvent(1, ActivityKind.COMPLETED, "Baldur's Gate 3", timeAgo = "1d"),
            ActivityEvent(2, ActivityKind.PLATINUM, "Celeste", timeAgo = "5d"),
            ActivityEvent(3, ActivityKind.RATED, "Helldivers 2", detail = "4 stars", timeAgo = "1w"),
            ActivityEvent(4, ActivityKind.STARTED, "Baldur's Gate 3", timeAgo = "2w"),
            ActivityEvent(5, ActivityKind.ADDED, "Metaphor: ReFantazio", timeAgo = "3w"),
        )

    val overview =
        ProfileOverview(
            stats = stats,
            breakdown = breakdown,
            favorites = favorites,
            currentlyPlaying = currentlyPlaying,
            activity = activity,
        )
}
