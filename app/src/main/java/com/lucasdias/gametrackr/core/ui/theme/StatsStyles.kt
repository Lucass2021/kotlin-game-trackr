package com.lucasdias.gametrackr.core.ui.theme

import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.core.model.StatAccent
import com.lucasdias.gametrackr.core.model.StatBar
import com.lucasdias.gametrackr.core.model.YearCount

private const val PRIMARY_MUTED_ALPHA = 0.5f
private const val SECONDARY_MUTED_ALPHA = 0.45f

val StatAccent.color: Color
    get() =
        when (this) {
            StatAccent.PRIMARY -> AppPrimary
            StatAccent.SECONDARY -> AppSecondary
            StatAccent.PRIMARY_MUTED -> AppPrimary.copy(alpha = PRIMARY_MUTED_ALPHA)
            StatAccent.SECONDARY_MUTED -> AppSecondary.copy(alpha = SECONDARY_MUTED_ALPHA)
        }

val StatBar.tint: Color get() = accent.color

val YearCount.tint: Color get() = accent.color
