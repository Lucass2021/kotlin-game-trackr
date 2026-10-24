package com.lucasdias.gametrackr.feature.auth

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.core.ui.text.UiText

fun ApiError.toUiText(): UiText =
    when (this) {
        ApiError.Network -> UiText.Resource(R.string.error_network)
        ApiError.Unauthorized -> UiText.Resource(R.string.error_invalid_credentials)
        is ApiError.Forbidden -> UiText.Resource(R.string.error_forbidden)
        ApiError.NotFound -> UiText.Resource(R.string.error_not_found)
        is ApiError.BadRequest -> UiText.Raw(errorMessage)
        is ApiError.Validation -> firstMessage?.let(UiText::Raw) ?: UiText.Resource(R.string.error_generic)
        is ApiError.Server -> UiText.Resource(R.string.error_generic)
        is ApiError.Unknown -> UiText.Resource(R.string.error_generic)
    }

fun Throwable.toUiText(): UiText = (this as? ApiError)?.toUiText() ?: UiText.Resource(R.string.error_generic)
