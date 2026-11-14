package com.lucasdias.gametrackr.core.network

import java.io.IOException

class RefreshFailedException(
    val statusCode: Int? = null,
    cause: Throwable? = null,
) : IOException("Token refresh failed", cause)
