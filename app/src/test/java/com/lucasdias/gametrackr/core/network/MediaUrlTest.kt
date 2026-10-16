package com.lucasdias.gametrackr.core.network

import org.junit.Assert.assertEquals
import org.junit.Test

class MediaUrlTest {
    @Test
    fun `localhost media urls use the api host so the emulator can reach them`() {
        assertEquals(EMULATOR_MEDIA_URL, resolveMediaUrl(LOCALHOST_MEDIA_URL, EMULATOR_API_URL))
    }

    @Test
    fun `remote media urls stay untouched`() {
        assertEquals(REMOTE_MEDIA_URL, resolveMediaUrl(REMOTE_MEDIA_URL, EMULATOR_API_URL))
    }

    @Test
    fun `invalid urls stay untouched`() {
        assertEquals(INVALID_URL, resolveMediaUrl(INVALID_URL, EMULATOR_API_URL))
    }

    private companion object {
        const val EMULATOR_API_URL = "http://10.0.2.2:8000/api/"
        const val LOCALHOST_MEDIA_URL = "http://localhost:8000/storage/1/desk.jpg"
        const val EMULATOR_MEDIA_URL = "http://10.0.2.2:8000/storage/1/desk.jpg"
        const val REMOTE_MEDIA_URL = "https://images.igdb.com/igdb/image/upload/cover.jpg"
        const val INVALID_URL = "not a url"
    }
}
