package com.lucasdias.gametrackr.feature.app.profile.setup

import com.lucasdias.gametrackr.data.profile.ProfileMockData
import org.junit.Assert.assertEquals
import org.junit.Test

class EditSetupTargetTest {
    private val setup = ProfileMockData.setups[0]

    @Test
    fun `no setup id opens a new setup`() {
        assertEquals(EditSetupTarget.New, editSetupTarget(null, null, MySetupUiState()))
    }

    @Test
    fun `a setup id resolves the setup from the list`() {
        val target = editSetupTarget(setup.id, null, MySetupUiState(setups = ProfileMockData.setups))

        assertEquals(EditSetupTarget.Existing(setup), target)
    }

    @Test
    fun `a setup id waits while the list loads`() {
        assertEquals(EditSetupTarget.Loading, editSetupTarget(setup.id, null, MySetupUiState(isLoading = true)))
    }

    @Test
    fun `a setup id that is not in the loaded list is missing`() {
        assertEquals(EditSetupTarget.Missing, editSetupTarget(setup.id, null, MySetupUiState()))
    }

    @Test
    fun `a resolved setup stays open after it leaves the list`() {
        assertEquals(EditSetupTarget.Existing(setup), editSetupTarget(setup.id, setup, MySetupUiState()))
    }
}
