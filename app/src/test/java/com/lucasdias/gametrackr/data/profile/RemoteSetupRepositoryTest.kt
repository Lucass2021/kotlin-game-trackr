package com.lucasdias.gametrackr.data.profile

import com.lucasdias.gametrackr.core.model.SetupImage
import com.lucasdias.gametrackr.core.model.SetupImageUpload
import com.lucasdias.gametrackr.core.model.SetupPalette
import com.lucasdias.gametrackr.core.network.ProfileApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class RemoteSetupRepositoryTest {
    private val server = MockWebServer()
    private val json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    private lateinit var repository: RemoteSetupRepository

    @Before
    fun setUp() {
        server.start()
        val api =
            Retrofit
                .Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(ProfileApi::class.java)
        repository = RemoteSetupRepository(api, json, API_BASE_URL)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `fetch setups maps images to the api host and picks the palette from the id`() =
        runBlocking {
            server.enqueue(MockResponse().setResponseCode(HTTP_OK).setBody(SETUPS_BODY))

            val setup = repository.fetchSetups().getOrThrow().single()

            assertEquals(SETUP_ID, setup.id)
            assertEquals(TITLE, setup.title)
            assertEquals("", setup.description)
            assertEquals(listOf(SetupImage(id = IMAGE_ID, url = RESOLVED_IMAGE_URL)), setup.images)
            assertEquals(SetupPalette.forId(SETUP_ID), setup.palette)
        }

    @Test
    fun `create setup sends title, description and images as multipart`() =
        runBlocking {
            server.enqueue(MockResponse().setResponseCode(HTTP_CREATED).setBody(SETUP_BODY))

            repository.createSetup(title = TITLE, description = DESCRIPTION, images = listOf(upload())).getOrThrow()

            val request = server.takeRequest()
            val body = request.body.readUtf8()
            assertEquals("POST", request.method)
            assertEquals("/profile/setups", request.path)
            assertTrue(request.getHeader("Content-Type").orEmpty().startsWith("multipart/form-data"))
            assertTrue(body.contains("""name="title"""" + "\r\n"))
            assertTrue(body.contains(TITLE))
            assertTrue(body.contains(DESCRIPTION))
            assertTrue(body.contains("""name="images[]"; filename="$FILE_NAME""""))
        }

    @Test
    fun `update setup posts new images and removed image ids`() =
        runBlocking {
            server.enqueue(MockResponse().setResponseCode(HTTP_OK).setBody(SETUP_BODY))

            repository
                .updateSetup(
                    id = SETUP_ID,
                    title = TITLE,
                    description = DESCRIPTION,
                    newImages = listOf(upload()),
                    removedImageIds = listOf(IMAGE_ID),
                ).getOrThrow()

            val request = server.takeRequest()
            val body = request.body.readUtf8()
            assertEquals("POST", request.method)
            assertEquals("/profile/setups/$SETUP_ID", request.path)
            assertTrue(body.contains("""name="remove_image_ids[]""""))
            assertTrue(body.contains("""name="images[]"; filename="$FILE_NAME""""))
        }

    @Test
    fun `delete setup calls the setup endpoint`() =
        runBlocking {
            server.enqueue(MockResponse().setResponseCode(HTTP_OK).setBody(DELETE_BODY))

            repository.deleteSetup(SETUP_ID).getOrThrow()

            val request = server.takeRequest()
            assertEquals("DELETE", request.method)
            assertEquals("/profile/setups/$SETUP_ID", request.path)
        }

    private fun upload() = SetupImageUpload(bytes = byteArrayOf(1, 2, 3), fileName = FILE_NAME, mimeType = "image/jpeg")

    private companion object {
        const val HTTP_OK = 200
        const val HTTP_CREATED = 201
        const val SETUP_ID = 5
        const val IMAGE_ID = 9
        const val TITLE = "Main Battle Station"
        const val DESCRIPTION = "RTX 4090"
        const val FILE_NAME = "desk.jpg"
        const val IMAGE_URL = "http://localhost:8000/storage/9/desk.jpg"
        const val API_BASE_URL = "http://10.0.2.2:8000/api/"
        const val RESOLVED_IMAGE_URL = "http://10.0.2.2:8000/storage/9/desk.jpg"
        const val SETUP_JSON =
            """{"id":$SETUP_ID,"user_id":1,"title":"$TITLE","description":null,""" +
                """"images":[{"id":$IMAGE_ID,"url":"$IMAGE_URL","name":"desk","file_name":"$FILE_NAME",""" +
                """"mime_type":"image/jpeg","size":3}],"media":[]}"""
        const val SETUPS_BODY = """{"message":"Setups retrieved successfully.","data":[$SETUP_JSON]}"""
        const val SETUP_BODY = """{"message":"Setup created successfully.","data":$SETUP_JSON}"""
        const val DELETE_BODY = """{"message":"Setup deleted successfully."}"""
    }
}
