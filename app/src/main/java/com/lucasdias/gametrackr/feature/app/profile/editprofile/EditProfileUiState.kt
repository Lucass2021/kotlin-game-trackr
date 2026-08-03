package com.lucasdias.gametrackr.feature.app.profile.editprofile

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.feature.app.profile.ProfileVisibility

data class EditProfileUiState(
    val name: String = "",
    val username: String = "",
    val bio: String = "",
    val palette: AvatarPalette = AvatarPalette.VIOLET,
    val visibility: ProfileVisibility = ProfileVisibility.PUBLIC,
    @param:StringRes val nameError: Int? = null,
    @param:StringRes val usernameError: Int? = null,
    @param:StringRes val bioError: Int? = null,
) {
    val bioRemaining: Int get() = BIO_LIMIT - bio.length

    val canSave: Boolean
        get() =
            name.trim().length in 3..NAME_LIMIT &&
                normalizedUsername.length in 3..USERNAME_LIMIT &&
                normalizedUsername.all { it in USERNAME_ALPHABET } &&
                bio.trim().length <= BIO_LIMIT

    val normalizedUsername: String get() = username.trim().lowercase()

    companion object {
        const val NAME_LIMIT = 50
        const val USERNAME_LIMIT = 20
        const val BIO_LIMIT = 160
        const val USERNAME_ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789_"
    }
}
