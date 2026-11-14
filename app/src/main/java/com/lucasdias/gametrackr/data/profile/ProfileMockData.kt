package com.lucasdias.gametrackr.data.profile

import com.lucasdias.gametrackr.core.model.ActivityEvent
import com.lucasdias.gametrackr.core.model.ActivityKind
import com.lucasdias.gametrackr.core.model.CoverPalette
import com.lucasdias.gametrackr.core.model.LibraryEntry
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.model.Profile
import com.lucasdias.gametrackr.core.model.ProfileColor
import com.lucasdias.gametrackr.core.model.ProfileOverview
import com.lucasdias.gametrackr.core.model.ProfileStats
import com.lucasdias.gametrackr.core.model.SetupItem
import com.lucasdias.gametrackr.core.model.StatusCount
import com.lucasdias.gametrackr.core.model.User
import java.time.LocalDate

object ProfileMockData {
    val stats = ProfileStats(totalGames = 142, hours = 1248, platinum = 21)

    val profile =
        Profile(
            name = "Lucas Dias",
            username = "@lucasdias",
            bio = "Backlog eterno, platina ocasional. RPGs de turno e speedruns de fim de semana.",
            joinedAt = LocalDate.of(2024, 3, 15),
            avatarHex = "#8B5CF6",
        )

    val user =
        User(
            id = 1,
            name = profile.name,
            email = "player@example.com",
            username = profile.username.removePrefix("@"),
            profileColor = profile.avatarHex,
            bio = profile.bio,
            createdAt = profile.joinedAt,
        )

    val colors =
        listOf(
            ProfileColor(key = "purple", name = "Purple", hex = "#8B5CF6"),
            ProfileColor(key = "emerald", name = "Emerald", hex = "#10B981"),
            ProfileColor(key = "amber", name = "Amber", hex = "#F59E0B"),
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
            LibraryEntry("Elden Ring", "elden-ring", LibraryStatus.PLATINUM, 5, 210, CoverPalette.EMERALD),
            LibraryEntry("Hollow Knight", "hollow-knight", LibraryStatus.COMPLETED, 5, 64, CoverPalette.INDIGO),
            LibraryEntry("Chrono Trigger", "chrono-trigger", LibraryStatus.PLATINUM, 5, 38, CoverPalette.CRIMSON),
            LibraryEntry("Outer Wilds", "outer-wilds", LibraryStatus.COMPLETED, 5, 27, CoverPalette.CYAN),
        )

    val currentlyPlaying =
        listOf(
            LibraryEntry(
                "Cyberpunk 2077: Phantom Liberty",
                "cyberpunk-2077-phantom-liberty",
                LibraryStatus.PLAYING,
                4,
                32,
                CoverPalette.VIOLET,
            ),
            LibraryEntry("Final Fantasy VII Rebirth", "final-fantasy-vii-rebirth", LibraryStatus.PLAYING, 5, 61, CoverPalette.AZURE),
        )

    val activity =
        listOf(
            ActivityEvent(1, ActivityKind.PLATINUM, "Uncharted: Legacy of Thieves", timeAgo = "2d"),
            ActivityEvent(2, ActivityKind.RATED, "Starfield", detail = "4 stars", timeAgo = "4d"),
            ActivityEvent(3, ActivityKind.COMPLETED, "Hollow Knight", timeAgo = "1w"),
            ActivityEvent(4, ActivityKind.STARTED, "Final Fantasy VII Rebirth", timeAgo = "2w"),
            ActivityEvent(5, ActivityKind.ADDED, "Silksong", timeAgo = "3w"),
        )

    val setups =
        listOf(
            SetupItem(
                id = 1,
                title = "Main Battle Station",
                description = "RTX 4090 + i9-14900K, 32\" 4K 144Hz",
                palette = CoverPalette.INDIGO,
            ),
            SetupItem(
                id = 2,
                title = "Retro Corner",
                description = "CRT TV + SNES, N64, and PS2",
                palette = CoverPalette.CRIMSON,
            ),
            SetupItem(
                id = 3,
                title = "Portable Setup",
                description = "Steam Deck OLED + Backbone",
                palette = CoverPalette.EMERALD,
            ),
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
