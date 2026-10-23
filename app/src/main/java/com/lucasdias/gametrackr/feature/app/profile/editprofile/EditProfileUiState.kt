package com.lucasdias.gametrackr.feature.app.profile.editprofile

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Profile
import com.lucasdias.gametrackr.core.model.ProfileColor
import com.lucasdias.gametrackr.core.model.ProfileVisibility
import com.lucasdias.gametrackr.core.ui.text.UiText

data class EditProfileUiState(
    val original: Profile,
    val name: String = original.name,
    val username: String = original.username.removePrefix(USERNAME_PREFIX),
    val bio: String = original.bio,
    val avatarHex: String = original.avatarHex,
    val visibility: ProfileVisibility = original.visibility,
    val colors: List<ProfileColor> = emptyList(),
    val isLoadingColors: Boolean = false,
    val colorsFailed: Boolean = false,
    val isSubmitted: Boolean = false,
    val isSaving: Boolean = false,
    val savedProfile: Profile? = null,
    val toastMessage: UiText? = null,
) {
    val trimmedName: String get() = name.trim()

    val normalizedUsername: String get() = username.trim().lowercase()

    val trimmedBio: String get() = bio.trim()

    val bioRemaining: Int get() = BIO_LIMIT - bio.length

    val nameError: UiText? get() = if (isSubmitted) nameErrorFor(trimmedName) else null

    val usernameError: UiText? get() = if (isSubmitted) usernameErrorFor(normalizedUsername) else null

    val bioError: UiText? get() = if (isSubmitted) bioErrorFor(trimmedBio) else null

    val canSave: Boolean
        get() = nameErrorFor(trimmedName) == null && usernameErrorFor(normalizedUsername) == null && bioErrorFor(trimmedBio) == null

    val hasChanges: Boolean
        get() =
            trimmedName != original.name ||
                formattedUsername != original.username ||
                trimmedBio != original.bio ||
                avatarHex != original.avatarHex ||
                visibility != original.visibility

    val previewName: String get() = trimmedName.ifEmpty { original.name }

    val previewUsername: String get() = if (normalizedUsername.isEmpty()) original.username else formattedUsername

    fun updatedProfile(): Profile =
        original.copy(
            name = trimmedName,
            username = formattedUsername,
            bio = trimmedBio,
            avatarHex = avatarHex,
            visibility = visibility,
        )

    private val formattedUsername: String get() = "$USERNAME_PREFIX$normalizedUsername"

    companion object {
        const val NAME_MIN = 3
        const val NAME_LIMIT = 50
        const val USERNAME_MIN = 3
        const val USERNAME_LIMIT = 20
        const val BIO_LIMIT = 1000
        const val USERNAME_PREFIX = "@"
        private const val USERNAME_ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789_"

        private fun nameErrorFor(name: String): UiText? =
            when {
                name.isEmpty() -> UiText.Resource(R.string.validation_display_name_required)
                name.length < NAME_MIN -> UiText.Resource(R.string.validation_name_too_short)
                name.length > NAME_LIMIT -> UiText.Resource(R.string.validation_name_too_long, listOf(NAME_LIMIT))
                else -> null
            }

        private fun usernameErrorFor(username: String): UiText? =
            when {
                username.isEmpty() -> UiText.Resource(R.string.validation_username_required)
                username.length < USERNAME_MIN -> UiText.Resource(R.string.validation_username_too_short)
                username.length > USERNAME_LIMIT -> UiText.Resource(R.string.validation_username_too_long, listOf(USERNAME_LIMIT))
                username.any { it !in USERNAME_ALPHABET } -> UiText.Resource(R.string.validation_username_invalid)
                else -> null
            }

        private fun bioErrorFor(bio: String): UiText? =
            if (bio.length > BIO_LIMIT) UiText.Resource(R.string.validation_bio_too_long, listOf(BIO_LIMIT)) else null
    }
}
