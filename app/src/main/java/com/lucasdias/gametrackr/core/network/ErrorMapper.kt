package com.lucasdias.gametrackr.core.network

import com.lucasdias.gametrackr.core.network.dto.ApiErrorBody
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toApiError(json: Json): ApiError = when (this) {
    is ApiError -> this
    is IOException -> ApiError.Network
    is HttpException -> when (val code = code()) {
        401 -> ApiError.Unauthorized
        404 -> ApiError.NotFound
        422 -> parseValidation(this, json)
        else -> ApiError.Server(code)
    }

    else -> ApiError.Unknown(this)
}

private fun parseValidation(exception: HttpException, json: Json): ApiError {
    val body = runCatching {
        exception.response()?.errorBody()?.string()?.let { json.decodeFromString<ApiErrorBody>(it) }
    }.getOrNull()

    val errors = body?.errors.orEmpty()
    val firstMessage = errors.values.firstOrNull()?.firstOrNull() ?: body?.message
    return ApiError.Validation(errors = errors, firstMessage = firstMessage)
}
