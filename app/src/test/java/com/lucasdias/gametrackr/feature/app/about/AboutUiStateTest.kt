package com.lucasdias.gametrackr.feature.app.about

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.text.UiText
import org.junit.Assert.assertEquals
import org.junit.Test

class AboutUiStateTest {
    @Test
    fun `the version text shows the build version`() {
        val state = AboutUiState(versionName = "2.4")

        assertEquals(UiText.Resource(R.string.about_version, listOf("2.4")), state.versionText)
    }
}
