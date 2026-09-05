package com.lucasdias.gametrackr.feature.app.profile

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
import com.lucasdias.gametrackr.core.ui.theme.toHexString
import com.lucasdias.gametrackr.feature.app.library.LibraryEntry
import com.lucasdias.gametrackr.feature.app.library.LibraryStatus
import kotlin.math.abs

data class UserProfile(
    val name: String,
    val username: String,
    val avatarStart: Color,
    val avatarEnd: Color,
    val isFriend: Boolean = false,
)

object UserProfileMockData {
    private val avatarPalette =
        listOf(
            CoverCyanStart to CoverCyanEnd,
            CoverVioletStart to CoverVioletEnd,
            CoverEmeraldStart to CoverEmeraldEnd,
            CoverCrimsonStart to CoverCrimsonEnd,
            CoverAzureStart to CoverAzureEnd,
            CoverIndigoStart to CoverIndigoEnd,
        )

    fun forName(raw: String): UserProfile {
        val handle = raw.trim()
        val hasAt = handle.startsWith("@")
        val displayName = if (hasAt) handle.drop(1) else handle
        val username = if (hasAt) handle else "@" + handle.lowercase().replace(" ", "")
        val (start, end) = avatarPalette[abs(handle.hashCode()) % avatarPalette.size]
        return UserProfile(
            name = displayName,
            username = username,
            avatarStart = start,
            avatarEnd = end,
        )
    }

    fun profile(user: UserProfile): Profile =
        Profile(
            name = user.name,
            username = user.username,
            bio = "Co-op enjoyer and lore hunter. Always down for a raid night or a slow-burn RPG.",
            joinedAt = "Joined January 2024",
            avatarHex = user.avatarStart.toHexString(),
            stats = ProfileStats(totalGames = 87, hours = 934, platinum = 12),
        )

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
            LibraryEntry("The Witcher 3", LibraryStatus.PLATINUM, 5, 168, CoverPineStart, CoverPineEnd),
            LibraryEntry("Bloodborne", LibraryStatus.COMPLETED, 5, 72, CoverCrimsonStart, CoverCrimsonEnd),
            LibraryEntry("Celeste", LibraryStatus.PLATINUM, 5, 24, CoverAzureStart, CoverAzureEnd),
            LibraryEntry("Disco Elysium", LibraryStatus.COMPLETED, 5, 41, CoverIndigoStart, CoverIndigoEnd),
        )

    val currentlyPlaying =
        listOf(
            LibraryEntry("Baldur's Gate 3", LibraryStatus.PLAYING, 5, 96, CoverEmeraldStart, CoverEmeraldEnd),
            LibraryEntry("Helldivers 2", LibraryStatus.PLAYING, 4, 44, CoverCyanStart, CoverCyanEnd),
        )

    val activity =
        listOf(
            ActivityEvent(1, ActivityKind.COMPLETED, "Baldur's Gate 3", timeAgo = "1d"),
            ActivityEvent(2, ActivityKind.PLATINUM, "Celeste", timeAgo = "5d"),
            ActivityEvent(3, ActivityKind.RATED, "Helldivers 2", detail = "4 stars", timeAgo = "1w"),
            ActivityEvent(4, ActivityKind.STARTED, "Baldur's Gate 3", timeAgo = "2w"),
            ActivityEvent(5, ActivityKind.ADDED, "Metaphor: ReFantazio", timeAgo = "3w"),
        )
}
