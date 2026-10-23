package com.lucasdias.gametrackr.feature.auth

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.ui.text.UiText
import org.junit.Assert.assertEquals
import org.junit.Test

class ApiErrorMessageTest {
    @Test
    fun `validation error uses the server message`() {
        val error = ApiError.Validation(mapOf("username" to listOf(MESSAGE)), MESSAGE)

        assertEquals(UiText.Raw(MESSAGE), error.toUiText())
    }

    @Test
    fun `validation error without a message falls back to the generic text`() {
        assertEquals(UiText.Resource(R.string.error_generic), ApiError.Validation(emptyMap(), null).toUiText())
    }

    @Test
    fun `network error uses its own text`() {
        assertEquals(UiText.Resource(R.string.error_network), ApiError.Network.toUiText())
    }

    @Test
    fun `unknown throwable falls back to the generic text`() {
        assertEquals(UiText.Resource(R.string.error_generic), IllegalStateException().toUiText())
    }

    private companion object {
        const val MESSAGE = "The username has already been taken."
    }
}
