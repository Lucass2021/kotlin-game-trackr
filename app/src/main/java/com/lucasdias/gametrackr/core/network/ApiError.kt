package com.lucasdias.gametrackr.core.network

sealed class ApiError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data object Unauthorized : ApiError("Unauthorized")

    data object NotFound : ApiError("Not found")

    data class BadRequest(
        val errorMessage: String,
    ) : ApiError(errorMessage)

    data class Validation(
        val errors: Map<String, List<String>>,
        val firstMessage: String?,
    ) : ApiError(firstMessage)

    data object Network : ApiError("Network failure")

    data class Server(
        val code: Int,
    ) : ApiError("Server error $code")

    data class Unknown(
        val throwable: Throwable,
    ) : ApiError(throwable.message, throwable)
}
