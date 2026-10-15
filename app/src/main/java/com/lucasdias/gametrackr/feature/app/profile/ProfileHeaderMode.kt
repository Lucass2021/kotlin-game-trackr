package com.lucasdias.gametrackr.feature.app.profile

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.User
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.darkened
import com.lucasdias.gametrackr.core.ui.theme.toAppColor
import java.util.UUID
import kotlin.math.roundToInt

sealed interface ProfileHeaderMode {
    data object Own : ProfileHeaderMode

    data class Other(
        val isFriend: Boolean,
    ) : ProfileHeaderMode
}
