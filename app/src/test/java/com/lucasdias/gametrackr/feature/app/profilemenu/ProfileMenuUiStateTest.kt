package com.lucasdias.gametrackr.feature.app.profilemenu

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.text.UiText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProfileMenuUiStateTest {
    @Test
    fun `a signed in user shows their name, e-mail and every item`() {
        val state = ProfileMenuUiState.from(isGuest = false, userName = NAME, email = EMAIL)

        assertEquals(UiText.Raw(NAME), state.name)
        assertEquals(UiText.Raw(EMAIL), state.subtitle)
        assertEquals(
            listOf(ProfileMenuItem.EDIT_PROFILE, ProfileMenuItem.STATS, ProfileMenuItem.ACHIEVEMENTS),
            state.accountItems,
        )
        assertEquals(listOf(ProfileMenuItem.SETTINGS, ProfileMenuItem.HELP, ProfileMenuItem.ABOUT), state.appItems)
    }

    @Test
    fun `a guest sees the guest header and only help and about`() {
        val state = ProfileMenuUiState.from(isGuest = true, userName = NAME, email = EMAIL)

        assertEquals(UiText.Resource(R.string.menu_guest_name), state.name)
        assertEquals(UiText.Resource(R.string.menu_not_signed_in), state.subtitle)
        assertTrue(state.accountItems.isEmpty())
        assertEquals(listOf(ProfileMenuItem.HELP, ProfileMenuItem.ABOUT), state.appItems)
    }

    @Test
    fun `a signed in user not loaded yet shows no mock name`() {
        val state = ProfileMenuUiState.from(isGuest = false, userName = null, email = null)

        assertEquals(UiText.Raw(""), state.name)
        assertEquals(UiText.Raw(""), state.subtitle)
    }

    @Test
    fun `a blank name or e-mail is shown as empty`() {
        val state = ProfileMenuUiState.from(isGuest = false, userName = "  ", email = " ")

        assertEquals(UiText.Raw(""), state.name)
        assertEquals(UiText.Raw(""), state.subtitle)
    }

    private companion object {
        const val NAME = "Ada Lovelace"
        const val EMAIL = "ada@example.com"
    }
}
