package com.lucasdias.gametrackr.feature.app.profile

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.feature.app.library.LibraryStatus
import kotlin.math.roundToInt

data class Profile(
    val name: String,
    val username: String,
    val bio: String,
    val joinedAt: String,
    val avatarStart: Color,
    val avatarEnd: Color,
    val stats: ProfileStats,
)

data class ProfileStats(
    val totalGames: Int,
    val hours: Int,
    val platinum: Int,
) {
    val platinumRate: Int
        get() = if (totalGames == 0) 0 else (platinum.toDouble() / totalGames * 100).roundToInt()
}

data class StatusCount(
    val status: LibraryStatus,
    val count: Int,
)

val LibraryStatus.barTint: Color
    get() =
        when (this) {
            LibraryStatus.PLAYING -> AppPrimary
            LibraryStatus.COMPLETED -> AppSecondary
            LibraryStatus.BACKLOG -> AppTextSecondary
            LibraryStatus.PLATINUM -> AppTextPrimary
            LibraryStatus.ABANDONED -> AppTertiary
            LibraryStatus.WISHLIST -> AppSecondary.copy(alpha = 0.55f)
        }

enum class ActivityKind(
    val icon: AppIcon,
    @param:StringRes val verbRes: Int,
) {
    PLATINUM(AppIcon.MEDAL, R.string.profile_activity_platinum),
    COMPLETED(AppIcon.SUCCESS, R.string.profile_activity_completed),
    STARTED(AppIcon.BRAND, R.string.profile_activity_started),
    RATED(AppIcon.LIKE, R.string.profile_activity_rated),
    ADDED(AppIcon.ADD_TO_LIBRARY, R.string.profile_activity_added),
    ;

    val tint: Color
        get() =
            when (this) {
                PLATINUM, STARTED -> AppPrimary
                COMPLETED -> AppSecondary
                RATED -> AppTertiary
                ADDED -> AppTextSecondary
            }
}

data class ActivityEvent(
    val id: Long,
    val kind: ActivityKind,
    val gameTitle: String,
    val detail: String? = null,
    val timeAgo: String,
)
