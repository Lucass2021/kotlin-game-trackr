package com.lucasdias.gametrackr.feature.app.profilemenu

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.text.UiText

data class ProfileMenuUiState(
    val isGuest: Boolean = false,
    val name: UiText = UiText.Raw(""),
    val subtitle: UiText = UiText.Raw(""),
) {
    val accountItems: List<ProfileMenuItem>
        get() =
            if (isGuest) {
                emptyList()
            } else {
                listOf(ProfileMenuItem.EDIT_PROFILE, ProfileMenuItem.STATS, ProfileMenuItem.ACHIEVEMENTS)
            }

    val appItems: List<ProfileMenuItem>
        get() =
            if (isGuest) {
                listOf(ProfileMenuItem.HELP, ProfileMenuItem.ABOUT)
            } else {
                listOf(ProfileMenuItem.SETTINGS, ProfileMenuItem.HELP, ProfileMenuItem.ABOUT)
            }

    companion object {
        fun from(
            isGuest: Boolean,
            userName: String?,
            email: String?,
        ): ProfileMenuUiState =
            if (isGuest) {
                ProfileMenuUiState(
                    isGuest = true,
                    name = UiText.Resource(R.string.menu_guest_name),
                    subtitle = UiText.Resource(R.string.menu_not_signed_in),
                )
            } else {
                ProfileMenuUiState(
                    name = UiText.Raw(userName?.trim().orEmpty()),
                    subtitle = UiText.Raw(email?.trim().orEmpty()),
                )
            }
    }
}
