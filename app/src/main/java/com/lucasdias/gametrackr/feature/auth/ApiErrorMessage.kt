package com.lucasdias.gametrackr.feature.auth

import android.content.Context
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.network.ApiError

fun ApiError.toMessage(context: Context): String = when (this) {
    ApiError.Network -> context.getString(R.string.error_network)
    ApiError.Unauthorized -> context.getString(R.string.error_invalid_credentials)
    ApiError.NotFound -> context.getString(R.string.error_not_found)
    is ApiError.Validation -> firstMessage ?: context.getString(R.string.error_generic)
    is ApiError.Server -> context.getString(R.string.error_generic)
    is ApiError.Unknown -> context.getString(R.string.error_generic)
}
