package com.lucasdias.gametrackr.feature.app.profile.editprofile

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EditProfileUiStateTest {
    private fun state(
        name: String = "Lucas Dias",
        username: String = "lucasdias",
        bio: String = "",
    ) = EditProfileUiState(name = name, username = username, bio = bio)

    @Test
    fun `accepts a filled in profile`() {
        assertTrue(state().canSave)
    }

    @Test
    fun `rejects a name that is empty too short or over the limit`() {
        assertFalse(state(name = "   ").canSave)
        assertFalse(state(name = "Lu").canSave)
        assertFalse(state(name = "a".repeat(EditProfileUiState.NAME_LIMIT + 1)).canSave)
        assertTrue(state(name = "a".repeat(EditProfileUiState.NAME_LIMIT)).canSave)
    }

    @Test
    fun `allows only letters numbers and underscore in the username`() {
        assertFalse(state(username = "lucas dias").canSave)
        assertFalse(state(username = "lucas-dias").canSave)
        assertFalse(state(username = "lucas.dias").canSave)
        assertTrue(state(username = "lucas_dias_02").canSave)
    }

    @Test
    fun `lowercases and trims the username before validating it`() {
        val state = state(username = "  LucasDias  ")

        assertEquals("lucasdias", state.normalizedUsername)
        assertTrue(state.canSave)
    }

    @Test
    fun `enforces the username length bounds`() {
        assertFalse(state(username = "").canSave)
        assertFalse(state(username = "lu").canSave)
        assertFalse(state(username = "a".repeat(EditProfileUiState.USERNAME_LIMIT + 1)).canSave)
        assertTrue(state(username = "a".repeat(EditProfileUiState.USERNAME_LIMIT)).canSave)
    }

    @Test
    fun `caps the bio and counts down the remaining characters`() {
        val atLimit = state(bio = "a".repeat(EditProfileUiState.BIO_LIMIT))
        val overLimit = state(bio = "a".repeat(EditProfileUiState.BIO_LIMIT + 1))

        assertTrue(atLimit.canSave)
        assertEquals(0, atLimit.bioRemaining)
        assertFalse(overLimit.canSave)
        assertEquals(-1, overLimit.bioRemaining)
    }
}
