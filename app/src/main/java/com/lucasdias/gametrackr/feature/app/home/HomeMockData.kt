package com.lucasdias.gametrackr.feature.app.home

import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.CoverCyanEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverCyanStart
import com.lucasdias.gametrackr.core.ui.theme.CoverPineEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverPineStart
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletStart

data class AnticipatedGame(
    val title: String,
    val subtitle: String,
    val badge: String,
    val badgeColor: Color,
    val coverStart: Color,
    val coverEnd: Color,
)

object HomeMockData {
    val mostAnticipated =
        listOf(
            AnticipatedGame(
                title = "GTA VI",
                subtitle = "Coming to Next-Gen Consoles",
                badge = "2025",
                badgeColor = AppPrimary,
                coverStart = CoverVioletStart,
                coverEnd = CoverVioletEnd,
            ),
            AnticipatedGame(
                title = "Hollow Knight: Silksong",
                subtitle = "PC, Switch, Xbox",
                badge = "TBA",
                badgeColor = AppSecondary,
                coverStart = CoverCyanStart,
                coverEnd = CoverCyanEnd,
            ),
            AnticipatedGame(
                title = "Death Stranding 2",
                subtitle = "PS5",
                badge = "2025",
                badgeColor = AppPrimary,
                coverStart = CoverPineStart,
                coverEnd = CoverPineEnd,
            ),
        )
}
