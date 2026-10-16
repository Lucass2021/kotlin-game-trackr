package com.lucasdias.gametrackr.data.profile

import com.lucasdias.gametrackr.core.model.SetupImage
import com.lucasdias.gametrackr.core.model.SetupImageUpload
import com.lucasdias.gametrackr.core.model.SetupItem
import com.lucasdias.gametrackr.core.network.ApiError
import com.lucasdias.gametrackr.data.FakeLatency
import kotlinx.coroutines.delay

class FakeSetupRepository(
    setups: List<SetupItem> = ProfileMockData.setups,
    var failure: Throwable? = null,
) : SetupRepository {
    private val setups = setups.toMutableList()
    private var nextId = (setups.map { it.id } + setups.flatMap { it.images }.map { it.id }).maxOrNull()?.inc() ?: 1

    override suspend fun fetchSetups(): Result<List<SetupItem>> = respond { setups.toList() }

    override suspend fun createSetup(
        title: String,
        description: String,
        images: List<SetupImageUpload>,
    ): Result<SetupItem> =
        respond {
            SetupItem(id = nextId++, title = title, description = description, images = images.map(::fakeImage))
                .also { setups.add(it) }
        }

    override suspend fun updateSetup(
        id: Int,
        title: String,
        description: String,
        newImages: List<SetupImageUpload>,
        removedImageIds: List<Int>,
    ): Result<SetupItem> =
        respond {
            val index = setups.indexOfFirst { it.id == id }
            val current = setups.getOrNull(index) ?: throw ApiError.NotFound
            current
                .copy(
                    title = title,
                    description = description,
                    images = current.images.filterNot { it.id in removedImageIds } + newImages.map(::fakeImage),
                ).also { setups[index] = it }
        }

    override suspend fun deleteSetup(id: Int): Result<Unit> = respond { setups.removeAll { it.id == id }.let { } }

    private fun fakeImage(upload: SetupImageUpload) = SetupImage(id = nextId++, url = "$FAKE_IMAGE_SCHEME${upload.fileName}")

    private suspend fun <T> respond(block: () -> T): Result<T> {
        val error = failure
        delay(FakeLatency.RESPONSE_DELAY_MS)
        return error?.let { Result.failure(it) } ?: runCatching(block)
    }

    private companion object {
        const val FAKE_IMAGE_SCHEME = "fake://"
    }
}
