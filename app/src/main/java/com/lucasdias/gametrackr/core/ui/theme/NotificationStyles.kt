package com.lucasdias.gametrackr.core.ui.theme

import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.core.model.NotificationIconKind
import com.lucasdias.gametrackr.core.ui.icon.AppIcon

val NotificationIconKind.icon: AppIcon
    get() =
        when (this) {
            NotificationIconKind.LIKE -> AppIcon.LIKE
            NotificationIconKind.ACHIEVEMENT -> AppIcon.MEDAL
            NotificationIconKind.MESSAGE -> AppIcon.ENVELOPE
        }

val NotificationIconKind.tint: Color
    get() =
        when (this) {
            NotificationIconKind.LIKE, NotificationIconKind.ACHIEVEMENT -> AppPrimary
            NotificationIconKind.MESSAGE -> AppTextSecondary
        }
