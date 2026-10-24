package com.lucasdias.gametrackr.feature.auth.verifyresetcode

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.text.UiText

data class VerifyResetCodeUiState(
    val code: String = "",
    val isSubmitted: Boolean = false,
    val isLoading: Boolean = false,
    val resendSeconds: Int = RESEND_SECONDS,
    val verifiedCode: String? = null,
    val toastMessage: UiText? = null,
) {
    val codeError: UiText? get() = if (isSubmitted) codeErrorFor(code) else null

    val canSubmit: Boolean get() = codeErrorFor(code) == null

    val canResend: Boolean get() = resendSeconds <= 0

    companion object {
        const val CODE_LENGTH = 6
        const val RESEND_SECONDS = 30

        private fun codeErrorFor(code: String): UiText? =
            when {
                code.isBlank() -> UiText.Resource(R.string.validation_code_required)
                code.length != CODE_LENGTH -> UiText.Resource(R.string.validation_code_invalid, listOf(CODE_LENGTH))
                else -> null
            }
    }
}
