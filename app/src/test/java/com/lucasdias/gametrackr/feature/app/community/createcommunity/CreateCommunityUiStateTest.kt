package com.lucasdias.gametrackr.feature.app.community.createcommunity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CreateCommunityUiStateTest {
    private val validDescription = "A club for retro shoot-em-ups."

    @Test
    fun `derives the handle the backend will actually store`() {
        val state = CreateCommunityUiState(name = "Retro Shmup Club")

        assertEquals("RetroShmupClub", state.handle)
        assertTrue(state.isRenamed)
    }

    @Test
    fun `does not flag a rename when the name has no whitespace`() {
        assertFalse(CreateCommunityUiState(name = "RetroShmupClub").isRenamed)
    }

    @Test
    fun `does not flag a rename on an empty name`() {
        val state = CreateCommunityUiState(name = "   ")

        assertTrue(state.handle.isEmpty())
        assertFalse(state.isRenamed)
    }

    @Test
    fun `validates the handle length not the typed length`() {
        val typedThree = CreateCommunityUiState(name = "a b", description = validDescription)

        assertEquals(3, typedThree.name.length)
        assertFalse(typedThree.canSubmit)

        assertTrue(CreateCommunityUiState(name = "a b c", description = validDescription).canSubmit)
    }

    @Test
    fun `requires a description of at least ten characters`() {
        val name = "RetroShmupClub"

        assertFalse(CreateCommunityUiState(name = name).canSubmit)
        assertFalse(CreateCommunityUiState(name = name, description = "  \n ").canSubmit)
        assertFalse(CreateCommunityUiState(name = name, description = "Too short").canSubmit)
        assertTrue(CreateCommunityUiState(name = name, description = validDescription).canSubmit)
    }

    @Test
    fun `blocks a second submit while the first is still in flight`() {
        val state =
            CreateCommunityUiState(
                name = "RetroShmupClub",
                description = validDescription,
                isSubmitting = true,
            )

        assertFalse(state.canSubmit)
    }

    @Test
    fun `counts the remaining characters against the handle`() {
        val state = CreateCommunityUiState(name = "Retro Shmup Club")

        assertEquals(CreateCommunityUiState.NAME_LIMIT - "RetroShmupClub".length, state.nameRemaining)
    }

    @Test
    fun `considers the form dirty as soon as either field has content`() {
        assertFalse(CreateCommunityUiState().hasContent)
        assertFalse(CreateCommunityUiState(name = "   ").hasContent)
        assertTrue(CreateCommunityUiState(name = "A").hasContent)
        assertTrue(CreateCommunityUiState(description = "A").hasContent)
    }
}
