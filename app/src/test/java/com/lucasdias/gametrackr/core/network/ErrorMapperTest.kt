package com.lucasdias.gametrackr.core.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ErrorMapperTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

    private fun http(
        code: Int,
        body: String = "",
    ) = HttpException(Response.error<Any>(code, body.toResponseBody(JSON_MEDIA_TYPE)))

    private fun map(throwable: Throwable) = throwable.toApiError(json)

    @Test
    fun `maps a dropped connection to a network failure`() {
        assertEquals(ApiError.Network, map(IOException("offline")))
    }

    @Test
    fun `maps an unauthenticated 401 to unauthorized`() {
        assertEquals(ApiError.Unauthorized, map(http(401, """{"message":"Unauthenticated."}""")))
    }

    @Test
    fun `maps a 401 carrying an error key to forbidden`() {
        val error = map(http(401, """{"error":"Only the author can delete this community."}"""))

        assertEquals(ApiError.Forbidden("Only the author can delete this community."), error)
    }

    @Test
    fun `falls back to unauthorized when the 401 has no body at all`() {
        assertEquals(ApiError.Unauthorized, map(http(401)))
    }

    @Test
    fun `maps 404 to not found`() {
        assertEquals(ApiError.NotFound, map(http(404, """{"message":"Game not found"}""")))
    }

    @Test
    fun `surfaces the first field message from a 422`() {
        val error =
            map(
                http(
                    422,
                    """{"message":"The given data was invalid.","errors":{"email":["The email has already been taken."]}}""",
                ),
            ) as ApiError.Validation

        assertEquals("The email has already been taken.", error.firstMessage)
        assertEquals(listOf("The email has already been taken."), error.errors["email"])
    }

    @Test
    fun `falls back to the top level message when a 422 has no field errors`() {
        val error = map(http(422, """{"message":"The given data was invalid."}""")) as ApiError.Validation

        assertEquals("The given data was invalid.", error.firstMessage)
        assertTrue(error.errors.isEmpty())
    }

    @Test
    fun `maps a 400 with a message to a bad request`() {
        assertEquals(ApiError.BadRequest("Invalid code."), map(http(400, """{"error":"Invalid code."}""")))
        assertEquals(ApiError.BadRequest("Invalid code."), map(http(400, """{"message":"Invalid code."}""")))
    }

    @Test
    fun `treats a 400 with no readable body as a server error`() {
        assertEquals(ApiError.Server(400), map(http(400, "<html>gateway</html>")))
    }

    @Test
    fun `maps a duplicate community name 500 to a server error`() {
        assertEquals(ApiError.Server(500), map(http(500, "SQLSTATE[23000]: Duplicate entry")))
    }

    @Test
    fun `lets an already mapped api error through untouched`() {
        val original = ApiError.Forbidden("nope")

        assertTrue(map(original) === original)
    }

    @Test
    fun `wraps anything it does not recognise`() {
        val cause = IllegalStateException("boom")

        assertEquals(ApiError.Unknown(cause), map(cause))
    }

    private companion object {
        val JSON_MEDIA_TYPE = "application/json".toMediaType()
    }
}
