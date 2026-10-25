package com.lucasdias.gametrackr.core.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.darkened
import com.lucasdias.gametrackr.core.ui.theme.toAppColor
import java.time.LocalDate
import kotlin.math.roundToInt

data class Profile(
    val name: String,
    val username: String,
    val bio: String,
    val joinedAt: LocalDate?,
    val avatarHex: String,
    val visibility: ProfileVisibility = ProfileVisibility.PUBLIC,
) {
    val avatarStart: Color get() = avatarHex.toAppColor()
    val avatarEnd: Color get() = avatarHex.toAppColor().darkened(AVATAR_GRADIENT_DEPTH)

    companion object {
        const val DEFAULT_AVATAR_HEX = "#8B5CF6"
        private const val AVATAR_GRADIENT_DEPTH = 0.28f

        fun from(user: User): Profile =
            Profile(
                name = user.name,
                username = user.username?.let { "@$it" }.orEmpty(),
                bio = user.bio.orEmpty(),
                joinedAt = user.createdAt,
                avatarHex = user.profileColor ?: DEFAULT_AVATAR_HEX,
                visibility = user.visibility,
            )
    }
}

enum class ProfileVisibility(
    val apiValue: String,
    val icon: AppIcon,
    @param:StringRes val titleRes: Int,
    @param:StringRes val detailRes: Int,
) {
    PUBLIC(
        "public",
        AppIcon.EYE,
        R.string.edit_profile_visibility_public,
        R.string.edit_profile_visibility_public_detail,
    ),
    PRIVATE(
        "private",
        AppIcon.EYE_SLASH,
        R.string.edit_profile_visibility_private,
        R.string.edit_profile_visibility_private_detail,
    ),
    ;

    companion object {
        fun fromApi(value: String?): ProfileVisibility = entries.firstOrNull { it.apiValue == value } ?: PUBLIC
    }
}

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

data class SetupItem(
    val id: Int,
    val title: String,
    val description: String,
    val images: List<SetupImage> = emptyList(),
    val palette: SetupPalette = SetupPalette.forId(id),
)

data class SetupImage(
    val id: Int,
    val url: String,
)

class SetupImageUpload(
    val bytes: ByteArray,
    val fileName: String,
    val mimeType: String,
)

data class ProfileOverview(
    val stats: ProfileStats,
    val breakdown: List<StatusCount>,
    val favorites: List<LibraryEntry>,
    val currentlyPlaying: List<LibraryEntry>,
    val activity: List<ActivityEvent>,
)

data class UserOverview(
    val name: String,
    val username: String,
    val avatarHex: String,
    val bio: String,
    val joinedAt: LocalDate?,
    val isFriend: Boolean,
    val overview: ProfileOverview,
)
