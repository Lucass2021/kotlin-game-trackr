package com.lucasdias.gametrackr.core.network

import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json

inline fun <T> apiCall(
    json: Json,
    block: () -> T,
): Result<T> =
    try {
        Result.success(block())
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (throwable: Throwable) {
        Result.failure(throwable.toApiError(json))
    }
