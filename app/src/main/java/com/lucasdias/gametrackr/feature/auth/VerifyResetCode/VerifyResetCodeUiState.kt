package com.lucasdias.gametrackr.feature.auth.verifyresetcode

import androidx.annotation.StringRes

data class VerifyResetCodeUiState(
    val code: String = "",
    @param:StringRes val codeError: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val verified: Boolean = false,
    val resendSeconds: Int = RESEND_SECONDS
)

const val RESEND_SECONDS = 30
const val CODE_LENGTH = 6
